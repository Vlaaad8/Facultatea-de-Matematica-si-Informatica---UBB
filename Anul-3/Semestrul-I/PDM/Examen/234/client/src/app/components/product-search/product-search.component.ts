import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { Product } from '../../models/models';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.scss']
})
export class ProductSearchComponent implements OnInit, OnDestroy {
  @Output() productSelected = new EventEmitter<Product>();

  public searchTerm = '';
  public searchResults: Product[] = [];
  public isSearching = false;

  private searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(private productService: ProductService) {}

  ngOnInit() {
    // Setup debounced search
    this.searchSubject
      .pipe(
        debounceTime(2000), // Wait 2 seconds after user stops typing
        distinctUntilChanged(), // Only emit when the value changes
        takeUntil(this.destroy$)
      )
      .subscribe(searchTerm => {
        this.performSearch(searchTerm);
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearchInput(event: any) {
    const value = event.target.value;
    this.searchTerm = value;

    if (value.trim()) {
      this.isSearching = true;
      this.searchSubject.next(value);
    } else {
      this.clearSearch();
    }
  }

  private performSearch(searchTerm: string) {
    this.isSearching = false;
    this.searchResults = this.productService.searchProducts(searchTerm);
  }

  onProductSelect(product: Product) {
    this.productSelected.emit(product);
    this.clearSearch();
  }

  private clearSearch() {
    this.searchResults = [];
    this.isSearching = false;
  }

  clearSearchInput() {
    this.searchTerm = '';
    this.clearSearch();
  }
}
