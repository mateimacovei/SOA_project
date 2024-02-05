import {Injectable} from '@angular/core';

export interface UserToastInfo {
  header: string;
  body: string;
  delay?: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserToastService {

  toasts: UserToastInfo[] = [];

  show(header: string, body: string) {
    this.toasts.push({header, body});
  }

  remove(toast: UserToastInfo) {
    this.toasts = this.toasts.filter(t => t != toast);
  }

  successfulOperation() {
    this.show("Success", "The operation was performed");
  }
}
