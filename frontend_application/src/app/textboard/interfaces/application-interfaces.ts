export enum Role {
  USER = 'user', ADMIN = 'admin'
}


// posts

export interface PostCount {
  count: number;
}

export interface Post {
  id: number;
  text: string;
  createdBy: string;
  createdDate: bigint;
}

export interface PostUpdate {
  id: number;
  text: string;
}

export interface NewPost {
  text: string;
}


// comments
export interface Comment {
  id: number;
  text: string;
  createdBy: string;
  createdDate: number;
}

export interface NewComment {
  text: string;
  postId: number;
}
