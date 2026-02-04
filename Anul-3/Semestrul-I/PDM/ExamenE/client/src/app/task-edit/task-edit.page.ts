import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButton,
  IonButtons,
  IonIcon,
  IonSpinner,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonItem,
  IonLabel,
  IonTextarea,
  IonNote,
  AlertController,
  ToastController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBack,
  save,
  warning
} from 'ionicons/icons';
import { TaskService } from '../services/task.service';
import { LocalTask, TaskStatus } from '../models/task.model';

@Component({
  selector: 'app-task-edit',
  templateUrl: './task-edit.page.html',
  styleUrls: ['./task-edit.page.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButton,
    IonButtons,
    IonIcon,
    IonSpinner,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonItem,
    IonLabel,
    IonTextarea,
    IonNote
  ],
})
export class TaskEditPage implements OnInit, OnDestroy {
  taskForm: FormGroup;
  task: LocalTask | null = null;
  loading$: Observable<boolean>;
  loading = false;
  isVersionConflict = false;
  serverTask: LocalTask | null = null;

  private subscriptions: Subscription[] = [];
  private taskId: number = 0;

  constructor(
    private taskService: TaskService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private alertController: AlertController,
    private toastController: ToastController
  ) {
    addIcons({ arrowBack, save, warning });
    this.loading$ = this.taskService.loading$;

    this.taskForm = this.formBuilder.group({
      text: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    // Get task ID from route
    this.taskId = parseInt(this.route.snapshot.params['id']);

    // Subscribe to loading state
    const loadingSubscription = this.loading$.subscribe(loading => {
      this.loading = loading;
    });
    this.subscriptions.push(loadingSubscription);

    // Subscribe to tasks to get the current task
    const tasksSubscription = this.taskService.tasks$.subscribe(tasks => {
      this.task = tasks.find(t => t.id === this.taskId) || null;
      if (this.task) {
        this.setupForm();
      }
    });
    this.subscriptions.push(tasksSubscription);

    // Load task if it's a version conflict
    this.loadTaskIfNeeded();
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private setupForm() {
    if (!this.task) return;

    this.isVersionConflict = this.task.status === TaskStatus.VERSION_CONFLICT;

    // Set form value to modified text if it exists, otherwise use current text
    const textValue = this.task.modifiedText || this.task.text;
    this.taskForm.patchValue({
      text: textValue
    });
  }

  private loadTaskIfNeeded() {
    if (!this.task) return;

    if (this.task.status === TaskStatus.VERSION_CONFLICT) {
      // Load the server version of the task
      this.taskService.getTaskById(this.taskId).subscribe({
        next: (serverTask) => {
          this.serverTask = {
            ...serverTask,
            status: TaskStatus.SYNCED,
            read: true
          };
        },
        error: (error) => {
          console.error('Failed to load server task:', error);
          this.showToast('Failed to load server version of the task', 'danger');
        }
      });
    }
  }

  async save() {
    if (!this.task || !this.taskForm.valid) return;

    const formValue = this.taskForm.value;

    if (this.isVersionConflict) {
      // Show confirmation dialog for version conflict resolution
      const alert = await this.alertController.create({
        header: 'Version Conflict Resolution',
        message: 'Choose how to resolve the version conflict:',
        buttons: [
          {
            text: 'Cancel',
            role: 'cancel'
          },
          {
            text: 'Use Server Version',
            handler: () => {
              this.useServerVersion();
            }
          },
          {
            text: 'Keep My Changes',
            handler: () => {
              this.overwriteWithLocal();
            }
          }
        ]
      });
      await alert.present();
    } else {
      this.saveTask(formValue.text);
    }
  }

  useServerVersion() {
    if (!this.serverTask) return;

    this.taskForm.patchValue({
      text: this.serverTask.text
    });

    this.saveTaskWithVersion(this.serverTask.text, this.serverTask.version);
    this.showToast('Using server version', 'success');
  }

  overwriteWithLocal() {
    const formValue = this.taskForm.value;

    if (this.isVersionConflict && this.serverTask) {
      this.saveTaskWithVersion(formValue.text, this.serverTask.version);
    } else {
      this.saveTask(formValue.text);
    }

    this.showToast('Overwriting with your changes', 'success');
  }

  getStatusText(task: LocalTask): string {
    switch (task.status) {
      case TaskStatus.SENDING:
        return 'Sending...';
      case TaskStatus.NOT_SENT:
        return 'Not sent';
      case TaskStatus.VERSION_CONFLICT:
        return 'Version conflict';
      case TaskStatus.SYNCED:
        return 'Synced';
      default:
        return '';
    }
  }

  private saveTask(text: string) {
    if (!this.task) return;

    const updatedTask: LocalTask = {
      ...this.task,
      text: text
    };

    this.taskService.updateTask(updatedTask);
    this.showToast('Task saved', 'success');
    this.goBack();
  }

  private saveTaskWithVersion(text: string, version: number) {
    if (!this.task) return;

    const updatedTask: LocalTask = {
      ...this.task,
      text: text,
      version: version
    };

    this.taskService.updateTask(updatedTask);
    this.showToast('Task saved', 'success');
    this.goBack();
  }


  goBack() {
    this.router.navigate(['/home']);
  }

  private async showToast(message: string, color: 'success' | 'danger' | 'warning' = 'success') {
    const toast = await this.toastController.create({
      message,
      duration: 3000,
      color,
      position: 'bottom'
    });
    await toast.present();
  }
}
