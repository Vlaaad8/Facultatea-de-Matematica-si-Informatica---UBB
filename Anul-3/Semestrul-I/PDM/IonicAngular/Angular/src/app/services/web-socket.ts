import { Injectable } from '@angular/core';
import { Subject } from "rxjs";
import { Movie } from "../model/movie";
import { Preferences } from '@capacitor/preferences';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private ws!: WebSocket;
  public notification = new Subject<Movie>();

  async connect(): Promise<void> {
    const { value: token } = await Preferences.get({ key: 'token' });

    if (!token) {
      console.error('No JWT token found');
      return;
    }

    this.ws = new WebSocket(`ws://localhost:8081/ws?token=${encodeURIComponent(token)}`);

    this.ws.onopen = () => {
      console.log('WebSocket connected');
      this.ws.send(JSON.stringify({ message: "Salut server!" }));
    };

    this.ws.onmessage = (event: MessageEvent) => {
      try {
        const message: Movie = JSON.parse(event.data);
        this.notification.next(message);
      } catch (err) {
        console.error('Invalid WS message', err, event.data);
      }
    };

    this.ws.onclose = () => {
      console.log('WebSocket disconnected. Trying to reconnect in 3s...');
      setTimeout(() => this.connect(), 3000);
    };

    this.ws.onerror = (err) => {
      console.error('WebSocket error:', err);
    };
  }
}
