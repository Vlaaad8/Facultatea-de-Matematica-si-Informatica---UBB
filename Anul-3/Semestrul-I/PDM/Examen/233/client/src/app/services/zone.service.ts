import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ZoneService {
  private zoneSubject = new BehaviorSubject<string | null>(null);
  public zone$ = this.zoneSubject.asObservable();

  private readonly ZONE_KEY = 'audit_zone';

  constructor() {
    this.init();
  }

  async init() {
    // Load saved zone
    const savedZone = this.getZoneFromStorage();
    this.zoneSubject.next(savedZone);
  }

  private getZoneFromStorage(): string | null {
    try {
      return localStorage.getItem(this.ZONE_KEY);
    } catch (error) {
      console.error('Error reading zone from storage:', error);
      return null;
    }
  }

  async setZone(zone: string): Promise<void> {
    try {
      localStorage.setItem(this.ZONE_KEY, zone);
      this.zoneSubject.next(zone);
    } catch (error) {
      console.error('Error saving zone to storage:', error);
      throw error;
    }
  }

  getZone(): string | null {
    return this.zoneSubject.value;
  }

  isZoneSet(): boolean {
    return !!this.zoneSubject.value;
  }
}
