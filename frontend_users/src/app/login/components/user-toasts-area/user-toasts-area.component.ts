import {Component} from '@angular/core';
import {NgbToast} from "@ng-bootstrap/ng-bootstrap";
import {UserToastService} from "../../service/userToast.service";

@Component({
  selector: 'app-user-toasts-area',
  standalone: true,
  imports: [
    NgbToast
  ],
  templateUrl: './user-toasts-area.component.html',
  styleUrl: './user-toasts-area.component.scss'
})
export class UserToastsAreaComponent {
  constructor(public toastService: UserToastService) {
  }
}
