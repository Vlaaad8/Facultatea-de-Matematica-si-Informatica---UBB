import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonInput,
  IonButton,
  IonItem,
  IonLabel
} from '@ionic/angular/standalone';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonInput,
    IonButton,
    IonItem,
    IonLabel
  ]
})
export class LoginPage implements OnInit {
  username: string = '';

  constructor(
    private router: Router,
    private storageService: StorageService
  ) {}

  ngOnInit() {
    // Check if user has clicked Next before and has a username saved
    const hasClickedNext = this.storageService.getHasClickedNext();
    const savedUsername = this.storageService.getUsername();

    if (hasClickedNext && savedUsername) {
      // Automatically navigate to assets page
      this.router.navigate(['/assets']);
    }
  }

  onNext() {
    if (this.username.trim()) {
      this.storageService.saveUsername(this.username.trim());
      this.storageService.setHasClickedNext(true);
      this.router.navigate(['/assets']);
    }
  }
}
