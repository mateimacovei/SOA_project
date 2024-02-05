import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginPageComponent} from "./components/login-page/login-page.component";
import {UserManagementComponent} from "./components/user-management/user-management.component";
import {userComponentIsLoggedInGuard} from "./guard/user-component-is-logged-in.guard";

const routes: Routes = [
  {
    path: 'login',
    component: LoginPageComponent,
    title: 'Login'
  },
  {
    path: 'management',
    component: UserManagementComponent,
    canActivate: [userComponentIsLoggedInGuard],
    title: 'User management'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LoginRoutingModule {
}
