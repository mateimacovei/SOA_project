import {Component, Inject} from '@angular/core';
import {UserToastsAreaComponent} from "../user-toasts-area/user-toasts-area.component";
import {UserToastService} from "../../service/userToast.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {UserPassword, UserRoleToken} from "../../interfaces/Interfaces";
import {UserApiService} from "../../service/user-api.service";
import {DOCUMENT} from "@angular/common";

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    UserToastsAreaComponent,
    ReactiveFormsModule
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {

  userPasswordForm: FormGroup;

  constructor(private userApi: UserApiService, private fb: FormBuilder, private toastService: UserToastService, private router: Router,  @Inject(DOCUMENT) private document: Document) {
    this.userPasswordForm = this.fb.group({
      username: [''],
      password: ['']
    })
  }

  private validateInput(username: string, password: string) {
    if (username.length == 0) {
      this.toastService.show("Invalid input", "The username must not be empty");
    }

    if (password.length < 4) {
      this.toastService.show("Invalid input", "The password must have at least 4 characters");
    }
  }

  private extractInput() {
    const username = this.userPasswordForm.controls['username'].value;
    const password = this.userPasswordForm.controls['password'].value;
    this.validateInput(username, password);
    const usernamePassword: UserPassword = {username: username, password: password};
    return usernamePassword;
  }

  handleSuccessfulAuthentication(data: UserRoleToken) {
    localStorage.setItem('username', data.username);
    localStorage.setItem('token', data.authorizationToken);
    localStorage.setItem('role', data.role);

    this.userPasswordForm.controls['username'].setValue('');
    this.userPasswordForm.controls['password'].setValue('');

    // this.router.navigate(['/textboard'])
    this.document.location.href = 'http://localhost:8080/application/textboard/textboard';
  }

  logIn() {
    const usernamePassword = this.extractInput();
    this.userApi.logIn(usernamePassword, (data: UserRoleToken) => {
      this.handleSuccessfulAuthentication(data);
    });
  }


  signUp() {
    const usernamePassword = this.extractInput();
    this.userApi.signUp(usernamePassword, (data: UserRoleToken) => {
      this.handleSuccessfulAuthentication(data);
    });
  }
}
