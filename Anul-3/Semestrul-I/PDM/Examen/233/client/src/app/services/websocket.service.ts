import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { InventoryItem } from '../models/inventory.model';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: WebSocket | null = null;
  private inventorySubject = new BehaviorSubject<InventoryItem[]>([]);
  private connectionStatusSubject = new BehaviorSubject<'disconnected' | 'connecting' | 'connected'>('disconnected');

  public inventory$ = this.inventorySubject.asObservable();
  public connectionStatus$ = this.connectionStatusSubject.asObservable();

  constructor() { }

  connect(): void {
    if (this.socket?.readyState === WebSocket.OPEN) {
      console.log('WebSocket already connected');
      return;
    }

    // Close existing connection if any
    if (this.socket) {
      this.socket.close();
    }

    console.log('Attempting to connect to WebSocket...');
    this.connectionStatusSubject.next('connecting');

    try {
      this.socket = new WebSocket('ws://localhost:3000');

      this.socket.onopen = () => {
        console.log('WebSocket connected successfully');
        this.connectionStatusSubject.next('connected');
      };

      this.socket.onmessage = (event) => {
        try {
          const rawItems: {code: number, name: string, quantity: number}[] = JSON.parse(event.data);
          console.log('Received inventory items:', rawItems);

          // Initialize all properties for inventory items
          const items: InventoryItem[] = rawItems.map(item => ({
            ...item,
            isEditing: false,
            hasError: false,
            isLoading: false,
            hasBeenSubmitted: false
          }));

          this.inventorySubject.next(items);
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.connectionStatusSubject.next('disconnected');
      };

      this.socket.onclose = (event) => {
        console.log('WebSocket disconnected. Code:', event.code, 'Reason:', event.reason);
        this.connectionStatusSubject.next('disconnected');
        this.socket = null;

        // Retry connection after 5 seconds if it wasn't a manual close
        if (event.code !== 1000) {
          console.log('Attempting to reconnect in 5 seconds...');
          setTimeout(() => {
            if (this.connectionStatusSubject.value === 'disconnected') {
              this.connect();
            }
          }, 5000);
        }
      };
    } catch (error) {
      console.error('Error creating WebSocket connection:', error);
      this.connectionStatusSubject.next('disconnected');
    }
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
      this.connectionStatusSubject.next('disconnected');
    }
  }

  getInventoryItems(): InventoryItem[] {
    return this.inventorySubject.value;
  }

  isConnected(): boolean {
    return this.connectionStatusSubject.value === 'connected';
  }
}
