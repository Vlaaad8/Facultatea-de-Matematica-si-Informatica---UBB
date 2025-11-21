import { Component } from '@angular/core';
import { IonApp, IonRouterOutlet } from '@ionic/angular/standalone';
import {addIcons} from "ionicons";
import {
  add,
  arrowBackOutline,
  arrowForward,
  cameraOutline,
  createOutline,
  globeOutline,
  imageOutline,
  locationOutline,
  logOut,
  mapOutline,
  wifiOutline
} from "ionicons/icons";



@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  imports: [IonApp, IonRouterOutlet],
})
export class AppComponent {

  constructor() {
    addIcons({
      'arrow-forward': arrowForward,
      'arrow-back-outline': arrowBackOutline,
      'add': add,
      'globe-outline': globeOutline,
      'wifi-outline': wifiOutline,
      'log-out': logOut,
      'camera-outline': cameraOutline,
      'location-outline': locationOutline,
      'map-outline': mapOutline,
      'image-outline': imageOutline,
      'create-outline': createOutline
    });
  }
}
