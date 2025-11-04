import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonSpinner,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {ActivatedRoute, Router} from "@angular/router";
import {Home} from "../services/home";
import {Movie} from "../model/movie";
import {filter,switchMap} from "rxjs";

@Component({
  selector: 'app-movie',
  templateUrl: './movie.page.html',
  styleUrls: ['./movie.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonLabel, IonItem, IonIcon, IonSpinner]
})
export class MoviePage implements OnInit {
  private router = inject(ActivatedRoute);
  private route = inject(Router)
  private service = inject(Home);
  public movie: Movie | null = null;

  ngOnInit() {
    this.router.paramMap.pipe(
      filter(params => params.has('id')),
      switchMap(params => {
        const id = Number(params.get('id'));
        return this.service.getMovie(id);
      })
    ).subscribe({
      next: (data : Movie) => {
        this.movie = data;
        console.log(this.movie);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  handleMain() {
    this.route.navigate(['main']);
  }
}


