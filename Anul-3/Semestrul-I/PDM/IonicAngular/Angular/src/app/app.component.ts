import { Component } from '@angular/core';
import { IonApp, IonRouterOutlet } from '@ionic/angular/standalone';
import {addIcons} from "ionicons";
import {add, arrowBackOutline, arrowForward, globeOutline, logOut, wifiOutline} from "ionicons/icons";



@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  imports: [IonApp, IonRouterOutlet],
})
export class AppComponent {

  constructor() {
    addIcons({ 'arrow-forward': arrowForward });
    addIcons({ 'arrow-back-outline': arrowBackOutline });
    addIcons({'add':add})
    addIcons({'globe-outline':globeOutline})
    addIcons({'wifi-outline':wifiOutline})
    addIcons({'log-out':logOut})
  }
}
