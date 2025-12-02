import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class Login {
  private http = inject(HttpClient);
  private URL = 'http://localhost:8081/';

  public login(username: string, password: string) : Observable<{ token: string }> {
    return this.http.post<{ token: string }>(this.URL+'login',{username: username, password: password});
  }

}
