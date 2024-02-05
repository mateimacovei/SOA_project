import {Component} from '@angular/core';
import {ToastService} from "../../service/toast.service";
import {NgbToast} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-toasts-area',
  standalone: true,
  imports: [
    NgbToast
  ],
  templateUrl: './toasts-area.component.html',
  styleUrl: './toasts-area.component.scss'
})
export class ToastsAreaComponent {
  constructor(public toastService: ToastService) {
  }
}
