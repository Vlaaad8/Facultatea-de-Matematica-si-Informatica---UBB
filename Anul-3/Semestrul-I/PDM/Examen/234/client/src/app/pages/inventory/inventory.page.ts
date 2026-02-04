import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AlertController, ToastController } from '@ionic/angular';
import { Product, Item, DownloadState } from '../../models/models';
import { ProductService } from '../../services/product.service';
import { ItemService } from '../../services/item.service';
import { WebSocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.page.html',
  styleUrls: ['./inventory.page.scss']
})
export class InventoryPage implements OnInit, OnDestroy {
  public downloadState: DownloadState = {
    isDownloading: false,
    currentPage: 1,
    totalPages: 1,
    hasError: false,
    isComplete: false
  };

  public items: Item[] = [];
  public isUploading = false;
  public selectedProduct: Product | null = null;
  public quantity: number = 1;

  private destroy$ = new Subject<void>();

  constructor(
    private productService: ProductService,
    public itemService: ItemService,
    private webSocketService: WebSocketService,
    private alertController: AlertController,
    private toastController: ToastController
  ) {}

  async ngOnInit() {
    // Load data from storage
    await this.loadData();

    // Subscribe to download state
    this.productService.downloadState$
      .pipe(takeUntil(this.destroy$))
      .subscribe(state => {
        this.downloadState = state;
      });

    // Subscribe to items
    this.itemService.items$
      .pipe(takeUntil(this.destroy$))
      .subscribe(items => {
        this.items = items;
      });

    // Subscribe to upload state
    this.itemService.isUploading$
      .pipe(takeUntil(this.destroy$))
      .subscribe(uploading => {
        this.isUploading = uploading;
      });

    // Subscribe to WebSocket messages
    this.webSocketService.messages$
      .pipe(takeUntil(this.destroy$))
      .subscribe(message => {
        if (message?.event === 'productsChanged') {
          this.handleProductsChanged();
        }
      });

    // Start initial download if not complete
    if (!this.downloadState.isComplete && !this.downloadState.isDownloading) {
      this.downloadProducts();
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private async loadData() {
    await Promise.all([
      this.productService.loadProductsFromStorage(),
      this.itemService.loadItemsFromStorage()
    ]);
  }

  public async downloadProducts() {
    try {
      await this.productService.downloadProducts();
      this.showToast('Descărcarea produselor s-a finalizat cu succes!', 'success');
    } catch (error) {
      console.error('Download failed:', error);
      this.showToast('Eroare la descărcarea produselor!', 'danger');
    }
  }

  public getDownloadMessage(): string {
    if (this.downloadState.isDownloading) {
      if (this.downloadState.currentPage === 1 && this.downloadState.totalPages === 1) {
        return 'Downloading...';
      }
      return `Downloading ${this.downloadState.currentPage}/${this.downloadState.totalPages}`;
    }
    return '';
  }

  public canDownload(): boolean {
    return !this.downloadState.isComplete || this.downloadState.hasError;
  }

  public onProductSelected(product: Product) {
    this.selectedProduct = product;
  }

  public async addItem() {
    if (!this.selectedProduct || this.quantity <= 0) {
      this.showToast('Selectează un produs și introdu o cantitate validă!', 'warning');
      return;
    }

    try {
      await this.itemService.addItem(this.selectedProduct.code, this.quantity);
      this.showToast(`Produs adăugat: ${this.selectedProduct.name} (${this.quantity})`, 'success');

      // Reset form
      this.selectedProduct = null;
      this.quantity = 1;
    } catch (error) {
      console.error('Error adding item:', error);
      this.showToast('Eroare la adăugarea produsului!', 'danger');
    }
  }

  public async removeItem(item: Item) {
    const alert = await this.alertController.create({
      header: 'Confirmare',
      message: 'Ești sigur că vrei să ștergi acest element?',
      buttons: [
        {
          text: 'Anulează',
          role: 'cancel'
        },
        {
          text: 'Șterge',
          role: 'destructive',
          handler: async () => {
            try {
              await this.itemService.removeItem(item.id!);
              this.showToast('Element șters!', 'success');
            } catch (error) {
              console.error('Error removing item:', error);
              this.showToast('Eroare la ștergerea elementului!', 'danger');
            }
          }
        }
      ]
    });

    await alert.present();
  }

  public async uploadItems() {
    if (!this.itemService.hasItemsToUpload()) {
      this.showToast('Nu există elemente de trimis!', 'warning');
      return;
    }

    try {
      await this.itemService.uploadItems();
      this.showToast('Upload finalizat!', 'success');
    } catch (error) {
      console.error('Upload failed:', error);
      this.showToast('Eroare la upload!', 'danger');
    }
  }

  public getProductName(productCode: number): string {
    const product = this.productService.getProductByCode(productCode);
    return product ? product.name : `Produs ${productCode}`;
  }

  public getItemStatusColor(status: string): string {
    switch (status) {
      case 'pending': return 'medium';
      case 'submitting': return 'primary';
      case 'submitted': return 'success';
      case 'failed': return 'danger';
      default: return 'medium';
    }
  }

  public getItemStatusText(status: string): string {
    switch (status) {
      case 'pending': return 'În așteptare';
      case 'submitting': return 'Se trimite...';
      case 'submitted': return 'Trimis';
      case 'failed': return 'Eșuat';
      default: return status;
    }
  }

  private async handleProductsChanged() {
    const alert = await this.alertController.create({
      header: 'Notificare',
      message: 'Lista produselor a fost modificată pe server. Vrei să descarci din nou produsele?',
      buttons: [
        {
          text: 'Mai târziu',
          role: 'cancel'
        },
        {
          text: 'Descarcă',
          handler: () => {
            this.productService.resetDownloadState();
            this.downloadProducts();
          }
        }
      ]
    });

    await alert.present();
  }

  private async showToast(message: string, color: string) {
    const toast = await this.toastController.create({
      message: message,
      duration: 2000,
      color: color,
      position: 'top'
    });
    await toast.present();
  }

  public async clearSubmittedItems() {
    const alert = await this.alertController.create({
      header: 'Confirmare',
      message: 'Vrei să ștergi toate elementele trimise cu succes?',
      buttons: [
        {
          text: 'Anulează',
          role: 'cancel'
        },
        {
          text: 'Șterge',
          handler: async () => {
            try {
              await this.itemService.clearSubmittedItems();
              this.showToast('Elementele trimise au fost șterse!', 'success');
            } catch (error) {
              console.error('Error clearing items:', error);
              this.showToast('Eroare la ștergerea elementelor!', 'danger');
            }
          }
        }
      ]
    });

    await alert.present();
  }

  public trackByItemId(index: number, item: Item): number {
    return item.id || index;
  }

  public hasSubmittedItems(): boolean {
    return this.items.some(item => item.status === 'submitted');
  }

  public hasItemsToUpload(): boolean {
    return this.itemService.hasItemsToUpload();
  }
}
