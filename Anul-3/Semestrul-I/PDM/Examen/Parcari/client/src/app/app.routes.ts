import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'username',
    pathMatch: 'full',
  },
  {
    path: 'username',
    loadComponent: () => import('./pages/username/username.page').then( m => m.UsernamePage)
  },
  {
    path: 'parking-list',
    loadComponent: () => import('./pages/parking-list/parking-list.page').then( m => m.ParkingListPage)
  },
];
