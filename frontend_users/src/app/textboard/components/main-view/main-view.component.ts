import {Component} from '@angular/core';
import {PaginationListComponent} from "../pagination-list/pagination-list.component";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {ToastsAreaComponent} from "../toasts-area/toasts-area.component";
import {ToastService} from "../../service/toast.service";
import {PostApiService} from "../../service/post-api.service";
import {NgForOf} from "@angular/common";
import {PostComponent} from "../post/post.component";
import {RouterLink} from "@angular/router";
import {Post} from "../../interfaces/application-interfaces";
import {WebsocketListenerService} from "../../service/websocket-listener.service";

@Component({
  selector: 'app-main-view',
  standalone: true,
  imports: [
    PaginationListComponent,
    ReactiveFormsModule,
    ToastsAreaComponent,
    NgForOf,
    PostComponent,
    RouterLink
  ],
  templateUrl: './main-view.component.html',
  styleUrl: './main-view.component.scss'
})
export class MainViewComponent {

  postsInPage: Post[] = [];
  nrOfElements = 0;

  // form
  newPostForm: FormGroup;

  constructor(private fb: FormBuilder, private toastService: ToastService, private postApi: PostApiService, private socketService: WebsocketListenerService) {
    this.newPostForm = this.fb.group({
      newPostTextArea: ['']
    })

    postApi.getNrOfElements(count => this.nrOfElements = count.count);
    postApi.getPostsInPage(0, data => this.postsInPage = data);
  }

  handleNewSelectedPage(newPage: number) {
    // the order for the paginator starts from 1, but for the backend api the pageIndex starts from 0
    this.postApi.getPostsInPage(newPage - 1, data => this.postsInPage = data);
  }

  newPost() {
    const text: string = this.newPostForm.controls['newPostTextArea'].value;
    console.info(text);

    if (text.length > 0) {
      //   do stuff

      this.postApi.addNewPost({text: text},data=>{
        // add the response to the top of the posts list
        this.postsInPage.unshift(data);
        this.newPostForm.controls['newPostTextArea'].setValue('');
      })


    } else {
      this.toastService.show("Invalid input", "The text must not be empty");
    }
  }
}
