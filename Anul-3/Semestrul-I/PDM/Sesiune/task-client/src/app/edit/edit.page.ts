import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../services/task.service';
import { Task } from '../models/task.model';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.page.html',
  styleUrls: ['./edit.page.scss'],
  standalone: false,
})
export class EditPage implements OnInit {
  task: Task | null = null;
  editedText: string = '';
  isLoading = false;
  hasConflict = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService,
    private alertController: AlertController
  ) { }

  ngOnInit() {
    const taskId = parseInt(this.route.snapshot.paramMap.get('id') || '0');
    this.loadTask(taskId);
  }

  loadTask(taskId: number) {
    this.isLoading = true;

    // First try to get from local cache
    this.taskService.tasks$.subscribe(tasks => {
      const cachedTask = tasks.find(t => t.id === taskId);
      if (cachedTask) {
        this.task = { ...cachedTask };
        this.editedText = cachedTask.localText || cachedTask.text;
        this.hasConflict = cachedTask.sendStatus === 'conflict';

        // If there's a conflict and we don't have server text, fetch it
        if (this.hasConflict && !cachedTask.serverText) {
          this.fetchTaskFromServer(taskId);
        } else {
          this.isLoading = false;
        }
      } else {
        this.fetchTaskFromServer(taskId);
      }
    }).unsubscribe(); // Unsubscribe immediately after first value
  }

  fetchTaskFromServer(taskId: number) {
    this.isLoading = true;
    this.taskService.getTaskById(taskId).subscribe({
      next: (task) => {
        this.task = { ...task };
        this.editedText = task.localText || task.text;
        this.hasConflict = task.sendStatus === 'conflict';
        if (this.hasConflict && task.serverText) {
          this.task.serverText = task.serverText;
        }
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.showErrorAlert('Failed to load task. Please try again.');
      }
    });
  }

  async showErrorAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Error',
      message: message,
      buttons: [
        {
          text: 'Retry',
          handler: () => {
            if (this.task) {
              this.loadTask(this.task.id);
            }
          }
        },
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            this.router.navigate(['/home']);
          }
        }
      ]
    });
    await alert.present();
  }

  async onSave() {
    if (!this.task) return;

    const updatedTask: Task = {
      ...this.task,
      text: this.editedText,
      localText: this.editedText
    };

    // If resolving conflict, update version from server
    if (this.hasConflict) {
      // Fetch latest version from server first
      this.isLoading = true;
      this.taskService.getTaskById(this.task.id).subscribe({
        next: async (serverTask) => {
          this.isLoading = false;
          updatedTask.version = serverTask.version;
          updatedTask.sendStatus = undefined;
          delete updatedTask.localText;
          delete updatedTask.serverText;
          this.taskService.clearPendingUpdate(this.task!.id);
          await this.taskService.updateTask(updatedTask);
          this.router.navigate(['/home']);
        },
        error: (error) => {
          this.isLoading = false;
          this.showErrorAlert('Failed to fetch latest task version.');
        }
      });
    } else {
      await this.taskService.updateTask(updatedTask);
      this.router.navigate(['/home']);
    }
  }

  onCancel() {
    this.router.navigate(['/home']);
  }

  useServerVersion() {
    if (this.task && this.task.serverText) {
      this.editedText = this.task.serverText;
    }
  }

  useLocalVersion() {
    if (this.task && this.task.localText) {
      this.editedText = this.task.localText;
    }
  }
}


