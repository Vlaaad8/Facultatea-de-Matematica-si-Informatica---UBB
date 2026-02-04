import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private readonly USERNAME_KEY = 'username';
  private readonly ASSETS_KEY = 'assets';
  private readonly HAS_CLICKED_NEXT_KEY = 'hasClickedNext';

  constructor() {}

  // Username
  saveUsername(username: string): void {
    localStorage.setItem(this.USERNAME_KEY, username);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  // Assets
  saveAssets(assets: any[]): void {
    localStorage.setItem(this.ASSETS_KEY, JSON.stringify(assets));
  }

  getAssets(): any[] {
    const stored = localStorage.getItem(this.ASSETS_KEY);
    return stored ? JSON.parse(stored) : [];
  }

  // Has clicked next flag
  setHasClickedNext(value: boolean): void {
    localStorage.setItem(this.HAS_CLICKED_NEXT_KEY, value.toString());
  }

  getHasClickedNext(): boolean {
    return localStorage.getItem(this.HAS_CLICKED_NEXT_KEY) === 'true';
  }

  clearAll(): void {
    localStorage.clear();
  }
}
