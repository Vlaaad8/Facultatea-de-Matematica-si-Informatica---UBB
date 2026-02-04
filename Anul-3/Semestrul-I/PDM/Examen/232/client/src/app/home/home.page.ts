import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonHeader, IonToolbar, IonTitle, IonContent, IonList, IonItem,
  IonLabel, IonInput, IonButton, IonSpinner, IonSegment, IonSegmentButton,
  IonIcon, IonToast
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { checkmark, close } from 'ionicons/icons';
import { InventoryItem } from '../models/inventory-item.model';
import { StorageService } from '../services/storage.service';
import { WebSocketService } from '../services/websocket.service';
import { AuditService } from '../services/audit.service';
import { Subscription, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [
    CommonModule,
    FormsModule,
    IonHeader, IonToolbar, IonTitle, IonContent, IonList, IonItem,
    IonLabel, IonInput, IonButton, IonSpinner, IonSegment, IonSegmentButton,
    IonIcon, IonToast
  ],
  providers: [AuditService, WebSocketService, StorageService]
})
export class HomePage implements OnInit, OnDestroy {
  zone: string = '';
  zoneSet: boolean = false;
  inventoryItems: InventoryItem[] = [];
  filteredItems: InventoryItem[] = [];
  isLoading: boolean = false;
  filter: 'all' | 'discrepancies' = 'all';
  showToast: boolean = false;
  toastMessage: string = '';
  toastColor: string = 'danger';
  private wsSubscription?: Subscription;

  constructor(
    private storageService: StorageService,
    private wsService: WebSocketService,
    private auditService: AuditService
  ) {
    addIcons({ checkmark, close });
  }

  async ngOnInit() {
    await this.loadZone();
    if (this.zoneSet) {
      await this.loadInventory();
    }
  }

  ngOnDestroy() {
    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
    this.wsService.disconnect();
  }

  async loadZone() {
    const savedZone = await this.storageService.get('zone');
    if (savedZone) {
      this.zone = savedZone;
      this.zoneSet = true;
    }
  }

  async setZone() {
    if (this.zone && this.zone.trim()) {
      await this.storageService.set('zone', this.zone.trim());
      this.zoneSet = true;
      await this.loadInventory();
    }
  }

  async loadInventory() {
    // Check if we have locally saved inventory
    const savedItems = await this.storageService.get('inventoryItems');

    if (savedItems && savedItems.length > 0) {
      this.inventoryItems = savedItems;
      this.applyFilter();
    } else {
      // Need to fetch from server via WebSocket
      this.isLoading = true;
      this.wsSubscription = this.wsService.connect().subscribe({
        next: async (items) => {
          this.inventoryItems = items.map(item => ({
            ...item,
            isEditing: false,
            isSending: false,
            hasError: false,
            sentToServer: false
          }));
          await this.storageService.set('inventoryItems', this.inventoryItems);
          this.applyFilter();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('WebSocket error:', error);
          this.showToastMessage('Failed to connect to server', 'danger');
          this.isLoading = false;
        }
      });
    }
  }

  selectItem(item: InventoryItem) {
    // Toggle editing mode for this item
    item.isEditing = true;
  }

  async confirmCount(item: InventoryItem, value: string | number | null | undefined) {
    const normalized = value === null || value === undefined ? '' : String(value);
    const countedValue = parseFloat(normalized);
    if (!isNaN(countedValue)) {
      item.counted = countedValue;
      item.isEditing = false;
      item.hasError = false;
      item.sentToServer = false;
      await this.saveInventory();
      this.applyFilter();
    }
  }

  cancelEdit(item: InventoryItem) {
    item.isEditing = false;
  }

  async saveInventory() {
    await this.storageService.set('inventoryItems', this.inventoryItems);
  }

  applyFilter() {
    if (this.filter === 'all') {
      this.filteredItems = [...this.inventoryItems];
    } else {
      this.filteredItems = this.inventoryItems.filter(item =>
        item.counted !== undefined && item.counted !== item.quantity
      );
    }
  }

  onFilterChange(event: any) {
    this.filter = event.detail.value;
    this.applyFilter();
  }

  async auditComplete() {
    const itemsToSend = this.inventoryItems.filter(item =>
      item.counted !== undefined && !item.sentToServer
    );

    if (itemsToSend.length === 0) {
      this.showToastMessage('No items to audit', 'warning');
      return;
    }

    // Send all items in parallel
    const promises = itemsToSend.map(item => this.sendAudit(item));
    await Promise.all(promises);

    await this.saveInventory();
    this.applyFilter();
  }

  private async sendAudit(item: InventoryItem): Promise<void> {
    item.isSending = true;
    item.hasError = false;
    item.errorMessage = undefined;

    try {
      await firstValueFrom(this.auditService.submitAudit({
        code: item.code,
        counted: item.counted!,
        zone: this.zone
      }));

      item.sentToServer = true;
      item.isSending = false;
    } catch (error: any) {
      item.hasError = true;
      item.isSending = false;
      item.sentToServer = false;

      if (error.message) {
        item.errorMessage = error.message;
      } else {
        item.errorMessage = 'Failed to send audit';
      }

      this.showToastMessage(`Error for ${item.name}: ${item.errorMessage}`, 'danger');
    }
  }

  showToastMessage(message: string, color: string) {
    this.toastMessage = message;
    this.toastColor = color;
    this.showToast = true;
  }

  onToastDismiss() {
    this.showToast = false;
  }
}
