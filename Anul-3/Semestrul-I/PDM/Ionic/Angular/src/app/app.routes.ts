import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'main',
    loadComponent: () => import('./main/main.page').then( m => m.MainPage)
  },
  {
    path: 'movies/new',
    loadComponent: () => import('./movie-edit/movie-edit.page').then( m => m.MovieEditPage)
  },
  {
    path: 'movies/:id/edit',
    loadComponent: () => import('./movie-edit/movie-edit.page').then( m => m.MovieEditPage)
  },
  {
    path: 'movies/:id',
    loadComponent: () => import('./movie/movie.page').then( m => m.MoviePage)
  },

  {
    path: 'login',
    loadComponent: () => import('./login/login.page').then( m => m.LoginPage)
  },
];
