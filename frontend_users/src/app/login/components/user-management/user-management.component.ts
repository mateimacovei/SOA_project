import {Component} from '@angular/core';
import {UserToastService} from "../../service/userToast.service";
import {UserApiService} from "../../service/user-api.service";
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {throwError} from "rxjs";
import {UserToastsAreaComponent} from "../user-toasts-area/user-toasts-area.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    UserToastsAreaComponent,
    RouterLink
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.scss'
})
export class UserManagementComponent {
  userForAdminAction: FormGroup;
  username: string = '';

  constructor(private fb: FormBuilder, private userApi: UserApiService, private toastsService: UserToastService, private router: Router) {
    this.userForAdminAction = this.fb.group({
      username: ['']
    })


    const loggedInUser = localStorage.getItem('username');
    if (loggedInUser == null) {
      this.router.navigate(['/user/login'])
    } else {
      this.username = loggedInUser;
    }
  }

  hasAdminRole(): boolean {
    return localStorage.getItem('role') == 'ADMIN';
  }

  getUserForAdminActionAndValidate(): string | null {
    const username = this.userForAdminAction.controls['username'].value;
    if (username.length == 0) {
      this.toastsService.show("Invalid input", "The selected username must not be empty");
      return null;
    }
    return username;
  }

  addAdminRole() {
    const user = this.getUserForAdminActionAndValidate();
    if (user != null) {
      this.userApi.addUserAsAdmin(user, data => {
        this.toastsService.successfulOperation()
      });
    }
  }

  removeAdminRole() {
    const user = this.getUserForAdminActionAndValidate();
    if (user != null) {
      this.userApi.removeUserAsAdmin(user, data => {
        this.toastsService.successfulOperation()
      });
    }
  }

  deleteUser() {
    const user = this.getUserForAdminActionAndValidate();
    if (user != null) {
      this.userApi.deleteUser(user, data => {
        this.toastsService.successfulOperation()
      });
    }
  }

  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    this.router.navigate(['/user/login'])
  }

  deleteAccount() {
    this.userApi.deleteUser(this.username, data => {
      this.toastsService.successfulOperation();
      this.router.navigate(['/user/login']);
    });
  }
}
