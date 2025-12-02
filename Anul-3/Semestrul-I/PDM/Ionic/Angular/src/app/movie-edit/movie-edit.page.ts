import {Component, ElementRef, OnDestroy, OnInit, ViewChild, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonBackButton,
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonIcon,
  IonInput,
  IonItem,
  IonLabel,
  IonList,
  IonModal,
  IonNote,
  IonSpinner,
  IonText,
  IonTitle,
  IonToggle,
  IonToolbar
} from '@ionic/angular/standalone';
import {ActivatedRoute, Router} from '@angular/router';
import {Movie} from '../model/movie';
import {Home} from '../services/home';
import {GoogleMapsLoaderService} from '../services/google-maps-loader.service';
import {AnimationController, ToastController} from '@ionic/angular';
import {Camera, CameraPhoto, CameraResultType, CameraSource} from '@capacitor/camera';
import {Filesystem, Directory} from '@capacitor/filesystem';
import {Capacitor} from '@capacitor/core';
import {Geolocation} from '@capacitor/geolocation';

declare const google: any;

@Component({
  selector: 'app-movie-edit',
  templateUrl: './movie-edit.page.html',
  styleUrls: ['./movie-edit.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonBackButton,
    IonButton,
    IonList,
    IonItem,
    IonLabel,
    IonInput,
    IonToggle,
    IonNote,
    IonText,
    IonSpinner,
    IonIcon,
    IonModal,
    ReactiveFormsModule,
    CommonModule
  ]
})
export class MovieEditPage implements OnInit, OnDestroy {
  @ViewChild('webCameraVideo') webCameraVideo?: ElementRef<HTMLVideoElement>;

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private service = inject(Home);
  private googleMapsLoader = inject(GoogleMapsLoaderService);
  private toastCtrl = inject(ToastController);
  private animationCtrl = inject(AnimationController);

  public form: FormGroup = this.fb.group({
    name: ['', Validators.required],
    rating: [null, [Validators.required, Validators.min(0), Validators.max(10)]],
    premierDate: ['', Validators.required],
    running: [true],
    photoPath: [null],
    photoUrl: [null],
    latitude: [null],
    longitude: [null],
    locationLabel: ['']
  });

  public loading = true;
  public isNew = true;
  public photoPreview: string | null = null;
  public mapModalOpen = false;
  public isUploading = false;
  public webCameraModalOpen = false;
  public webCameraError: string | null = null;

