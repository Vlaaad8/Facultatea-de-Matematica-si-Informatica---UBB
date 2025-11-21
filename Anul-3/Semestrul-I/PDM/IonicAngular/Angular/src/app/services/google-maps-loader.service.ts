import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoogleMapsLoaderService {
  private loadPromise: Promise<void> | null = null;

  load(): Promise<void> {
    if (this.loadPromise) {
      return this.loadPromise;
    }

    // Check if already loaded and ready
    if (typeof google !== 'undefined' && google.maps && google.maps.Map) {
      return Promise.resolve();
    }

    this.loadPromise = new Promise((resolve, reject) => {
      const apiKey = environment.googleMapsApiKey;
      
      if (!apiKey || typeof apiKey !== 'string' || apiKey.trim() === '' || apiKey === 'YOUR_GOOGLE_MAPS_API_KEY') {
        reject(new Error('Google Maps API key is not configured. Please set it in environment.ts'));
        return;
      }

      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&loading=async`;
      script.async = true;
      script.defer = true;
      
      script.onload = () => {
        // Wait a bit for the API to fully initialize (needed with loading=async)
        const checkReady = () => {
          if (typeof google !== 'undefined' && google.maps && google.maps.Map) {
            resolve();
          } else {
            // Retry after a short delay
            setTimeout(() => {
              if (typeof google !== 'undefined' && google.maps && google.maps.Map) {
                resolve();
              } else {
                reject(new Error('Google Maps API failed to initialize properly'));
              }
            }, 200);
          }
        };
        
        // Give it a moment to initialize
        setTimeout(checkReady, 100);
      };
      
      script.onerror = () => {
        reject(new Error('Failed to load Google Maps API script'));
      };

      document.head.appendChild(script);
    });

    return this.loadPromise;
  }
}

