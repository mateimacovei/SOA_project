import {Injectable} from '@angular/core';
import {ToastService} from "./toast.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsocketListenerService {
  private socket: WebSocket | null;

  constructor(toastsService: ToastService) {

    const auth: string | null = localStorage.getItem('token');
    if (auth == null) {
      console.error("auth token was null during websocket instantiation");
      this.socket=null;
    } else {
      this.socket = new WebSocket('ws://localhost:8080/backend/notifications/ws/notifications?token=' + auth);

      this.socket.onmessage = (message => {
        const messageText: string = message.data.toString();
        toastsService.show("Notification", messageText);
      })
    }
  }

  close(){
    this.socket?.close();
  }
}
