import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";

export const userComponentIsLoggedInGuard: CanActivateFn = (_route, _state) => {
  if (localStorage.getItem('username') !== null && localStorage.getItem('role') !== null && localStorage.getItem('token') !== null) {
    return true;
  }
  return inject(Router).navigate(['/user/login']);
};
