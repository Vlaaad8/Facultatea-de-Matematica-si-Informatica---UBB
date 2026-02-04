import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardSubtitle,
  IonCardContent,
  IonItem,
  IonLabel,
  IonInput,
  IonButton,
  IonList,
  IonSpinner,
  IonSegment,
  IonSegmentButton,
  IonText,
  ToastController
} from '@ionic/angular/standalone';
import { OrderService } from '../services/order.service';
import { MenuItem } from '../models/menu-item.model';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [
    CommonModule,
    FormsModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardSubtitle,
    IonCardContent,
    IonItem,
    IonLabel,
    IonInput,
    IonButton,
    IonList,
    IonSpinner,
    IonSegment,
    IonSegmentButton,
    IonText
  ],
})
export class HomePage implements OnInit {
  table: string = '';
  tableSet: boolean = false;
  menu: MenuItem[] = [];
  filteredMenu: MenuItem[] = [];
  isLoading: boolean = false;
  filterMode: 'all' | 'ordered' = 'all';

  constructor(
    private orderService: OrderService,
    private toastController: ToastController
  ) {}

  async ngOnInit() {
    const savedTable = await this.orderService.getTable();
    if (savedTable) {
      this.table = savedTable;
      this.tableSet = true;
      await this.loadMenu();
    }
  }

  async setTable() {
    if (this.table.trim()) {
      await this.orderService.setTable(this.table);
      this.tableSet = true;
      await this.loadMenu();
    }
  }

  async loadMenu() {
    const storedMenu = await this.orderService.loadMenuFromStorage();

    if (storedMenu.length > 0) {
      this.menu = storedMenu;
      this.applyFilter();
    } else {
      this.isLoading = true;
      try {
        this.menu = await this.orderService.connectWebSocket();
        this.applyFilter();
      } catch (error) {
        await this.showToast('Error loading menu from server', 'danger');
      } finally {
        this.isLoading = false;
      }
    }
  }

  onItemClick(item: MenuItem) {
    if (!item.isSubmitting) {
      item.isEditing = true;
    }
  }

  async confirmQuantity(item: MenuItem) {
    console.log('Confirming quantity for', item.name, 'raw value:', item.quantity, 'type:', typeof item.quantity);

    // Convertim la număr dacă e string
    if (typeof item.quantity === 'string') {
      item.quantity = parseInt(item.quantity as any, 10);
    }

    if (item.quantity !== undefined && item.quantity !== null && !isNaN(item.quantity) && item.quantity >= 0) {
      item.hasError = undefined;  // Reset pentru a permite submit
      console.log('Quantity confirmed:', item.quantity);
      await this.orderService.saveMenuToStorage(this.menu);
    } else {
      console.log('Invalid quantity, resetting to undefined');
      item.quantity = undefined;
    }

    item.isEditing = false;
    this.applyFilter();
  }

  applyFilter() {
    if (this.filterMode === 'all') {
      this.filteredMenu = [...this.menu];
    } else {
      this.filteredMenu = this.menu.filter(item => item.quantity && item.quantity > 0);
    }
  }

  setFilter(mode: 'all' | 'ordered') {
    this.filterMode = mode;
    this.applyFilter();
  }

  async submitOrders() {
    console.log('Submit clicked, checking menu:', this.menu);

    const itemsToSubmit = this.menu.filter(item => {
      const hasQuantity = item.quantity && item.quantity > 0;
      const notSubmitting = !item.isSubmitting;
      const canSubmit = item.hasError !== false; // Trimite tot ce nu e deja trimis cu succes

      console.log(`Item ${item.name}: qty=${item.quantity}, hasError=${item.hasError}, canSubmit=${canSubmit}`);

      return hasQuantity && notSubmitting && canSubmit;
    });

    console.log('Items to submit:', itemsToSubmit);

    if (itemsToSubmit.length === 0) {
      await this.showToast('No items to submit', 'warning');
      return;
    }

    const promises = itemsToSubmit.map(async (item) => {
      item.isSubmitting = true;

      try {
        await this.orderService.submitItem(item.code, item.quantity!, this.table);
        item.hasError = false;
        item.isSubmitting = false;
        await this.showToast(`${item.name} submitted successfully`, 'success');
      } catch (error: any) {
        item.hasError = true;
        item.isSubmitting = false;

        if (error.status === 0 || !error.status) {
          await this.showToast('Network error: Server unavailable', 'danger');
        } else if (error.error?.text) {
          await this.showToast(`Error for ${item.name}: ${error.error.text}`, 'danger');
        }
      }
    });

    await Promise.all(promises);
    await this.orderService.saveMenuToStorage(this.menu);
    this.applyFilter();
  }

  getTotalPrice(item: MenuItem): number {
    return item.quantity ? item.quantity * item.price : 0;
  }

  async resetApp() {
    const { Preferences } = await import('@capacitor/preferences');
    await Preferences.clear();
    console.log('Storage cleared!');
    await this.showToast('App reset! Reloading...', 'success');
    setTimeout(() => {
      window.location.reload();
    }, 1000);
  }

  private async showToast(message: string, color: string = 'primary') {
    const toast = await this.toastController.create({
      message,
      duration: 3000,
      color,
      position: 'bottom'
    });
    await toast.present();
  }
}
