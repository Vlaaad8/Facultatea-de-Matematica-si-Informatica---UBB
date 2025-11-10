import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from "@angular/router"
import {
  IonBadge,
  IonButton, IonButtons, IonChip,
  IonContent, IonDatetime, IonDatetimeButton, IonFab, IonFabButton,
  IonHeader, IonIcon,
  IonInfiniteScroll, IonInfiniteScrollContent, IonInput, IonItem,
  IonLabel,
  IonList, IonListHeader, IonModal, IonSearchbar, IonSpinner,
  IonTitle, IonToggle,
  IonToolbar, ToastController
} from '@ionic/angular/standalone';
import {Movie} from "../model/movie";
import {Home} from "../services/home";
import {WebSocketService} from "../services/web-socket";
import {Preferences} from "@capacitor/preferences";
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-main',
  templateUrl: './main.page.html',
  styleUrls: ['./main.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonList, IonLabel, IonInfiniteScroll, IonInfiniteScrollContent, IonItem, IonButton, IonIcon, IonSpinner, IonBadge, IonButtons, IonModal, IonListHeader, IonFab, IonFabButton, ReactiveFormsModule, IonInput, IonToggle, IonDatetimeButton, IonDatetime, IonSearchbar, IonChip]
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
  public isOpen: boolean = false;
  public addForm!: FormGroup;
  private formBuilder = inject(FormBuilder);
  public networkStatus!: boolean;
  private toastCtrl = inject(ToastController);
  private cd = inject(ChangeDetectorRef);


  async showToast(message: string, color: string = 'primary') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 2000,
      color,
      position: 'bottom',
    });
    await toast.present();
  }

  someAction() {
    this.showToast('Movie added with success!', 'success');
  }

  showOfflineMessage() {
    this.showToast("Movie added to local storage, waiting for internet", 'Offline');
  }

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

    this.addForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      rating: [null, [Validators.required]],
      premierDate: [null, Validators.required],
      running: [true],
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

  handleOpenAddMenu(value: boolean) {
    this.isOpen = value;
  }

  saveMovie() {
    if (this.addForm.valid) {
      this.service.addMovie(this.addForm.value).subscribe({
        next: movie => {
          if (movie != null) {
            this.data.push(movie);
            this.isOpen = false;
            this.someAction();
          } else {
            this.showOfflineMessage();
            this.isOpen = false;
          }
        },
        error: error => {
          this.showOfflineMessage();
        }
      })
      this.addForm.reset();
    }
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
          this.router.navigate(['login']);
        })
      }
    })

  }
}

