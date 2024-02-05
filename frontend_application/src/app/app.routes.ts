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
  }
];
