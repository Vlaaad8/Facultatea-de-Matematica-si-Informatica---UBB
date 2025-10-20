import { Component } from '@angular/core';
import { IonApp, IonRouterOutlet } from '@ionic/angular/standalone';
import {addIcons} from "ionicons";
import {arrowBackOutline, arrowForward} from "ionicons/icons";



@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  imports: [IonApp, IonRouterOutlet],
})
export class AppComponent {

  constructor() {
    addIcons({ 'arrow-forward': arrowForward });
    addIcons({ 'arrow-back-outline': arrowBackOutline });
  }
}
