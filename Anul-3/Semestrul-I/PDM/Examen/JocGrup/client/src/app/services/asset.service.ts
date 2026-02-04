import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Asset } from '../models/asset.model';

@Injectable({
  providedIn: 'root'
})
export class AssetService {
  private readonly API_URL = 'http://localhost:3000';
  private readonly WS_URL = 'ws://localhost:3000';

  private ws: WebSocket | null = null;
  private assetsSubject = new BehaviorSubject<Asset[]>([]);
  private connectionStatusSubject = new BehaviorSubject<string>('disconnected');

  public assets$ = this.assetsSubject.asObservable();
  public connectionStatus$ = this.connectionStatusSubject.asObservable();

  constructor(private http: HttpClient) {}

  connectWebSocket(): void {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      return;
    }

    this.connectionStatusSubject.next('connecting');
    this.ws = new WebSocket(this.WS_URL);

    this.ws.onopen = () => {
      console.log('WebSocket connected');
      this.connectionStatusSubject.next('connected');
    };

    this.ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log('WebSocket message received:', data);

      if (Array.isArray(data)) {
        // Initial load of all assets
        this.assetsSubject.next(data);
        this.connectionStatusSubject.next('loaded');
      } else {
        // Single asset update or new asset
        const currentAssets = this.assetsSubject.value;
        const index = currentAssets.findIndex(a => a.id === data.id);

        if (index !== -1) {
          // Update existing asset
          currentAssets[index] = data;
          this.assetsSubject.next([...currentAssets]);
        } else {
          // Add new asset
          this.assetsSubject.next([...currentAssets, data]);
        }
      }
    };

    this.ws.onerror = (error) => {
      console.error('WebSocket error:', error);
      this.connectionStatusSubject.next('error');
    };

    this.ws.onclose = () => {
      console.log('WebSocket disconnected');
      this.connectionStatusSubject.next('disconnected');
    };
  }

  disconnectWebSocket(): void {
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
  }

  updateAsset(id: number, updates: Partial<Asset>): Observable<Asset> {
    return this.http.patch<Asset>(`${this.API_URL}/asset/${id}`, updates);
  }

  getAssets(): Asset[] {
    return this.assetsSubject.value;
  }

  setAssets(assets: Asset[]): void {
    this.assetsSubject.next(assets);
  }
}
