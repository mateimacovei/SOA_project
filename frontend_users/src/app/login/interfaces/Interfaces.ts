export interface UserPassword {
  username: string;
  password: string;
}

export enum Role {
  USER = 'user', ADMIN = 'admin'
}

export interface UserRole {
  username: string;
  role: Role;
}
export interface UserRoleToken {
  username: string;
  role: Role;
  authorizationToken: string;
}
