import {Injectable} from '@angular/core';
import {NewPost, Post, PostCount, PostUpdate} from "../interfaces/application-interfaces";
import {ToastService} from "./toast.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {lastValueFrom} from "rxjs";
import {UserRoleToken} from "../../login/interfaces/Interfaces";

@Injectable({
  providedIn: 'root'
})
export class PostApiService {

  URL = 'http://localhost:8080/backend/application/post';

  constructor(private toastService: ToastService, private http: HttpClient, private router: Router) {
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


  getNrOfElements(callback: (data: PostCount) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.get(`${this.URL}/count`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  getPostsInPage(pageIndex: number, callback: (data: Post[]) => void) {
    let params = new HttpParams();
    params = params.append('pageIndex', pageIndex);
    params = params.append('pageSize', 50);
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      },
      params: params
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.get(`${this.URL}/`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  getOnePost(id: number, callback: (data: Post) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.get(`${this.URL}/${id}`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }


  addNewPost(newPost: NewPost, callback: (data: Post) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token'),
        'Content-Type': 'application/json'
      }
    };
    const body = JSON.stringify(newPost);
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.post(this.URL, body, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  updatePost(postUpdate: PostUpdate, callback: (data: Post) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token'),
        'Content-Type': 'application/json'
      }
    };
    const body = JSON.stringify(postUpdate);
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.put(this.URL, body, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  deletePost(id: number, callback: (data: void) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.delete(`${this.URL}/${id}`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }
}
