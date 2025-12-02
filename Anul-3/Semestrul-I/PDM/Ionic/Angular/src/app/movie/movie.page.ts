import {Component, OnDestroy, OnInit, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonList,
  IonModal,
  IonSpinner,
  IonText,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {ActivatedRoute, Router} from '@angular/router';
import {Movie} from '../model/movie';
import {Home} from '../services/home';
import {GoogleMapsLoaderService} from '../services/google-maps-loader.service';
import {ToastController} from '@ionic/angular';
import {Capacitor} from '@capacitor/core';
import {filter, switchMap} from 'rxjs';

declare const google: any;

@Component({
  selector: 'app-movie',
  templateUrl: './movie.page.html',
  styleUrls: ['./movie.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonButton,
    IonIcon,
    IonList,
    IonItem,
    IonLabel,
    IonSpinner,
    IonText,
    IonModal,
    CommonModule
  ]
})
export class MoviePage implements OnInit, OnDestroy {
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(Home);
  private googleMapsLoader = inject(GoogleMapsLoaderService);
  private toastCtrl = inject(ToastController);

  public movie: Movie | null = null;
  public loading = true;
  public mapOpen = false;
  private map?: google.maps.Map;
  private marker?: google.maps.Marker;

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      const idParam = params.get('id');
      const id = idParam !== null ? Number(idParam) : NaN;

      if (!idParam || Number.isNaN(id)) {
        this.movie = null;
        this.loading = false;
        return;
      }

      this.loading = true;
      this.service.getMovie(id).subscribe({
        next: movie => {
          this.movie = movie;
          this.loading = false;
        },
        error: async error => {
          console.error('Failed to load movie', error);
          this.movie = null;
          this.loading = false;
          await this.presentToast('Could not load movie details', 'danger');
        }
      });
    });
  }

  ngOnDestroy() {
    this.destroyMap();
  }

  get hasLocation(): boolean {
    if (!this.movie?.latitude || !this.movie?.longitude) {
      return false;
    }
    const lat = Number(this.movie.latitude);
    const lng = Number(this.movie.longitude);
    return !isNaN(lat) && !isNaN(lng) && isFinite(lat) && isFinite(lng);
  }

  get photoSource(): string | null {
    if (!this.movie) {
      return null;
    }
    if (this.movie.photoUrl) {
      return this.movie.photoUrl;
    }
    if (this.movie.photoPath) {
      return Capacitor.convertFileSrc(this.movie.photoPath);
    }
    return null;
  }

  handleMain() {
    this.router.navigate(['main']);
  }

  handleEdit() {
    if (!this.movie) {
      return;
    }
    this.router.navigate(['movies', this.movie.id, 'edit']);
  }

  openMap() {
    if (!this.hasLocation) {
      return;
    }
    this.mapOpen = true;
  }

  async onMapModalPresent() {
    await this.initializeMap();
  }

  closeMap() {
    this.mapOpen = false;
    this.destroyMap();
  }

  private async initializeMap() {
    if (!this.movie || !this.hasLocation) {
      return;
    }

    try {
      await this.googleMapsLoader.load();
    } catch (error) {
      console.error('Failed to load Google Maps API:', error);
      await this.presentToast('Failed to load map. Check API key configuration.', 'danger');
      return;
    }

    const lat = Number(this.movie.latitude);
    const lng = Number(this.movie.longitude);

    const mapElement = document.getElementById('movie-preview-map');
    if (!mapElement) {
      console.error('Map container not found');
      return;
    }

    this.destroyMap();

    this.map = new google.maps.Map(mapElement, {
      center: {lat, lng},
      zoom: 14,
      zoomControl: true,
      streetViewControl: false,
      fullscreenControl: false,
      mapTypeControl: false
    });

    this.marker = new google.maps.Marker({
      position: {lat, lng},
      map: this.map
    });

    setTimeout(() => {
      if (this.map) {
        google.maps.event.trigger(this.map, 'resize');
        this.map.setCenter({lat, lng});
      }
    }, 120);
  }

  private destroyMap() {
    if (this.marker) {
      this.marker.setMap(null);
      this.marker = undefined;
    }
    if (this.map) {
      google.maps.event.clearInstanceListeners(this.map);
      this.map = undefined;
    }
  }

  private async presentToast(message: string, color: string = 'primary') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 2000,
      color,
      position: 'bottom'
    });
    await toast.present();
  }
}
