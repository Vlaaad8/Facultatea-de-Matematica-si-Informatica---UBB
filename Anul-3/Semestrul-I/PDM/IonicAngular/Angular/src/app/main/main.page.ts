import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from "@angular/router"
import {
  IonBadge,
  IonButton, IonButtons,
  IonContent,
  IonHeader, IonIcon,
  IonInfiniteScroll, IonInfiniteScrollContent, IonItem,
  IonLabel,
  IonList, IonListHeader, IonModal, IonSpinner,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Movie} from "../model/movie";
import {Home} from "../services/home";
import {WebSocketService} from "../services/web-socket";
import {Notification} from "../model/notification";

@Component({
  selector: 'app-main',
  templateUrl: './main.page.html',
  styleUrls: ['./main.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonList, IonLabel, IonInfiniteScroll, IonInfiniteScrollContent, IonItem, IonButton, IonIcon, IonSpinner, IonBadge, IonButtons, IonModal, IonListHeader]
})
export class MainPage implements OnInit {

  public data!: Movie[];
  private service = inject(Home)
  private pageNumber!: number;
  private pageSize: number = 7;
  public hasAll! : boolean;
  private router = inject(Router);
  private notificationService = inject(WebSocketService);
  public nList : Notification[] = [];
  isModalOpen: boolean = false;

  ngOnInit() {
    this.pageNumber = 0;
    this.data= [];
    this.hasAll = false;
    this.service.getMovies(this.pageNumber, this.pageSize).subscribe({
      next: data => {
        this.data.push(...data);
        if(this.pageSize > data.length){
          this.hasAll = true;
        }
      },
      error: error => {
        console.log(error);
      }
    });
    this.notificationService.connect();
    this.notificationService.notification.subscribe({next: async (data :Notification) => {
      this.nList.push(data);
      }})
  }


  onIonInfinite($event: any) {
    console.log("onIonInfinite")
    console.log(this.hasAll);
    if (!this.hasAll) {
      this.pageNumber++;
      this.service.getMovies(this.pageNumber, this.pageSize).subscribe({
        next: data => {
          this.data.push(...data);
          if (this.pageSize > data.length) {
            this.hasAll = true;
          }
          $event.target.complete();


        },
        error: error => {
          console.log(error);
          $event.target.complete();
        }
      });
    }
  }

  handleMovie(movie: Movie) {
      this.router.navigate(['movies',movie.id]);
  }

  setOpen(b: boolean) {
    this.isModalOpen=b;

  }
}
