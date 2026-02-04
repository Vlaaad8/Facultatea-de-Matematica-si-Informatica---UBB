import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent, IonHeader, IonTitle, IonToolbar, IonList, IonItem, IonLabel,
  IonButton, IonSearchbar, IonSpinner, IonText, IonIcon
} from '@ionic/angular/standalone';
import { Router } from '@angular/router';
import { Storage } from '../../services/storage';
import { ParkingApi } from '../../services/parking-api';
import { Websocket } from '../../services/websocket';
import { ParkingSpace, ParkingSpaceUpdate } from '../../models/parking-space.model';
import { Subscription } from 'rxjs';
import { addIcons } from 'ionicons';
import { refreshOutline } from 'ionicons/icons';

@Component({
  selector: 'app-parking-list',
  templateUrl: './parking-list.page.html',
  styleUrls: ['./parking-list.page.scss'],
  standalone: true,
  imports: [
    IonContent, IonHeader, IonTitle, IonToolbar, IonList, IonItem, IonLabel,
    IonButton, IonSearchbar, IonSpinner, IonText, IonIcon,
    CommonModule, FormsModule
  ]
})
export class ParkingListPage implements OnInit, OnDestroy {

  username: string = '';
  spaces: ParkingSpace[] = [];
  filteredSpaces: ParkingSpace[] = [];
  searchQuery: string = '';
  isLoading: boolean = false;
  error: string = '';
  expandedSpaceId: number | null = null;
  updatingSpaceId: number | null = null;
  updateError: { [key: number]: string } = {};

  private wsSubscription?: Subscription;

  constructor(
    private storage: Storage,
    private parkingApi: ParkingApi,
    private websocket: Websocket,
    private router: Router
  ) {
    addIcons({ refreshOutline });
  }

  async ngOnInit() {
    // Get username from storage
    this.username = await this.storage.getItem('username');
    if (!this.username) {
      this.router.navigateByUrl('/username', { replaceUrl: true });
      return;
    }

    // Load parking spaces
    await this.loadSpaces();

    // Connect to WebSocket
    this.websocket.connect();
    this.wsSubscription = this.websocket.getMessages().subscribe({
      next: (updatedSpace) => {
        this.handleWebSocketUpdate(updatedSpace);
      },
      error: (error) => {
        console.error('WebSocket error', error);
      }
    });
  }

  ngOnDestroy() {
    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
  }

  async loadSpaces() {
    this.isLoading = true;
    this.error = '';

    try {
      const spaces = await this.parkingApi.getSpaces().toPromise();
      if (spaces) {
        // Add status field if not present
        this.spaces = spaces.map(space => ({
          ...space,
          status: space.status || (space.takenBy ? 'taken' : 'free')
        }));
        this.filteredSpaces = this.spaces;
        // Save to storage for offline access
        await this.storage.setItem('parkingSpaces', this.spaces);
      }
    } catch (error) {
      console.error('Error loading spaces', error);
      this.error = 'Failed to load parking spaces';

      // Try to load from storage
      const cachedSpaces = await this.storage.getItem('parkingSpaces');
      if (cachedSpaces) {
        this.spaces = cachedSpaces;
        this.filteredSpaces = cachedSpaces;
        this.error = 'Loaded from cache. Network unavailable.';
      }
    } finally {
      this.isLoading = false;
    }
  }

  async retryLoad() {
    await this.loadSpaces();
  }

  filterSpaces() {
    if (!this.searchQuery.trim()) {
      this.filteredSpaces = this.spaces;
    } else {
      const query = this.searchQuery.toLowerCase();
      this.filteredSpaces = this.spaces.filter(space =>
        space.number.toLowerCase().includes(query)
      );
    }
  }

  toggleSpace(spaceId: number) {
    if (this.expandedSpaceId === spaceId) {
      this.expandedSpaceId = null;
    } else {
      this.expandedSpaceId = spaceId;
    }
  }

  canTakeSpace(space: ParkingSpace): boolean {
    return !space.takenBy;
  }

  canReleaseSpace(space: ParkingSpace): boolean {
    return space.takenBy === this.username;
  }

  isExpanded(spaceId: number): boolean {
    return this.expandedSpaceId === spaceId;
  }

  getSpaceColor(space: ParkingSpace): string {
    if (!space.takenBy) {
      return 'success'; // Green for free
    } else if (space.takenBy === this.username) {
      return 'warning'; // Yellow for taken by current user
    }
    return 'medium'; // Gray for taken by others
  }

  private getCurrentStatus(space: ParkingSpace): 'taken' | 'free' {
    // If space has explicit status, use it; otherwise derive from takenBy
    if (space.status) {
      return space.status;
    }
    return space.takenBy ? 'taken' : 'free';
  }

  async takeSpace(space: ParkingSpace) {
    this.updatingSpaceId = space.id;
    delete this.updateError[space.id];

    const update: ParkingSpaceUpdate = {
      id: space.id,
      number: space.number,
      takenBy: this.username,
      status: 'taken'
    };

    try {
      const updatedSpace = await this.parkingApi.updateSpace(space.id, update).toPromise();
      if (updatedSpace) {
        this.updateSpaceInList(updatedSpace);
        this.expandedSpaceId = null;
      }
    } catch (error: any) {
      console.error('Error taking space', error);
      this.updateError[space.id] = error.error?.text || 'Failed to take space';
    } finally {
      this.updatingSpaceId = null;
    }
  }

  async releaseSpace(space: ParkingSpace) {
    this.updatingSpaceId = space.id;
    delete this.updateError[space.id];

    const update: ParkingSpaceUpdate = {
      id: space.id,
      number: space.number,
      takenBy: '',
      status: 'free'
    };

    try {
      const updatedSpace = await this.parkingApi.updateSpace(space.id, update).toPromise();
      if (updatedSpace) {
        this.updateSpaceInList(updatedSpace);
        this.expandedSpaceId = null;
      }
    } catch (error: any) {
      console.error('Error releasing space', error);
      this.updateError[space.id] = error.error?.text || 'Failed to release space';
    } finally {
      this.updatingSpaceId = null;
    }
  }

  async retryUpdate(space: ParkingSpace) {
    if (this.canTakeSpace(space)) {
      await this.takeSpace(space);
    } else if (this.canReleaseSpace(space)) {
      await this.releaseSpace(space);
    }
  }

  private updateSpaceInList(updatedSpace: ParkingSpace) {
    const index = this.spaces.findIndex(s => s.id === updatedSpace.id);
    if (index !== -1) {
      // Ensure status field is present
      const spaceWithStatus = {
        ...updatedSpace,
        status: updatedSpace.status || (updatedSpace.takenBy ? 'taken' : 'free')
      };
      this.spaces[index] = spaceWithStatus;
      this.filterSpaces();
      // Update storage
      this.storage.setItem('parkingSpaces', this.spaces);
    }
  }

  private handleWebSocketUpdate(updatedSpace: ParkingSpace) {
    console.log('Received WebSocket update', updatedSpace);
    this.updateSpaceInList(updatedSpace);
  }

}
