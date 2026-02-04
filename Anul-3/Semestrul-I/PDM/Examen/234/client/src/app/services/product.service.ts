import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Product, ProductResponse, DownloadState } from '../models/models';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsSubject = new BehaviorSubject<Product[]>([]);
  private downloadStateSubject = new BehaviorSubject<DownloadState>({
    isDownloading: false,
    currentPage: 1,
    totalPages: 1,
    hasError: false,
    isComplete: false
  });

  public products$ = this.productsSubject.asObservable();
  public downloadState$ = this.downloadStateSubject.asObservable();

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  public async loadProductsFromStorage(): Promise<void> {
    try {
      const storedProducts = await this.storageService.get('products');
      const storedDownloadState = await this.storageService.get('downloadState');

      if (storedProducts) {
        this.productsSubject.next(storedProducts);
      }

      if (storedDownloadState) {
        this.downloadStateSubject.next(storedDownloadState);
      }
    } catch (error) {
      console.error('Error loading products from storage:', error);
    }
  }

  public async downloadProducts(): Promise<void> {
    const currentState = this.downloadStateSubject.value;
    let startPage = 1;

    if (currentState.hasError && currentState.errorPage) {
      startPage = currentState.errorPage;
    }

    this.updateDownloadState({
      isDownloading: true,
      currentPage: startPage,
      totalPages: 1,
      hasError: false,
      errorPage: undefined,
      isComplete: false
    });

    try {
      await this.downloadFromPage(startPage);
    } catch (error) {
      console.error('Error starting download:', error);
      this.updateDownloadState({
        ...this.downloadStateSubject.value,
        isDownloading: false,
        hasError: true
      });
    }
  }

  private async downloadFromPage(startPage: number): Promise<void> {
    let allProducts: Product[] = [];
    const currentState = this.downloadStateSubject.value;

    // If we're resuming from an error, keep existing products
    if (startPage > 1 && currentState.hasError) {
      allProducts = this.productsSubject.value;
    }

    try {
      // Get first page to determine total pages
      const firstResponse = await this.getProductPage(startPage).toPromise();
      if (!firstResponse) throw new Error('No response received');

      const totalPages = Math.ceil(firstResponse.total / 10);

      this.updateDownloadState({
        ...currentState,
        totalPages,
        currentPage: startPage
      });

      // Add products from first page if we're starting fresh
      if (startPage === 1) {
        allProducts = [...firstResponse.products];
      } else if (startPage <= totalPages) {
        // Remove products from the error page onwards and add new ones
        const productsBeforeErrorPage = allProducts.slice(0, (startPage - 1) * 10);
        allProducts = [...productsBeforeErrorPage, ...firstResponse.products];
      }

      this.productsSubject.next(allProducts);
      await this.storageService.set('products', allProducts);

      // Download remaining pages
      for (let page = startPage + 1; page <= totalPages; page++) {
        this.updateDownloadState({
          ...this.downloadStateSubject.value,
          currentPage: page
        });

        try {
          const response = await this.getProductPage(page).toPromise();
          if (!response) throw new Error(`No response for page ${page}`);

          allProducts = [...allProducts, ...response.products];
          this.productsSubject.next(allProducts);
          await this.storageService.set('products', allProducts);

        } catch (error) {
          console.error(`Error downloading page ${page}:`, error);
          this.updateDownloadState({
            ...this.downloadStateSubject.value,
            isDownloading: false,
            hasError: true,
            errorPage: page
          });
          await this.storageService.set('downloadState', this.downloadStateSubject.value);
          throw error;
        }
      }

      // Download completed successfully
      this.updateDownloadState({
        ...this.downloadStateSubject.value,
        isDownloading: false,
        isComplete: true
      });

      await this.storageService.set('downloadState', this.downloadStateSubject.value);

    } catch (error) {
      console.error('Download failed:', error);
      throw error;
    }
  }

  private getProductPage(page: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${environment.apiUrl}/product?page=${page}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  public searchProducts(searchTerm: string): Product[] {
    if (!searchTerm.trim()) {
      return [];
    }

    const products = this.productsSubject.value;
    const filtered = products
      .filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
      )
      .slice(0, 5);

    return filtered;
  }

  public getProductByCode(code: number): Product | undefined {
    return this.productsSubject.value.find(product => product.code === code);
  }

  private updateDownloadState(state: DownloadState): void {
    this.downloadStateSubject.next(state);
  }

  public resetDownloadState(): void {
    this.updateDownloadState({
      isDownloading: false,
      currentPage: 1,
      totalPages: 1,
      hasError: false,
      isComplete: false
    });
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
