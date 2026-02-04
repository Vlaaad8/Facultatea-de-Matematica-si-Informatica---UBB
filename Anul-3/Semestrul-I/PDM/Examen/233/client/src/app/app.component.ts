import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { ZoneService } from './services/zone.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  standalone: true,
  imports: [IonicModule, CommonModule, RouterOutlet],
})
export class AppComponent implements OnInit {
  constructor(
    private zoneService: ZoneService,
    private router: Router
  ) {}

  async ngOnInit() {
    // Initialize zone service
    await this.zoneService.init();

    // Handle routing based on zone setup
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.handleRouting();
    });

    // Initial routing check
    this.handleRouting();
  }

  private handleRouting() {
    const currentUrl = this.router.url;
    const isZoneSet = this.zoneService.isZoneSet();

    if (!isZoneSet && currentUrl !== '/zone-setup') {
      this.router.navigate(['/zone-setup'], { replaceUrl: true });
    } else if (isZoneSet && (currentUrl === '/zone-setup' || currentUrl === '/')) {
      this.router.navigate(['/inventory'], { replaceUrl: true });
    }
  }
}
