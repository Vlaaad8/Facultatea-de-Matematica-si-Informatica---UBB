import { Injectable } from '@angular/core';
import { Preferences } from '@capacitor/preferences';
import { BehaviorSubject } from 'rxjs';
import { MenuItem } from '../models/menu-item.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly API_URL = 'http://localhost:3000';
  private readonly WS_URL = 'ws://localhost:3000';
  private menuSubject = new BehaviorSubject<MenuItem[]>([]);
  public menu$ = this.menuSubject.asObservable();

  constructor() {}

  async getTable(): Promise<string | null> {
    const { value } = await Preferences.get({ key: 'table' });
    return value;
  }

  async setTable(table: string): Promise<void> {
    await Preferences.set({ key: 'table', value: table });
  }

  async loadMenuFromStorage(): Promise<MenuItem[]> {
    const { value } = await Preferences.get({ key: 'menu' });
    if (value) {
      const menu = JSON.parse(value);
      this.menuSubject.next(menu);
      return menu;
    }
    return [];
  }

  async saveMenuToStorage(menu: MenuItem[]): Promise<void> {
    await Preferences.set({ key: 'menu', value: JSON.stringify(menu) });
    this.menuSubject.next(menu);
  }

  connectWebSocket(): Promise<MenuItem[]> {
    return new Promise((resolve, reject) => {
      const ws = new WebSocket(this.WS_URL);

      ws.onopen = () => {
        console.log('WebSocket connected');
      };

      ws.onmessage = async (event) => {
        try {
          const menu: MenuItem[] = JSON.parse(event.data);
          await this.saveMenuToStorage(menu);
          ws.close();
          resolve(menu);
        } catch (error) {
          reject(error);
        }
      };

      ws.onerror = (error) => {
        reject(error);
      };

      ws.onclose = () => {
        console.log('WebSocket closed');
      };
    });
  }

  async submitItem(code: number, quantity: number, table: string): Promise<any> {
    const response = await fetch(`${this.API_URL}/item`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ code, quantity, table }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw { status: response.status, error };
    }

    return response.json();
  }
}
