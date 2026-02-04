import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonContent, IonHeader, IonTitle, IonToolbar, IonButton, IonInput, IonItem } from '@ionic/angular/standalone';
import { Router } from '@angular/router';
import { Storage } from '../../services/storage';

@Component({
  selector: 'app-username',
  templateUrl: './username.page.html',
  styleUrls: ['./username.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, IonButton, IonInput, IonItem, CommonModule, FormsModule]
})
export class UsernamePage implements OnInit {

  username: string = '';

  constructor(
    private storage: Storage,
    private router: Router
  ) { }

  async ngOnInit() {
    // Check if user has already set username
    const storedUsername = await this.storage.getItem('username');
    if (storedUsername) {
      // Auto-navigate to parking list if username exists
      this.router.navigateByUrl('/parking-list', { replaceUrl: true });
    }
  }

  async onNext() {
    if (this.username.trim()) {
      await this.storage.setItem('username', this.username.trim());
      this.router.navigateByUrl('/parking-list', { replaceUrl: true });
    }
  }

}
