import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { InventoryItem } from '../models/inventory-item.model';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: WebSocket | null = null;
  private inventorySubject = new Subject<InventoryItem[]>();

  constructor() {}

  connect(): Observable<InventoryItem[]> {
    if (!this.socket || this.socket.readyState === WebSocket.CLOSED) {
      this.socket = new WebSocket('ws://localhost:3000');

      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          this.inventorySubject.next(data);
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.inventorySubject.error(error);
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };
    }

    return this.inventorySubject.asObservable();
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }
}
