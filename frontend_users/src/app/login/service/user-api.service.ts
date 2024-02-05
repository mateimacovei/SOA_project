import {Injectable} from '@angular/core';
import {Role, UserPassword, UserRoleToken} from "../interfaces/Interfaces";
import {UserToastService} from "./userToast.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  URL = 'http://localhost:4201/user';

  constructor(private toastService: UserToastService, private http: HttpClient, private router: Router) {
  }

  private handleError(error: any) {
    console.error(error);
    if (error.status == 401) {
      this.navigateToLogin();
    } else if (error.status == 404) {
      this.toastService.show("Not found", error.error.errors[0]);
    } else if (error.status == 400) {
      this.toastService.show("Bad request", error.error.errors[0]);
    } else if (error.status == 403) {
      this.toastService.show("Forbidden", error.error.errors[0]);
    } else if (error.status == 409) {
      this.toastService.show("Conflict", error.error.errors[0]);
    } else if (error.status == 500) {
      this.toastService.show("Server error", error.error.errors[0]);
    }
  }

  private navigateToLogin() {
    this.router.navigate(["user/login"]);
  }

// no auth
  logIn(usernamePassword: UserPassword, callback: (data: UserRoleToken) => void) {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    const body = JSON.stringify(usernamePassword);
    // @ts-ignore
    const promise: Promise<UserRoleToken> = lastValueFrom(this.http.post(`${this.URL}/login`, body, {
      headers
    }));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  signUp(usernamePassword: UserPassword, callback: (data: UserRoleToken) => void) {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    const body = JSON.stringify(usernamePassword);
    // @ts-ignore
    const promise: Promise<UserRoleToken> = lastValueFrom(this.http.post(`${this.URL}/new`, body, {
      headers
    }));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

//   regular requests

  deleteUser(username: string, callback: (data: void) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.delete(`${this.URL}/${username}`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }


  addUserAsAdmin(username: string, callback: (data: void) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.post(`${this.URL}/adminRole/add/${username}`, null, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  removeUserAsAdmin(username: string, callback: (data: void) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.delete(`${this.URL}/adminRole/remove/${username}`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }
}
