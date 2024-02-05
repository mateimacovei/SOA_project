import {Injectable} from '@angular/core';
import {ToastService} from "./toast.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {Comment, NewComment, NewPost, Post} from "../interfaces/application-interfaces";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CommentApiService {

  URL = 'http://localhost:4202/comment';

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

  getCommentsForPost(postId: number, callback: (data: Comment[]) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token')
      }
    };
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.get(`${this.URL}/post/${postId}`, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  addComment(newComment: NewComment, callback: (data: Comment) => void) {
    const options = {
      headers: {
        'Authorization': localStorage.getItem('token'),
        'Content-Type': 'application/json'
      }
    };
    const body = JSON.stringify(newComment);
    // @ts-ignore
    const promise: Promise<any> = lastValueFrom(this.http.post(this.URL, body, options));

    promise.then(callback).catch(error => {
      this.handleError(error);
    });
  }

  deleteComment(id: number, callback: (data: void) => void) {
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
