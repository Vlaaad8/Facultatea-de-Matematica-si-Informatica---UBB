import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { TaskService } from '../services/task.service';
import { WebsocketService } from '../services/websocket.service';
import { Task, TagInfo } from '../models/task.model';
import { Subscription } from 'rxjs';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage implements OnInit, OnDestroy {
  viewMode: 'tags' | 'tasks' = 'tags';
  tags: TagInfo[] = [];
  tasks: Task[] = [];
  selectedTag: string = '';
  isLoading = false;
  errorMessage = '';

  private subscriptions: Subscription[] = [];

  constructor(
    private taskService: TaskService,
    private websocketService: WebsocketService,
    private router: Router,
    private alertController: AlertController
  ) {}

  ngOnInit() {
    this.loadTasks();

    // Subscribe to task updates from service
    const tasksSub = this.taskService.tasks$.subscribe(tasks => {
      if (this.viewMode === 'tags') {
        this.tags = this.taskService.getTagsWithUnreadCount();
      } else if (this.selectedTag) {
        this.tasks = this.taskService.getTasksByTag(this.selectedTag);
      }
    });
    this.subscriptions.push(tasksSub);

    // Subscribe to WebSocket updates
    const wsSub = this.websocketService.taskUpdates$.subscribe(updatedTask => {
      this.taskService.updateTaskFromWebSocket(updatedTask);
    });
    this.subscriptions.push(wsSub);
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadTasks() {
    this.isLoading = true;
    this.errorMessage = '';

    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.isLoading = false;
        this.tags = this.taskService.getTagsWithUnreadCount();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load tasks. Please try again.';
        this.showErrorAlert();
      }
    });
  }

  async showErrorAlert() {
    const alert = await this.alertController.create({
      header: 'Error',
      message: this.errorMessage,
      buttons: [
        {
          text: 'Retry',
          handler: () => {
            this.loadTasks();
          }
        }
      ]
    });
    await alert.present();
  }

  onTagClick(tag: string) {
    this.selectedTag = tag;
    this.viewMode = 'tasks';
    this.tasks = this.taskService.getTasksByTag(tag);

    // Mark all tasks with this tag as read
    const taskIds = this.tasks.map(t => t.id);
    this.taskService.markTasksAsRead(taskIds);
  }

  onReturn() {
    this.viewMode = 'tags';
    this.selectedTag = '';
    this.tags = this.taskService.getTagsWithUnreadCount();
  }

  onTaskClick(task: Task) {
    if (task.sendStatus === 'not-sent') {
      // Retry sending
      this.taskService.updateTask(task);
    } else if (task.sendStatus === 'conflict') {
      // Navigate to edit with conflict resolution
      this.router.navigate(['/edit', task.id]);
    } else {
      // Normal edit
      this.router.navigate(['/edit', task.id]);
    }
  }

  getTaskDisplayText(task: Task): string {
    if (task.sendStatus === 'sending') {
      return 'Sending...';
    } else if (task.sendStatus === 'conflict') {
      return 'Version conflict';
    } else if (task.sendStatus === 'not-sent') {
      return 'Not sent';
    }
    return task.text;
  }

  getTaskColor(task: Task): string {
    if (task.sendStatus === 'sending') {
      return 'warning';
    } else if (task.sendStatus === 'conflict') {
      return 'danger';
    } else if (task.sendStatus === 'not-sent') {
      return 'danger';
    } else if (!task.isRead) {
      return 'primary';
    }
    return '';
  }
}


