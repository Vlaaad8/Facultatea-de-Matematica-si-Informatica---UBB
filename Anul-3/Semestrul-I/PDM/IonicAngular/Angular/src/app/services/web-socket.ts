import { Injectable } from '@angular/core';
import {Subject} from "rxjs"
import {Notification} from "../model/notification";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private ws!: WebSocket;
  public notification = new Subject<Notification>();

  connect(): void{
    this.ws=new WebSocket(('ws://localhost:8080/ws'))
    this.ws.onmessage = (event: MessageEvent) => {
      const message :Notification = JSON.parse(event.data);
      this.notification.next(message);
      if(this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({message: "I received."}));
      }
    }
  }

}
