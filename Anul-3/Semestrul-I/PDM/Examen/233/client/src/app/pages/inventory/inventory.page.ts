import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { InventoryService } from '../../services/inventory.service';
import { AuditService } from '../../services/audit.service';
import { ZoneService } from '../../services/zone.service';
import { InventoryItem, FilterType } from '../../models/inventory.model';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.page.html',
  styleUrls: ['./inventory.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule]
})
export class InventoryPage implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  items: InventoryItem[] = [];
  filteredItems: InventoryItem[] = [];
  currentFilter: string = 'all';
  connectionStatus: 'disconnected' | 'connecting' | 'connected' = 'disconnected';
  zone: string | null = null;
  isSubmittingAudit = false;

  // Filter options
  FilterType = FilterType;

  constructor(
    private inventoryService: InventoryService,
    private auditService: AuditService,
    private zoneService: ZoneService
  ) { }

  ngOnInit() {
    console.log('Inventory page loading...');

    // Get current zone
    this.zoneService.zone$.pipe(takeUntil(this.destroy$)).subscribe(zone => {
      this.zone = zone;
      console.log('Zone loaded:', zone);

      // Per requirement 2: Only proceed if zone is set
      if (zone) {
        this.connectIfNeeded();
      }
    });

    // Subscribe to connection status
    this.inventoryService.getConnectionStatus().pipe(takeUntil(this.destroy$)).subscribe(status => {
      this.connectionStatus = status;
      console.log('Connection status:', status);
    });

    // Subscribe to filtered inventory items
    this.inventoryService.getFilteredItems().pipe(takeUntil(this.destroy$)).subscribe(items => {
      this.filteredItems = items;
      console.log('Filtered items updated:', items.length);
    });

    // Subscribe to all items for audit purposes
    this.inventoryService.inventory$.pipe(takeUntil(this.destroy$)).subscribe(items => {
      this.items = items;
      console.log('All items updated:', items.length);
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private async connectIfNeeded() {
    console.log('Checking if WebSocket connection is needed...',
      'Has data:', this.inventoryService.hasInventoryData(),
      'Connection status:', this.connectionStatus);

    // Per requirement 2: Connect only if no inventory data exists locally
    if (!this.inventoryService.hasInventoryData() && this.connectionStatus !== 'connected') {
      console.log('No local data found, connecting to WebSocket...');
      await this.inventoryService.connectToServer();
    } else {
      console.log('Using existing local data');
    }
  }

  onItemClick(item: InventoryItem) {
    if (!item.isLoading) {
      this.inventoryService.setItemEditing(item.code, !item.isEditing);
    }
  }

  async onCountChange(item: InventoryItem, event: any) {
    const value = event.target.value;
    const counted = parseInt(value, 10);

    console.log('Count change:', item.code, value, counted);

    if (!isNaN(counted) && counted >= 0) {
      await this.inventoryService.updateItemCount(item.code, counted);
      console.log('Updated item count:', item.code, counted);
    } else if (value === '' || value === null || value === undefined) {
      // Clear the counted value if input is empty
      await this.inventoryService.updateItemCount(item.code, undefined);
    }
  }

  onEnterPressed(item: InventoryItem, event: any) {
    const counted = parseInt(event.target.value, 10);
    if (!isNaN(counted) && counted >= 0) {
      this.inventoryService.updateItemCount(item.code, counted);
      this.inventoryService.setItemEditing(item.code, false);
    }
  }

  onFilterChange(event: any) {
    const filter = event.detail.value;
    console.log('Filter changed to:', filter);
    this.currentFilter = filter;

    // Convert string to enum
    const filterType = filter === 'discrepancies' ? FilterType.DISCREPANCIES : FilterType.ALL;
    this.inventoryService.setFilter(filterType);
  }

  async submitAudit() {
    if (this.isSubmittingAudit) return;

    // Get all items with counted values
    const itemsWithCounted = this.getItemsWithCountedValues();

    if (itemsWithCounted.length === 0) {
      console.log('No items have counted values');
      return;
    }

    console.log('Submitting audit for items:', itemsWithCounted);

    this.isSubmittingAudit = true;

    try {
      await this.auditService.submitAudit(itemsWithCounted);
    } finally {
      this.isSubmittingAudit = false;
    }
  }

  async retryFailedItem(item: InventoryItem) {
    if (item.hasError && item.counted !== undefined) {
      await this.auditService.submitAudit([item]);
    }
  }

  getConnectionStatusText(): string {
    switch (this.connectionStatus) {
      case 'connecting': return 'Connecting to server...';
      case 'connected': return 'Connected';
      case 'disconnected': return 'Disconnected';
      default: return 'Unknown';
    }
  }

  getConnectionStatusColor(): string {
    switch (this.connectionStatus) {
      case 'connecting': return 'warning';
      case 'connected': return 'success';
      case 'disconnected': return 'danger';
      default: return 'medium';
    }
  }

  hasItemsToAudit(): boolean {
    const itemsWithCounted = this.items.filter(item =>
      item.counted !== undefined && item.counted !== null
    );
    console.log('Items with counted:', itemsWithCounted);
    console.log('Items for audit:', this.items.filter(item =>
      item.counted !== undefined && !item.hasError && !item.isLoading
    ));

    return this.items.some(item =>
      item.counted !== undefined && item.counted !== null && !item.hasError && !item.isLoading
    );
  }

  getItemsWithCountedValues(): InventoryItem[] {
    return this.items.filter(item =>
      item.counted !== undefined && item.counted !== null
    );
  }

  getFailedItemsCount(): number {
    return this.items.filter(item =>
      item.counted !== undefined && item.hasError
    ).length;
  }

  trackByCode(index: number, item: InventoryItem): number {
    return item.code;
  }

  async reconnectToServer() {
    console.log('Manual reconnection attempt...');
    await this.inventoryService.connectToServer();
  }

  getDebugInfo(): string {
    return `Items: ${this.items.length}, Zone: ${this.zone ? 'Set' : 'Not set'}, Status: ${this.connectionStatus}`;
  }

  loadTestData() {
    // Generate test data for debugging
    const testItems: InventoryItem[] = [
      {
        code: 0, name: 'Product 0', quantity: 25,
        counted: 20, // Pre-fill with counted value
        isEditing: false, hasError: false, isLoading: false, hasBeenSubmitted: false
      },
      {
        code: 1, name: 'Product 1', quantity: 15,
        isEditing: false, hasError: false, isLoading: false, hasBeenSubmitted: false
      },
      {
        code: 2, name: 'Product 2', quantity: 35,
        counted: 30, // Pre-fill with counted value
        isEditing: false, hasError: false, isLoading: false, hasBeenSubmitted: false
      },
    ];

    // Auto-set zone if not set
    if (!this.zone) {
      this.zoneService.setZone('Test Zone');
    }

    // Force update the inventory through the service
    this.items = testItems;
    this.filteredItems = testItems;

    // Also update the inventory service
    this.inventoryService['inventorySubject'].next(testItems);

    console.log('Test data loaded with pre-filled counted values:', testItems);
  }

  forceCreateItems() {
    console.log('🚀 FORȚEZ CREAREA DE ITEME!');

    // Creez iteme standard din server
    const serverItems: InventoryItem[] = Array.from(Array(5).keys()).map(code => ({
      code,
      name: `Product ${code}`,
      quantity: Math.floor(Math.random() * 50) + 1,
      isEditing: false,
      hasError: false,
      isLoading: false,
      hasBeenSubmitted: false
    }));

    // Setez zona automat
    if (!this.zone) {
      this.zoneService.setZone('Warehouse Zone');
    }

    // Forțez update direct
    this.items = serverItems;
    this.filteredItems = serverItems;

    // Update și serviciul
    this.inventoryService['inventorySubject'].next(serverItems);

    console.log('✅ ITEME FORȚATE CREATED:', serverItems);
  }
}
