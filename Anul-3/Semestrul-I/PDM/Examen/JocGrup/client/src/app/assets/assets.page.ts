import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
  IonList,
  IonItem,
  IonLabel,
  IonButton,
  IonSegment,
  IonSegmentButton,
  IonText,
  IonSpinner,
  IonCard,
  IonCardContent,
  ToastController,
  AlertController
} from '@ionic/angular/standalone';
import { Subscription } from 'rxjs';
import { Asset, AssetStatus } from '../models/asset.model';
import { AssetService } from '../services/asset.service';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-assets',
  templateUrl: './assets.page.html',
  styleUrls: ['./assets.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    IonList,
    IonItem,
    IonLabel,
    IonButton,
    IonSegment,
    IonSegmentButton,
    IonText,
    IonSpinner,
    IonCard,
    IonCardContent
  ]
})
export class AssetsPage implements OnInit, OnDestroy {
  assets: Asset[] = [];
  filteredAssets: Asset[] = [];
  username: string = '';
  selectedFilter: AssetStatus | 'all' = 'all';
  expandedAssetId: number | null = null;
  isLoading: boolean = true;
  connectionStatus: string = 'disconnected';

  private assetsSubscription?: Subscription;
  private connectionSubscription?: Subscription;
  private pendingOperation: { id: number, updates: Partial<Asset> } | null = null;

  constructor(
    private assetService: AssetService,
    private storageService: StorageService,
    private toastController: ToastController,
    private alertController: AlertController
  ) {}

  ngOnInit() {
    this.username = this.storageService.getUsername() || '';

    // Load cached assets immediately
    const cachedAssets = this.storageService.getAssets();
    if (cachedAssets.length > 0) {
      this.assets = cachedAssets;
      this.assetService.setAssets(cachedAssets);
      this.applyFilter();
      this.isLoading = false;
    }

    // Subscribe to assets updates
    this.assetsSubscription = this.assetService.assets$.subscribe(assets => {
      if (assets.length > 0) {
        this.assets = assets;
        this.storageService.saveAssets(assets);
        this.applyFilter();
        this.isLoading = false;
      }
    });

    // Subscribe to connection status
    this.connectionSubscription = this.assetService.connectionStatus$.subscribe(status => {
      this.connectionStatus = status;
      if (status === 'loaded') {
        this.showToast('Assets loaded successfully!');
      }
    });

    // Connect to WebSocket
    this.assetService.connectWebSocket();
  }

  ngOnDestroy() {
    this.assetsSubscription?.unsubscribe();
    this.connectionSubscription?.unsubscribe();
    this.assetService.disconnectWebSocket();
  }

  getAssetStatus(asset: Asset): AssetStatus {
    if (asset.takenBy === this.username) {
      return 'red';
    } else if (!asset.takenBy && asset.desiredBy.length === 0) {
      return 'green';
    } else if (!asset.takenBy && asset.desiredBy[0] === this.username) {
      return 'green';
    } else if (asset.desiredBy.includes(this.username) && asset.desiredBy[0] !== this.username) {
      return 'yellow';
    }
    return 'white';
  }

  getBackgroundColor(asset: Asset): string {
    const status = this.getAssetStatus(asset);
    switch (status) {
      case 'red': return '#ffcccc';
      case 'green': return '#ccffcc';
      case 'yellow': return '#ffffcc';
      default: return '#ffffff';
    }
  }

  applyFilter() {
    if (this.selectedFilter === 'all') {
      this.filteredAssets = this.assets;
    } else {
      this.filteredAssets = this.assets.filter(asset =>
        this.getAssetStatus(asset) === this.selectedFilter
      );
    }
  }

  onFilterChange(event: any) {
    this.selectedFilter = event.detail.value;
    this.applyFilter();
  }

  toggleExpand(assetId: number) {
    this.expandedAssetId = this.expandedAssetId === assetId ? null : assetId;
  }

  getButtonLabel(asset: Asset): string {
    const status = this.getAssetStatus(asset);
    switch (status) {
      case 'red': return 'Return';
      case 'green': return 'Take';
      case 'yellow': return 'Remove request';
      default: return 'Add request';
    }
  }

  async onAssetAction(asset: Asset) {
    const status = this.getAssetStatus(asset);
    let updates: Partial<Asset> = {};

    switch (status) {
      case 'red':
        // Return: remove user from takenBy, assign to next in desiredBy if any
        if (asset.desiredBy.length > 0) {
          updates = {
            takenBy: asset.desiredBy[0],
            desiredBy: asset.desiredBy.slice(1)
          };
        } else {
          updates = {
            takenBy: null,
            desiredBy: []
          };
        }
        break;

      case 'green':
        // Take: set takenBy to current user
        updates = {
          takenBy: this.username,
          desiredBy: asset.desiredBy.filter(u => u !== this.username)
        };
        break;

      case 'yellow':
        // Remove request: remove user from desiredBy
        updates = {
          takenBy: asset.takenBy,
          desiredBy: asset.desiredBy.filter(u => u !== this.username)
        };
        break;

      case 'white':
        // Add request: add user to desiredBy
        updates = {
          takenBy: asset.takenBy,
          desiredBy: [...asset.desiredBy, this.username]
        };
        break;
    }

    this.pendingOperation = { id: asset.id, updates };
    this.performUpdate(asset.id, updates);
  }

  async performUpdate(id: number, updates: Partial<Asset>) {
    try {
      await this.assetService.updateAsset(id, updates).toPromise();
      this.showToast('Asset updated successfully!');
      this.pendingOperation = null;
    } catch (error) {
      console.error('Failed to update asset:', error);
      this.showErrorAlert();
    }
  }

  async showErrorAlert() {
    const alert = await this.alertController.create({
      header: 'Error',
      message: 'Failed to update asset. Would you like to retry?',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            this.pendingOperation = null;
          }
        },
        {
          text: 'Retry',
          handler: () => {
            if (this.pendingOperation) {
              this.performUpdate(this.pendingOperation.id, this.pendingOperation.updates);
            }
          }
        }
      ]
    });

    await alert.present();
  }

  async showToast(message: string) {
    const toast = await this.toastController.create({
      message,
      duration: 2000,
      position: 'bottom'
    });
    await toast.present();
  }
}
