import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ParkingSpace, ParkingSpaceUpdate } from '../models/parking-space.model';

@Injectable({
  providedIn: 'root',
})
export class ParkingApi {

  private baseUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) { }

  getSpaces(): Observable<ParkingSpace[]> {
    return this.http.get<ParkingSpace[]>(`${this.baseUrl}/space`);
  }

  updateSpace(id: number, space: ParkingSpaceUpdate): Observable<ParkingSpace> {
    return this.http.put<ParkingSpace>(`${this.baseUrl}/space/${id}`, space);
  }
}
