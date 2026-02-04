import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { ParkingSpace } from '../models/parking-space.model';

@Injectable({
  providedIn: 'root',
})
export class Websocket {

  private socket: WebSocket | null = null;
  private messageSubject = new Subject<ParkingSpace>();
  private wsUrl = 'ws://localhost:3000';
  private reconnectInterval = 5000;
  private isConnecting = false;

  constructor() { }

  connect(): void {
    if (this.socket?.readyState === WebSocket.OPEN || this.isConnecting) {
      return;
    }

    this.isConnecting = true;
    console.log('Attempting to connect to WebSocket...');

    try {
      this.socket = new WebSocket(this.wsUrl);

      this.socket.onopen = () => {
        console.log('WebSocket connected');
        this.isConnecting = false;
      };

      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          this.messageSubject.next(data);
        } catch (error) {
          console.error('Error parsing WebSocket message', error);
        }
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error', error);
        this.isConnecting = false;
      };

      this.socket.onclose = () => {
        console.log('WebSocket closed, attempting to reconnect...');
        this.isConnecting = false;
        this.socket = null;
        setTimeout(() => this.connect(), this.reconnectInterval);
      };
    } catch (error) {
      console.error('Error creating WebSocket', error);
      this.isConnecting = false;
      setTimeout(() => this.connect(), this.reconnectInterval);
    }
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  getMessages(): Observable<ParkingSpace> {
    return this.messageSubject.asObservable();
  }
}
