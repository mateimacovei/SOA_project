import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'textboard',
    pathMatch: 'full'
  },
  {
    path: 'textboard',
    loadChildren: () => import('./textboard/textboard.module').then(m => m.TextboardModule)
  },
  {
    path: 'user',
    loadChildren: () => import('./login/login.module').then(m => m.LoginModule)
  }
];
