import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuditRequest, AuditResponse } from '../models/audit-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuditService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  submitAudit(auditData: AuditRequest): Observable<AuditResponse> {
    return this.http.post<AuditResponse>(`${this.apiUrl}/audit`, auditData)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      if (error.status === 0) {
        errorMessage = 'Unable to connect to server. Please check your connection.';
      } else if (error.error && error.error.text) {
        errorMessage = error.error.text;
      } else if (error.message) {
        errorMessage = error.message;
      }
    }

    return throwError(() => ({ status: error.status, message: errorMessage }));
  }
}
