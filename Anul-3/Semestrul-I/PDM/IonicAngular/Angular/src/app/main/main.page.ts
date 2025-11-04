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

  ngOnInit() {
    this.pageNumber = 0;
    this.data = [];
    this.hasAll = false;
    this.service.getMovies(this.pageNumber, this.pageSize).subscribe({
      next: datas => {
        this.data = [...this.data, ...datas];
        this.filteredData = [...this.data];
        if (this.pageSize > datas.length) {
          this.hasAll = true;
        }
        this.cd.detectChanges();
      },
      error: error => {
        console.log(error);
      }
    });
    this.notificationService.connect();
    this.notificationService.notification.subscribe({
      next: async (data: Movie) => {
        this.data.push(data);
        this.filteredData.push(data)
      }
    })

    this.addForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      rating: [null, [Validators.required]],
      premierDate: [null, Validators.required],
      running: [true],
    });
    this.service.getStatus().subscribe({
      next: data => {
        this.networkStatus = data;
      },
      error: error => {
        console.log(error);
      }
    })
  }


  onIonInfinite($event: any) {
    if (!this.hasAll) {
      this.pageNumber++;
      this.service.getMovies(this.pageNumber, this.pageSize).subscribe({
        next: data => {
          this.data = [...this.data, ...data];
          this.filteredData = [...this.data];
          if (this.pageSize > data.length) {
            this.hasAll = true;
          }
          console.log('Total Movies Loaded (this.data.length):', this.data.length);
          console.log('Filtered Movies Count (this.filteredData.length):', this.filteredData.length);
          $event.target.complete();
          this.cd.detectChanges();

        },
        error: error => {
          console.log(error);
          $event.target.complete();
        }
      });
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
          if (movie!=null) {
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

  filterMovies($event: any) {
    console.log($event.target.value);
    const value = $event.target.value?.toLowerCase().trim() || '';
    if (value == '') {
      this.filteredData = this.data;
    } else {

      this.filteredData = this.data.filter(movie => {
        return movie.name.toLowerCase().includes(value)
      });
    }
  }
  filterByType(type: string) {
    this.data.forEach(item => {console.log(JSON.stringify(item));});
    if(type=="Running"){
      this.filteredData= this.data.filter(movie => {return movie.running})
    }
    else if(type=="NRunning"){
      this.filteredData= this.data.filter(movie => {return !movie.running})
    }
    else if(type=="Reset"){
      this.filteredData = [...this.data];
    }
    console.log("-----------------------------------------")
    this.filteredData.forEach(item => {console.log(JSON.stringify(item));});
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

