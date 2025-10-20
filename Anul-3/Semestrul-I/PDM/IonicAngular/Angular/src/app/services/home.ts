import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {Movie} from '../../../../NodeJS/src/model/movie'
@Injectable({
  providedIn: 'root'
})
export class Home {

  private URL = 'http://localhost:8080/';
  private http = inject(HttpClient)

  public getMovies(pageNumber: number, pageSize: number): Observable<Movie[]> {
    return this.http.get<Movie[]>(this.URL + 'movies?pageNumber=' + pageNumber + '&pageSize=' + pageSize);


  }
  public getMovie(id: number): Observable<Movie> {
    return this.http.get<Movie>(this.URL + `movies/${id}`);
  }
}

