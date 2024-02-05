import {Component, Input} from '@angular/core';
import {Post} from "../../interfaces/application-interfaces";
import {RouterLink} from "@angular/router";
import {DatePipe} from "@angular/common";

/**
 * represents a post in the list of posts form the main page
 */
@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    RouterLink,
    DatePipe
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  @Input() post!: Post;

  getCreatedDate() {
    // return new Date(parseInt(this.post.createdDate.toString())).toString().substring();
    return new Date(parseInt(this.post.createdDate.toString()));
  }
}
