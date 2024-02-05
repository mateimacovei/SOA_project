import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import { DOCUMENT } from '@angular/common';

export const isLoggedInGuard: CanActivateFn = (_route, _state) => {
  if (localStorage.getItem('username') !== null && localStorage.getItem('role') !== null && localStorage.getItem('token') !== null) {
    return true;
  }
  inject(DOCUMENT).location.href ='http://localhost:8080/application/user/user/login';
  return false;
};