  private movieId?: number;
  private currentMovie?: Movie;
  private map?: google.maps.Map;
  private marker?: google.maps.Marker;
  private selectedCoords: {lat: number; lng: number} | null = null;
  private webCameraStream?: MediaStream;

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam && idParam !== 'new') {
        this.isNew = false;
        this.movieId = Number(idParam);
        if (!Number.isNaN(this.movieId)) {
          this.fetchMovie(this.movieId);
          return;
        }
      }
      this.isNew = true;
      this.loading = false;
    });
  }

  ngOnDestroy() {
    this.destroySelectionMap();
    this.stopWebCameraStream();
  }

  get currentPhotoSource(): string | null {
    if (this.photoPreview) {
      return this.photoPreview;
    }

    const photoUrl = this.form.get('photoUrl')?.value;
    if (photoUrl) {
      return photoUrl;
    }

    const photoPath = this.form.get('photoPath')?.value;
    if (photoPath) {
      return Capacitor.convertFileSrc(photoPath);
    }

    return null;
  }

  fetchMovie(id: number) {
    this.loading = true;
    this.service.getMovie(id).subscribe({
      next: movie => {
        this.currentMovie = movie;
        const premierDate = movie.premierDate ? new Date(movie.premierDate).toISOString().slice(0, 10) : '';
        this.form.patchValue({
          name: movie.name,
          rating: movie.rating,
          premierDate,
          running: movie.running,
          photoPath: movie.photoPath ?? null,
          photoUrl: movie.photoUrl ?? null,
          latitude: movie.latitude ?? null,
          longitude: movie.longitude ?? null,
          locationLabel: movie.locationLabel ?? ''
        });
        this.photoPreview = movie.photoUrl ?? this.resolveLocalPhoto(movie.photoPath);
        this.loading = false;
      },
      error: async () => {
        this.loading = false;
        await this.presentToast('Failed to load movie details', 'danger');
      }
    });
  }

  async takePhoto() {
    if (Capacitor.getPlatform() === 'web') {
      this.webCameraModalOpen = true;
      return;
    }
    await this.capturePhoto(CameraSource.Camera);
  }

  async selectPhotoFromGallery() {
    await this.capturePhoto(CameraSource.Photos);
  }

  private async capturePhoto(source: CameraSource) {
    try {
      const image = await Camera.getPhoto({
        source,
        resultType: CameraResultType.Base64,
        quality: 80,
        correctOrientation: true,
        allowEditing: false,
        webUseInput: Capacitor.getPlatform() === 'web'
          ? source !== CameraSource.Camera
          : false
      });

      await this.handleCapturedPhoto(image);
    } catch (error: any) {
      if (error?.message?.includes('User cancelled') || error?.message?.includes('cancelled')) {
        return;
      }
      const action = source === CameraSource.Camera ? 'capture photo' : 'select photo';
      console.error(`Camera error while trying to ${action}:`, error);
      await this.presentToast(`Failed to ${action}. Please check permissions.`, 'danger');
    }
  }

  private async handleCapturedPhoto(image: CameraPhoto) {
    if (!image.base64String) {
      return;
    }
    await this.processBase64Photo(image.base64String, image.format ?? 'jpeg');
  }

  async onWebCameraModalPresent() {
    this.webCameraError = null;
    await this.startWebCameraStream();
  }

  async captureWebCameraPhoto() {
    if (!this.webCameraVideo?.nativeElement || !this.webCameraStream) {
      this.webCameraError = 'Camera feed not ready.';
      return;
    }

    try {
      const video = this.webCameraVideo.nativeElement;
      const canvas = document.createElement('canvas');
      canvas.width = video.videoWidth || 1280;
      canvas.height = video.videoHeight || 720;
      const ctx = canvas.getContext('2d');
      if (!ctx) {
        throw new Error('Unable to capture frame');
      }
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      const dataUrl = canvas.toDataURL('image/jpeg', 0.9);
      const base64 = dataUrl.split(',')[1] ?? '';
      await this.processBase64Photo(base64, 'jpeg');
      this.closeWebCameraModal();
    } catch (error) {
      console.error('Failed to capture from web camera:', error);
      this.webCameraError = 'Failed to capture photo. Please try again.';
    }
  }

  closeWebCameraModal() {
    this.webCameraModalOpen = false;
    this.stopWebCameraStream();
  }

  private async startWebCameraStream() {
    try {
      this.stopWebCameraStream();
      if (!navigator.mediaDevices?.getUserMedia) {
        this.webCameraError = 'Camera API not supported in this browser.';
        return;
      }
      const stream = await navigator.mediaDevices.getUserMedia({
        video: {facingMode: {ideal: 'environment'}},
        audio: false
      });
      this.webCameraStream = stream;
      const video = this.webCameraVideo?.nativeElement;
      if (video) {
        video.srcObject = stream;
        await video.play();
      }
    } catch (error) {
      console.error('Unable to start web camera stream:', error);
      this.webCameraError = 'Unable to access camera. Please check permissions.';
    }
  }

  private stopWebCameraStream() {
    if (this.webCameraStream) {
      this.webCameraStream.getTracks().forEach(track => track.stop());
      this.webCameraStream = undefined;
    }
    const video = this.webCameraVideo?.nativeElement;
    if (video) {
      video.pause();
      video.srcObject = null;
    }
  }

  async openLocationSelector() {
    this.mapModalOpen = true;
  }

  async onMapModalPresent() {
    await new Promise(resolve => setTimeout(resolve, 100));
    await this.initializeSelectionMap();
  }

  closeLocationSelector() {
    this.mapModalOpen = false;
    this.destroySelectionMap();
  }

  async confirmLocation() {
    if (!this.selectedCoords) {
      await this.presentToast('Select a location on the map first', 'warning');
      return;
    }

    const label = `Lat: ${this.selectedCoords.lat.toFixed(4)}, Lng: ${this.selectedCoords.lng.toFixed(4)}`;
    this.form.patchValue({
      latitude: this.selectedCoords.lat,
      longitude: this.selectedCoords.lng,
      locationLabel: label
    });
    this.closeLocationSelector();
    await this.presentToast('Location saved', 'success');
  }

  async saveMovie() {
    if (this.form.invalid) {
      await this.presentToast('Fill in the required fields', 'warning');
      return;
    }

    this.loading = true;
    const formValue = this.form.value;

    const premierDate = formValue['premierDate'] ? new Date(formValue['premierDate']) : new Date();
    const payload: Movie = {
      id: this.movieId ?? 0,
      name: formValue['name'],
      rating: Number(formValue['rating']),
      premierDate,
      running: !!formValue['running'],
      owner_id: this.currentMovie?.owner_id ?? 0,
      photoPath: formValue['photoPath'] ?? null,
      photoUrl: formValue['photoUrl'] ?? null,
      latitude: formValue['latitude'] ? Number(formValue['latitude']) : null,
      longitude: formValue['longitude'] ? Number(formValue['longitude']) : null,
      locationLabel: formValue['locationLabel'] ?? null
    };

    const request$ = this.isNew ? this.service.addMovie(payload) : this.service.updateMovie(payload);
    request$.subscribe({
      next: async movie => {
        this.loading = false;
        if (this.isNew && !movie) {
          await this.presentToast('Movie stored locally until you are online', 'warning');
        } else {
          await this.presentToast('Movie saved successfully', 'success');
        }
        this.router.navigate(['main']);
      },
      error: async () => {
        this.loading = false;
        await this.presentToast('Failed to save movie', 'danger');
      }
    });
  }

  private async initializeSelectionMap() {
    try {
      await this.googleMapsLoader.load();
    } catch (error) {
      console.error('Failed to load Google Maps API:', error);
      await this.presentToast('Failed to load Google Maps. Please check your API key configuration.', 'danger');
      return;
    }

    await this.ensureGeolocationPermissions();
    const defaultCoords = await this.getDefaultCoords();
    const hasExistingCoords = !!(this.form.value['latitude'] && this.form.value['longitude']);

    let center: google.maps.LatLngLiteral;
    if (hasExistingCoords) {
      const lat = Number(this.form.value['latitude']);
      const lng = Number(this.form.value['longitude']);
      center = !isNaN(lat) && !isNaN(lng) ? {lat, lng} : {lat: defaultCoords[0], lng: defaultCoords[1]};
    } else {
      center = {lat: defaultCoords[0], lng: defaultCoords[1]};
    }

    this.destroySelectionMap();
    const mapElement = document.getElementById('movie-location-map');
    if (!mapElement) {
      console.error('Map container not found');
      return;
    }

    mapElement.style.width = '100%';
    mapElement.style.height = '360px';
    mapElement.style.minHeight = '360px';
    mapElement.style.display = 'block';

    if (typeof google === 'undefined' || !google.maps || !google.maps.Map) {
      console.error('Google Maps API is not ready');
      await this.presentToast('Google Maps API is not ready. Please try again.', 'danger');
      return;
    }

    this.map = new google.maps.Map(mapElement, {
      center,
      zoom: 13,
      zoomControl: true,
      mapTypeControl: false,
      streetViewControl: false,
      fullscreenControl: true,
      clickableIcons: false
    });

    const mapInstance = this.map!;

    mapInstance.addListener('click', (event: google.maps.MapMouseEvent) => {
      if (event.latLng) {
        this.handleMapClick(event.latLng);
      }
    });

    if (hasExistingCoords) {
      this.marker = new google.maps.Marker({
        position: center,
        map: mapInstance,
        draggable: true
      });

      const markerInstance = this.marker!;
      markerInstance.addListener('dragend', () => {
        const pos = markerInstance.getPosition();
        if (pos) {
          this.selectedCoords = {lat: pos.lat(), lng: pos.lng()};
        }
      });

      this.selectedCoords = {lat: center.lat, lng: center.lng};
    }

    setTimeout(() => {
      if (this.map) {
        google.maps.event.trigger(this.map, 'resize');
        this.map.setCenter(center);
      }
    }, 100);
  }

  private handleMapClick(latLng: google.maps.LatLng) {
    this.selectedCoords = {
      lat: latLng.lat(),
      lng: latLng.lng()
    };

    if (!this.map) {
      return;
    }

    if (this.marker) {
      this.marker.setPosition(latLng);
    } else {
      this.marker = new google.maps.Marker({
        position: latLng,
        map: this.map!,
        draggable: true
      });

      const markerInstance = this.marker!;
      markerInstance.addListener('dragend', () => {
        const pos = markerInstance.getPosition();
        if (pos) {
          this.selectedCoords = {lat: pos.lat(), lng: pos.lng()};
        }
      });
    }
  }

  private destroySelectionMap() {
    if (this.marker) {
      this.marker.setMap(null);
      this.marker = undefined;
    }
    if (this.map) {
      google.maps.event.clearInstanceListeners(this.map);
      this.map = undefined;
    }
    this.selectedCoords = null;
  }

  private async getDefaultCoords(): Promise<[number, number]> {
    try {
      const {coords} = await Geolocation.getCurrentPosition();
      return [coords.latitude, coords.longitude];
    } catch {
      return [44.4268, 26.1025];
    }
  }

  private async ensureGeolocationPermissions() {
    try {
      await Geolocation.requestPermissions();
    } catch {
      // ignore
    }
  }

  private async processBase64Photo(base64String: string, extension: string) {
    if (!base64String) {
      return;
    }

    const base64Data = `data:image/${extension};base64,${base64String}`;
    const savedPath = await this.savePhotoToFilesystem(base64String, extension);
    this.form.patchValue({photoPath: savedPath});
    this.photoPreview = base64Data;
    this.uploadPhoto(base64Data);
    await this.presentToast('Photo ready and saved', 'success');
  }

  private async savePhotoToFilesystem(base64Data: string, extension: string): Promise<string> {
    const fileName = `movie_${Date.now()}.${extension}`;
    const {uri} = await Filesystem.writeFile({
      path: `photos/${fileName}`,
      data: base64Data,
      directory: Directory.Data
    });
    return uri ?? `photos/${fileName}`;
  }

  private uploadPhoto(base64Data: string) {
    this.isUploading = true;
    this.service.uploadPhoto(base64Data).subscribe({
      next: response => {
        this.form.patchValue({
          photoUrl: response.photoUrl,
          photoPath: response.photoPath ?? this.form.get('photoPath')?.value
        });
        this.isUploading = false;
      },
      error: async () => {
        this.isUploading = false;
        await this.presentToast('Failed to upload photo. It is still saved locally.', 'warning');
      }
    });
  }

  private resolveLocalPhoto(path?: string | null): string | null {
    if (!path) {
      return null;
    }
    return Capacitor.convertFileSrc(path);
  }

  private async presentToast(message: string, color: string = 'primary') {
    const toast = await this.toastCtrl.create({
      message,
      duration: 2000,
      color,
      position: 'bottom'
    });
    await toast.present();
  }
}

