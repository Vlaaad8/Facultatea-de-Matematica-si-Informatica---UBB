import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private ws: WebSocket | null = null;
  private wsUrl = 'ws://localhost:3000';
  public taskUpdates$ = new Subject<Task>();
  private reconnectInterval = 5000;
  private reconnectTimer: any;

  constructor() {
    this.connect();
  }

  private connect(): void {
    try {
      this.ws = new WebSocket(this.wsUrl);

      this.ws.onopen = () => {
        console.log('WebSocket connected');
        if (this.reconnectTimer) {
          clearTimeout(this.reconnectTimer);
          this.reconnectTimer = null;
        }
      };

      this.ws.onmessage = (event) => {
        try {
          const task = JSON.parse(event.data) as Task;
          console.log('WebSocket message received:', task);
          this.taskUpdates$.next(task);
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

      this.ws.onclose = () => {
        console.log('WebSocket disconnected, reconnecting...');
        this.reconnect();
      };
    } catch (error) {
      console.error('Error connecting to WebSocket:', error);
      this.reconnect();
    }
  }

  private reconnect(): void {
    if (!this.reconnectTimer) {
      this.reconnectTimer = setTimeout(() => {
        console.log('Attempting to reconnect...');
        this.connect();
      }, this.reconnectInterval);
    }
  }

  disconnect(): void {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
  }
}
