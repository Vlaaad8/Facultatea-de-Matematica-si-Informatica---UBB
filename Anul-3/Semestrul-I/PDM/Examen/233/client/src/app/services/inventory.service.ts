import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { InventoryItem, FilterType } from '../models/inventory.model';
import { WebSocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private inventorySubject = new BehaviorSubject<InventoryItem[]>([]);
  private filterSubject = new BehaviorSubject<FilterType>(FilterType.ALL);

  public inventory$ = this.inventorySubject.asObservable();
  public filter$ = this.filterSubject.asObservable();

  private readonly INVENTORY_KEY = 'inventory_data';
  private readonly AUDIT_DATA_KEY = 'audit_data';

  constructor(private webSocketService: WebSocketService) {
    this.init();
    this.setupWebSocketSubscription();
  }

  private async init() {
    // Per requirement 3: Load saved inventory and audit data first
    this.loadFromStorage();

    // Per requirement 2: Only connect if we don't have inventory data
    // Don't auto-connect here, let the page decide when to connect
  }

  private setupWebSocketSubscription() {
    this.webSocketService.inventory$.subscribe(async (items) => {
      if (items.length > 0) {
        // Merge with existing audit data
        const existingAuditData = this.getAuditDataFromStorage();
        const mergedItems = this.mergeWithAuditData(items, existingAuditData);

        this.inventorySubject.next(mergedItems);
        this.saveToStorage(mergedItems);
      }
    });
  }

  private loadFromStorage() {
    try {
      const savedInventory = localStorage.getItem(this.INVENTORY_KEY);
      if (savedInventory) {
        this.inventorySubject.next(JSON.parse(savedInventory));
      }
    } catch (error) {
      console.error('Error loading inventory from storage:', error);
    }
  }

  private saveToStorage(items: InventoryItem[]) {
    try {
      localStorage.setItem(this.INVENTORY_KEY, JSON.stringify(items));

      // Save audit data separately for persistence
      const auditData = items
        .filter(item => item.counted !== undefined)
        .map(item => ({ code: item.code, counted: item.counted }));
      localStorage.setItem(this.AUDIT_DATA_KEY, JSON.stringify(auditData));
    } catch (error) {
      console.error('Error saving inventory to storage:', error);
    }
  }

  private getAuditDataFromStorage(): {code: number, counted: number}[] {
    try {
      const auditData = localStorage.getItem(this.AUDIT_DATA_KEY);
      return auditData ? JSON.parse(auditData) : [];
    } catch (error) {
      console.error('Error loading audit data from storage:', error);
      return [];
    }
  }

  private mergeWithAuditData(items: InventoryItem[], auditData: {code: number, counted: number}[]): InventoryItem[] {
    return items.map(item => {
      const auditEntry = auditData.find(audit => audit.code === item.code);
      return {
        ...item,
        counted: auditEntry?.counted,
        isEditing: false,
        hasError: false,
        isLoading: false,
        hasBeenSubmitted: false
      };
    });
  }

  async updateItemCount(code: number, counted: number | undefined) {
    const items = this.inventorySubject.value;
    const updatedItems = items.map(item =>
      item.code === code
        ? { ...item, counted, isEditing: false, hasError: false }
        : item
    );

    console.log('Inventory service updating:', code, counted);
    this.inventorySubject.next(updatedItems);
    this.saveToStorage(updatedItems);
  }

  setItemEditing(code: number, isEditing: boolean) {
    const items = this.inventorySubject.value;
    const updatedItems = items.map(item =>
      item.code === code
        ? { ...item, isEditing }
        : { ...item, isEditing: false }
    );

    this.inventorySubject.next(updatedItems);
  }

  setItemError(code: number, hasError: boolean, errorMessage?: string) {
    const items = this.inventorySubject.value;
    const updatedItems = items.map(item =>
      item.code === code
        ? { ...item, hasError, errorMessage }
        : item
    );

    this.inventorySubject.next(updatedItems);
  }

  setItemLoading(code: number, isLoading: boolean) {
    const items = this.inventorySubject.value;
    const updatedItems = items.map(item =>
      item.code === code
        ? { ...item, isLoading }
        : item
    );

    this.inventorySubject.next(updatedItems);
  }

  setItemSubmitted(code: number, hasBeenSubmitted: boolean) {
    const items = this.inventorySubject.value;
    const updatedItems = items.map(item =>
      item.code === code
        ? { ...item, hasBeenSubmitted, hasError: false }
        : item
    );

    this.inventorySubject.next(updatedItems);
    this.saveToStorage(updatedItems);
  }

  setFilter(filter: FilterType) {
    this.filterSubject.next(filter);
  }

  getFilteredItems(): Observable<InventoryItem[]> {
    return new Observable(observer => {
      this.inventory$.subscribe(items => {
        this.filter$.subscribe(filter => {
          let filteredItems = items;

          if (filter === FilterType.DISCREPANCIES) {
            filteredItems = items.filter(item =>
              item.counted !== undefined && item.counted !== item.quantity
            );
          }

          observer.next(filteredItems);
        });
      });
    });
  }

  getItemsWithCounts(): InventoryItem[] {
    return this.inventorySubject.value.filter(item => item.counted !== undefined);
  }

  hasInventoryData(): boolean {
    return this.inventorySubject.value.length > 0;
  }

  async connectToServer(): Promise<void> {
    this.webSocketService.connect();
  }

  isConnected(): boolean {
    return this.webSocketService.isConnected();
  }

  getConnectionStatus(): Observable<'disconnected' | 'connecting' | 'connected'> {
    return this.webSocketService.connectionStatus$;
  }
}
