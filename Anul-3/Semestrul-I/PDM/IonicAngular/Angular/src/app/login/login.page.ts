import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {IonContent, IonHeader, IonInput, IonItem, IonTitle, IonToolbar} from '@ionic/angular/standalone';
import {Login} from "../services/login";
import {Router} from "@angular/router";
import {Preferences} from '@capacitor/preferences';
@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, ReactiveFormsModule, IonInput, IonItem],
})
export class LoginPage implements OnInit {

  private formBuilder= inject(FormBuilder);
  public loginForm!: any;
  private service = inject(Login);
  private router = inject(Router);

  constructor() {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    })
    Preferences.get({key: "token"}).then((result) => {
      if(result.value) {
        this.router.navigate(['main']);
      }
    })
  }

  async handleLogin() {
    const { username, password } = this.loginForm.value;

    this.service.login(username, password).subscribe({
      next: async (data) => {
        if (data) {
          await Preferences.set({
            key: "token",
            value: data.token
          });

          console.log("Token salvat:", data.token);
          this.router.navigate(['main']);
        } else {
          console.error("Răspuns invalid: lipsește tokenul!");
        }
      },
      error: (error) => {
        console.error("Eroare la autentificare:", error);
      }
    });
  }

}
