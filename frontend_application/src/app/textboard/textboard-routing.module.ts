import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainViewComponent} from "./components/main-view/main-view.component";
import {PostWithCommentsComponent} from "./components/post-with-comments/post-with-comments.component";
import {isLoggedInGuard} from "./guard/is-logged-in.guard";

const routes: Routes = [
  {
    path: '',
    component: MainViewComponent,
    pathMatch: 'full',
    canActivate: [isLoggedInGuard],
    title:'Textboard'
  },
  {
    path: 'post/:id',
    component: PostWithCommentsComponent,
    canActivate: [isLoggedInGuard],
    title:'Textboard post'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TextboardRoutingModule {
}
