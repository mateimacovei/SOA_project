import {Injectable} from '@angular/core';
import {ToastService} from "./toast.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsocketListenerService {

  constructor(toastsService: ToastService) {

    const auth: string | null = localStorage.getItem('token');
    if (auth == null) {
      console.error("auth token was null during websocket instantiation");
    } else {
      const socket = new WebSocket('ws://localhost:4204/ws/notifications?token=' + auth);

      socket.onmessage = (message => {
        const messageText: string = message.data.toString();
        toastsService.show("Notification", messageText);
      })
    }
  }
}
