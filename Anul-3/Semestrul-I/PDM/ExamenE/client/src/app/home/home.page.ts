import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonList,
  IonItem,
  IonLabel,
  IonBadge,
  IonButton,
  IonButtons,
  IonIcon,
  IonSpinner,
  IonCard,
  IonCardContent,
  IonFab,
  IonFabButton
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBack,
  refresh,
  warning,
  ellipse,
  documentTextOutline
} from 'ionicons/icons';
import { TaskService } from '../services/task.service';
import { LocalTask, TaskStatus } from '../models/task.model';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  imports: [
    CommonModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonList,
    IonItem,
    IonLabel,
    IonBadge,
    IonButton,
    IonButtons,
    IonIcon,
    IonSpinner,
    IonCard,
    IonCardContent,
    IonFab,
    IonFabButton
  ],
})
export class HomePage implements OnInit, OnDestroy {
  currentView: 'tags' | 'tasks' = 'tags';
  selectedTag: string = '';
  selectedTasks: LocalTask[] = [];
  tagGroups: Map<string, { unreadCount: number; tasks: LocalTask[] }> = new Map();

  loading$: Observable<boolean>;
  errorMessage: string = '';
  loading = false;

  private subscriptions: Subscription[] = [];

  constructor(
    private taskService: TaskService,
    private router: Router
  ) {
    addIcons({ arrowBack, refresh, warning, ellipse, documentTextOutline });
    this.loading$ = this.taskService.loading$;
  }

  ngOnInit() {
    const tasksSubscription = this.taskService.tasks$.subscribe(tasks => {
      this.tagGroups = this.taskService.getTagGroups();
      this.updateSelectedTasks();
    });
    this.subscriptions.push(tasksSubscription);

    const loadingSubscription = this.loading$.subscribe(loading => {
      this.loading = loading;
    });
    this.subscriptions.push(loadingSubscription);


    const errorSubscription = this.taskService.error$.subscribe(error => {
      this.errorMessage = error;
      setTimeout(() => this.errorMessage = '', 5000);
    });
    this.subscriptions.push(errorSubscription);


    this.loadTasks();
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadTasks() {
    this.errorMessage = '';
    this.taskService.loadTasks().subscribe({
      error: (error) => {
        console.error('Failed to load tasks:', error);
      }
    });
  }

  retryLoadTasks() {
    this.loadTasks();
  }

  selectTag(tag: string) {
    this.selectedTag = tag;
    this.currentView = 'tasks';
    this.updateSelectedTasks();

    // Mark all tasks of this tag as read
    this.taskService.markTasksAsRead(tag);
  }

  backToTags() {
    this.currentView = 'tags';
    this.selectedTag = '';
    this.selectedTasks = [];
  }

  editTask(task: LocalTask, event?: Event) {
    if (event) {
      event.stopPropagation();
    }

    this.router.navigate(['/task-edit', task.id]);
  }

  retryTask(task: LocalTask, event: Event) {
    event.stopPropagation();
    this.taskService.retryTask(task);
  }

  getStatusText(task: LocalTask): string {
    switch (task.status) {
      case TaskStatus.SENDING:
        return 'Sending...';
      case TaskStatus.NOT_SENT:
        return 'Not sent - Click to retry';
      case TaskStatus.VERSION_CONFLICT:
        return 'Version conflict - Click to resolve';
      default:
        return '';
    }
  }

  getTaskItemClass(task: LocalTask): string {
    const classes = ['task-item'];

    if (!task.read) {
      classes.push('unread');
    }

    switch (task.status) {
      case TaskStatus.SENDING:
        classes.push('sending');
        break;
      case TaskStatus.NOT_SENT:
        classes.push('error');
        break;
      case TaskStatus.VERSION_CONFLICT:
        classes.push('conflict');
        break;
    }

    return classes.join(' ');
  }

  formatTagWithCount(tag: string, unreadCount: number): string {
    if (unreadCount > 0) {
      return `${tag} [${unreadCount}]`;
    }
    return tag;
  }

  handleStatusClick(task: LocalTask, event: Event) {
    event.stopPropagation();

    if (task.status === TaskStatus.NOT_SENT) {
      this.retryTask(task, event);
    } else if (task.status === TaskStatus.VERSION_CONFLICT) {
      this.editTask(task, event);
    }
  }

  private updateSelectedTasks() {
    if (this.currentView === 'tasks' && this.selectedTag) {
      const group = this.tagGroups.get(this.selectedTag);
      this.selectedTasks = group ? group.tasks : [];
    }
  }
}
