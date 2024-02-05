import {Component, Inject} from '@angular/core';
import {Comment, Post, Role} from "../../interfaces/application-interfaces";
import {PostApiService} from "../../service/post-api.service";
import {CommentApiService} from "../../service/comment-api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastsAreaComponent} from "../toasts-area/toasts-area.component";
import {DatePipe, DOCUMENT, NgForOf, NgIf} from "@angular/common";
import {PostComponent} from "../post/post.component";
import {ToastService} from "../../service/toast.service";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-post-with-comments',
  standalone: true,
  imports: [
    ToastsAreaComponent,
    NgForOf,
    PostComponent,
    DatePipe,
    NgIf,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './post-with-comments.component.html',
  styleUrl: './post-with-comments.component.scss'
})
export class PostWithCommentsComponent {
  post?: Post;
  comments: Comment[] = [];
  postId: number;
  username: string = '';
  role: Role = Role.USER;
  readonly: 'readonly' | '' = 'readonly';
  canEditPost: boolean = false;

  //forms
  modalForm: FormGroup;
  postTextForm: FormGroup;

  constructor(private fb: FormBuilder, private postApi: PostApiService, private commentsApi: CommentApiService, private router: Router, private activatedRoute: ActivatedRoute, private toastsService: ToastService,  @Inject(DOCUMENT) private document: Document) {
    this.modalForm = this.fb.group({
      modalTextArea: ['']
    })

    this.postTextForm = this.fb.group({
      postText: ['']
    })


    const loggedInUser = localStorage.getItem('username');
    const loggedInRole = localStorage.getItem('role');
    if (loggedInUser == null || loggedInRole == null) {
      // this.router.navigate(['/user/login']);
      this.document.location.href = 'http://localhost:8080/application/user/user/login';
    } else {
      this.username = loggedInUser;
      this.role = Role[loggedInRole as keyof typeof Role];
    }

    this.postId = Number(activatedRoute.snapshot.params['id']);
    this.postApi.getOnePost(this.postId, data => {
      this.post = data;
      this.canEditPost = this.role == Role.ADMIN || this.username === data.createdBy;
      this.postTextForm.controls['postText'].setValue(this.post.text);
    });
    commentsApi.getCommentsForPost(this.postId, data => this.comments = data)
  }

  getCreatedDate(millis: number) {
    return new Date(parseInt(millis.toString()));
  }

  getCreatedDateForPost() {
    // @ts-ignore
    return this.getCreatedDate(this.post?.createdDate)
  }

  deletePostPressed() {
    this.postApi.deletePost(this.postId, () => {
      this.router.navigate(["/textboard"]).then(() => this.toastsService.successfulOperation())
    })
  }

  isInEditPostMode(): boolean {
    return this.readonly === '';
  }

  editPostPressed() {
    this.readonly = '';
  }

  performUpdatePost() {
    const newText: string = this.postTextForm.controls['postText'].value;
    if (newText.length < 1) {
      this.toastsService.show("Invalid input", "The post must have some text");
    } else {
      this.postApi.updatePost({text: newText, id: this.postId}, data => {
        this.toastsService.successfulOperation();
        this.readonly = 'readonly';
      })
    }
  }


//   comments


  modalOkPressed() {
    const commentText: string = this.modalForm.controls['modalTextArea'].value;
    if (commentText.length < 1) {
      this.toastsService.show("Invalid input", "The comment must have some text");
    } else {
      this.commentsApi.addComment({postId: this.postId, text: commentText}, data => {
        this.comments.unshift(data);
        this.modalForm.controls['modalTextArea'].setValue('');
      });
    }
  }

  deleteComment(commentId: number) {
    this.commentsApi.deleteComment(commentId, () => {
      this.comments = this.comments.filter(x => x.id != commentId);
      this.toastsService.successfulOperation();
    })
  }

  showDeleteButton(comment: Comment): boolean {
    return this.role === Role.ADMIN || this.username === comment.createdBy;
  }
}
