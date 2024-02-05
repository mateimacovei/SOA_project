import {Injectable} from '@angular/core';
import WebSocket from 'ws';
import {ToastService} from "./toast.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsocketListenerService {
  private socket: WebSocket;

  constructor(toastsService: ToastService) {

    // const WebSocket = require('ws')
    // const Buffer = require('buffer/').Buffer;
    // @ts-ignore
    const auth: string = localStorage.getItem('token');
    this.socket = new WebSocket('ws://localhost:4204/ws/notifications', {
      headers: {
        Authorization: auth
      },
    });
    //
    // this.socket.onmessage = (message => {
    //   const messageText: string = message.data.toString();
    //   toastsService.show("Notification", messageText);
    // })
  }
}
