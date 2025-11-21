import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, catchError, from, Observable, of, switchMap} from "rxjs";
import {Movie} from '../../../../NodeJS/src/model/movie'
import {Network} from "@capacitor/network";
import {Preferences} from '@capacitor/preferences';


@Injectable({
  providedIn: 'root'
})
export class Home {
  private URL = 'http://localhost:8081/';
  private STORAGE_KEY = "offline_movies"
  private http = inject(HttpClient)

  private status = new BehaviorSubject<boolean>(true);

  public getMovies(pageNumber: number, pageSize: number): Observable<Movie[]> {
    return from(this.getAuthHeaders()).pipe(
      switchMap(options => this.http.get<Movie[]>(`${this.URL}movies?pageNumber=${pageNumber}&pageSize=${pageSize}`, options))
    );
  }

  private async getAuthHeaders(): Promise<{ headers: HttpHeaders }> {
    const { value: token } = await Preferences.get({ key: 'token' });
    const headers = token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
    return { headers };
  }

  constructor() {
    this.initializeNetwork();

  }

  public getMovie(id: number): Observable<Movie> {
    return from(this.getAuthHeaders()).pipe(
      switchMap(options => this.http.get<Movie>(`${this.URL}movies/${id}`, options))
    );
  }


  public async initializeNetwork() {
    await Network.addListener("networkStatusChange", async (data) => {
      const status = await Network.getStatus();
      if (status.connected) {
        this.syncOfflineMovies();
      }
      this.status.next(status.connected);
    })
  }

  private async syncOfflineMovies() {
    const list = await this.getOfflineMovies();
    const remaining: Movie[] = [];

    for (const movie of list) {
      this.addMovie(movie).subscribe({
        next: () => console.log('Synced:', movie),
        error: () => remaining.push(movie)
      });
    }

    await Preferences.set({key: this.STORAGE_KEY, value: JSON.stringify(remaining)});
  }

  public async getOfflineMovies(): Promise<Movie[]> {
    const {value} = await Preferences.get({key: this.STORAGE_KEY});
    return value ? JSON.parse(value) : [];
  }

  public getStatus() {
    return this.status.asObservable();
  }

  public addMovie(movie: Movie): Observable<Movie | null> {
    if (this.status.value) {
      return from(this.getAuthHeaders()).pipe(
        switchMap(options =>
          this.http.post<Movie>(`${this.URL}`, movie, options).pipe(
            catchError(err => {
              console.error('Eroare la adăugarea pe server:', err);
              this.saveOfflineMovie(movie);
              return of(null);
            })
          )
        )
      );
    } else {
      this.saveOfflineMovie(movie);
      return of(null);
    }
  }

  public updateMovie(movie: Movie): Observable<Movie> {
    return from(this.getAuthHeaders()).pipe(
      switchMap(options => this.http.put<Movie>(`${this.URL}movies/${movie.id}`, movie, options))
    );
  }

  public uploadPhoto(data: string, fileName?: string): Observable<{photoUrl: string, photoPath?: string}> {
    return from(this.getAuthHeaders()).pipe(
      switchMap(options => this.http.post<{photoUrl: string, photoPath?: string}>(`${this.URL}upload`, {data, fileName}, options))
    );
  }

  private async saveOfflineMovie(movie: Movie) {
    const {value} = await Preferences.get({key: this.STORAGE_KEY});
    const list: Movie[] = value ? JSON.parse(value) : [];
    list.push(movie);
    await Preferences.set({key: this.STORAGE_KEY, value: JSON.stringify(list)});
  }
}

