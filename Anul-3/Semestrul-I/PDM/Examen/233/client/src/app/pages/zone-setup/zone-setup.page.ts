import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { ZoneService } from '../../services/zone.service';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-zone-setup',
  templateUrl: './zone-setup.page.html',
  styleUrls: ['./zone-setup.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule, CommonModule]
})
export class ZoneSetupPage implements OnInit {
  zone = '';
  isLoading = false;

  constructor(
    private zoneService: ZoneService,
    private router: Router,
    private toastController: ToastController
  ) { }

  ngOnInit() {
    // Check if zone is already set
    this.zoneService.zone$.subscribe(zone => {
      if (zone) {
        this.router.navigate(['/inventory'], { replaceUrl: true });
      }
    });
  }

  async setZone() {
    if (!this.zone.trim()) {
      await this.showToast('Please enter a zone name', 'warning');
      return;
    }

    this.isLoading = true;

    try {
      await this.zoneService.setZone(this.zone.trim());
      await this.showToast('Zone set successfully!', 'success');
      this.router.navigate(['/inventory'], { replaceUrl: true });
    } catch (error) {
      console.error('Error setting zone:', error);
      await this.showToast('Error setting zone. Please try again.', 'danger');
    } finally {
      this.isLoading = false;
    }
  }

  private async showToast(message: string, color: 'success' | 'danger' | 'warning' = 'success') {
    const toast = await this.toastController.create({
      message,
      duration: 3000,
      color,
      position: 'top'
    });
    await toast.present();
  }
}
