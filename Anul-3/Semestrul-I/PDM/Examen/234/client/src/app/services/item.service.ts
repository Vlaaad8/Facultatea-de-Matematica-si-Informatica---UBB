import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Item } from '../models/models';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private itemsSubject = new BehaviorSubject<Item[]>([]);
  private isUploadingSubject = new BehaviorSubject<boolean>(false);

  public items$ = this.itemsSubject.asObservable();
  public isUploading$ = this.isUploadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  public async loadItemsFromStorage(): Promise<void> {
    try {
      const storedItems = await this.storageService.get('items');
      if (storedItems) {
        this.itemsSubject.next(storedItems);
      }
    } catch (error) {
      console.error('Error loading items from storage:', error);
    }
  }

  public async addItem(productCode: number, quantity: number): Promise<void> {
    const currentItems = this.itemsSubject.value;
    const newItem: Item = {
      id: Date.now(), // Simple ID generation
      code: productCode,
      quantity: quantity,
      status: 'pending'
    };

    const updatedItems = [...currentItems, newItem];
    this.itemsSubject.next(updatedItems);

    try {
      await this.storageService.set('items', updatedItems);
    } catch (error) {
      console.error('Error saving items to storage:', error);
    }
  }

  public async removeItem(itemId: number): Promise<void> {
    const currentItems = this.itemsSubject.value;
    const updatedItems = currentItems.filter(item => item.id !== itemId);

    this.itemsSubject.next(updatedItems);

    try {
      await this.storageService.set('items', updatedItems);
    } catch (error) {
      console.error('Error saving items to storage:', error);
    }
  }

  public async uploadItems(): Promise<void> {
    const items = this.itemsSubject.value.filter(item =>
      item.status === 'pending' || item.status === 'failed'
    );

    if (items.length === 0) {
      return;
    }

    this.isUploadingSubject.next(true);

    for (const item of items) {
      await this.uploadSingleItem(item);
    }

    this.isUploadingSubject.next(false);
  }

  private async uploadSingleItem(item: Item): Promise<void> {
    // Update item status to submitting
    this.updateItemStatus(item.id!, 'submitting');

    try {
      const response = await this.postItem(item).toPromise();

      if (response) {
        // Update item status to submitted
        this.updateItemStatus(item.id!, 'submitted');
      }
    } catch (error) {
      console.error(`Error uploading item ${item.id}:`, error);
      // Update item status to failed
      this.updateItemStatus(item.id!, 'failed');
    }
  }

  private postItem(item: Item): Observable<any> {
    const payload = {
      code: item.code,
      quantity: item.quantity
    };

    return this.http.post(`${environment.apiUrl}/item`, payload)
      .pipe(
        catchError(this.handleError)
      );
  }

  private async updateItemStatus(itemId: number, status: 'pending' | 'submitting' | 'submitted' | 'failed'): Promise<void> {
    const currentItems = this.itemsSubject.value;
    const updatedItems = currentItems.map(item =>
      item.id === itemId ? { ...item, status } : item
    );

    this.itemsSubject.next(updatedItems);

    try {
      await this.storageService.set('items', updatedItems);
    } catch (error) {
      console.error('Error saving items to storage:', error);
    }
  }

  public getItemStatusCount(): { pending: number; submitting: number; submitted: number; failed: number } {
    const items = this.itemsSubject.value;

    return {
      pending: items.filter(item => item.status === 'pending').length,
      submitting: items.filter(item => item.status === 'submitting').length,
      submitted: items.filter(item => item.status === 'submitted').length,
      failed: items.filter(item => item.status === 'failed').length
    };
  }

  public hasItemsToUpload(): boolean {
    const items = this.itemsSubject.value;
    return items.some(item => item.status === 'pending' || item.status === 'failed');
  }

  public async clearSubmittedItems(): Promise<void> {
    const currentItems = this.itemsSubject.value;
    const remainingItems = currentItems.filter(item => item.status !== 'submitted');

    this.itemsSubject.next(remainingItems);

    try {
      await this.storageService.set('items', remainingItems);
    } catch (error) {
      console.error('Error saving items to storage:', error);
    }
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => errorMessage);
  }
}
