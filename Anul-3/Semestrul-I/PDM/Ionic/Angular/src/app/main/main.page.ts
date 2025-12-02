import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from "@angular/router"
import {
  IonButton, IonButtons, IonChip,
  IonContent, IonFab, IonFabButton,
  IonHeader, IonIcon,
  IonInfiniteScroll, IonInfiniteScrollContent, IonItem,
  IonLabel,
  IonList, IonSearchbar, IonSpinner,
  IonThumbnail,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Movie} from "../model/movie";
import {Home} from "../services/home";
import {WebSocketService} from "../services/web-socket";
import {Preferences} from "@capacitor/preferences";
import { lastValueFrom } from 'rxjs';
import {animate, query, stagger, style, transition, trigger} from '@angular/animations';
import {Capacitor} from '@capacitor/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.page.html',
  styleUrls: ['./main.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonList, IonLabel, IonInfiniteScroll, IonInfiniteScrollContent, IonItem, IonButton, IonIcon, IonSpinner, IonButtons, IonFab, IonFabButton, IonSearchbar, IonChip, IonThumbnail],
  animations: [
    trigger('listAnimation', [
      transition('* <=> *', [
        query(':enter', [
          style({ opacity: 0, transform: 'translateY(20px)' }),
          stagger('100ms', [
            animate('300ms cubic-bezier(0.35, 0, 0.25, 1)', style({ opacity: 1, transform: 'translateY(0)' }))
          ])
        ], { optional: true })
      ])
    ])
  ]
})
export class MainPage implements OnInit {

  public data!: Movie[];
  public filteredData: Movie[] = [];
  private service = inject(Home)
  private pageNumber!: number;
  private pageSize: number = 7;
  public hasAll!: boolean;
  private router = inject(Router);
  private notificationService = inject(WebSocketService);
  public networkStatus!: boolean;
  private cd = inject(ChangeDetectorRef);

  async ngOnInit() {
    this.pageNumber = 0;
    this.data = [];
    this.filteredData = [];
    this.hasAll = false;

    try {
      const movies = await lastValueFrom(this.service.getMovies(this.pageNumber, this.pageSize));
      this.data.push(...movies);
      this.filteredData = [...this.data];
      this.hasAll = movies.length < this.pageSize;
      this.cd.detectChanges();
    } catch (err) {
      console.error('Error loading movies:', err);
    }

    this.notificationService.connect();
    this.notificationService.notification.subscribe({
      next: (movie: Movie) => {
        this.data.push(movie);
        this.filteredData.push(movie);
        this.cd.detectChanges();
      }
    });

    this.service.getStatus().subscribe({
      next: status => this.networkStatus = status
    });
  }


  async onIonInfinite(event: any) {
    this.pageNumber++;
    try {
      const movies = await lastValueFrom(this.service.getMovies(this.pageNumber, this.pageSize));
      this.data.push(...movies);
      this.filteredData.push(...movies);
      this.hasAll = movies.length < this.pageSize;
      event.target.complete();
      this.cd.detectChanges();
    } catch (err) {
      console.error(err);
      event.target.complete();
    }
  }


  handleMovie(movie: Movie) {
    this.router.navigate(['movies', movie.id]);
  }

  filterMovies(event: any) {
    const value = event.target.value?.toLowerCase().trim() || '';
    this.filteredData = value ? this.data.filter(m => m.name.toLowerCase().includes(value)) : [...this.data];
  }

  filterByType(type: string) {
    if(type === "Running") this.filteredData = this.data.filter(m => m.running);
    else if(type === "NRunning") this.filteredData = this.data.filter(m => !m.running);
    else this.filteredData = [...this.data];
  }

  handleLogOut() {
    Preferences.get({key: "token"}).then((result) => {
      if (result.value) {
        const key = 'token';
        Preferences.remove({key}).then(() => {
          this.router.navigate(['/login']);
        })
      }
    })

  }

  handleAddMovie() {
    this.router.navigate(['movies', 'new']);
  }

  handleEditMovie(movie: Movie, event?: Event) {
    event?.stopPropagation();
    this.router.navigate(['movies', movie.id, 'edit']);
  }

  getMovieImage(movie: Movie): string | null {
    if (movie.photoUrl) {
      return movie.photoUrl;
    }
    if (movie.photoPath) {
      return Capacitor.convertFileSrc(movie.photoPath);
    }
    return null;
  }
}

