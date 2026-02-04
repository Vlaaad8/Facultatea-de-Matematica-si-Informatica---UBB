import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Task, TagInfo } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:3000';
  private tasksSubject = new BehaviorSubject<Task[]>([]);
  public tasks$ = this.tasksSubject.asObservable();

  private readTaskIds = new Set<number>();
  private pendingUpdates = new Map<number, Task>();

  constructor(private http: HttpClient) {
    this.loadReadTaskIds();
    this.loadPendingUpdates();
  }

  private loadReadTaskIds(): void {
    const stored = localStorage.getItem('readTaskIds');
    if (stored) {
      this.readTaskIds = new Set(JSON.parse(stored));
    }
  }

  private saveReadTaskIds(): void {
    localStorage.setItem('readTaskIds', JSON.stringify(Array.from(this.readTaskIds)));
  }

  private loadPendingUpdates(): void {
    const stored = localStorage.getItem('pendingUpdates');
    if (stored) {
      const updates = JSON.parse(stored);
      this.pendingUpdates = new Map(Object.entries(updates).map(([key, val]) => [parseInt(key), val as Task]));
    }
  }

  private savePendingUpdates(): void {
    const obj: any = {};
    this.pendingUpdates.forEach((value, key) => {
      obj[key] = value;
    });
    localStorage.setItem('pendingUpdates', JSON.stringify(obj));
  }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/task`).pipe(
      tap(tasks => {
        // Mark tasks as read/unread based on local storage
        tasks.forEach(task => {
          task.isRead = this.readTaskIds.has(task.id);
          // Check if there's a pending update
          if (this.pendingUpdates.has(task.id)) {
            const pending = this.pendingUpdates.get(task.id)!;
            task.sendStatus = pending.sendStatus;
            task.localText = pending.localText;
            task.serverText = task.text;
          }
        });
        this.tasksSubject.next(tasks);
      }),
      catchError(this.handleError)
    );
  }

  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/task/${id}`).pipe(
      tap(task => {
        task.isRead = this.readTaskIds.has(task.id);
        if (this.pendingUpdates.has(task.id)) {
          const pending = this.pendingUpdates.get(task.id)!;
          task.sendStatus = pending.sendStatus;
          task.localText = pending.localText;
          task.serverText = task.text;
        }
      }),
      catchError(this.handleError)
    );
  }

  updateTask(task: Task): Promise<void> {
    return new Promise((resolve) => {
      const taskToUpdate = { ...task };
      taskToUpdate.sendStatus = 'sending';
      this.updateTaskInList(taskToUpdate);

      // Save as pending
      this.pendingUpdates.set(task.id, {
        ...task,
        sendStatus: 'sending',
        localText: task.text
      });
      this.savePendingUpdates();

      // Return immediately
      resolve();

      // Send in background
      this.http.put<Task>(`${this.apiUrl}/task/${task.id}`, {
        id: task.id,
        tag: task.tag,
        text: task.text,
        version: task.version
      }).subscribe({
        next: (updatedTask) => {
          taskToUpdate.sendStatus = 'sent';
          taskToUpdate.version = updatedTask.version;
          taskToUpdate.text = updatedTask.text;
          delete taskToUpdate.localText;
          delete taskToUpdate.serverText;
          this.updateTaskInList(taskToUpdate);
          this.pendingUpdates.delete(task.id);
          this.savePendingUpdates();
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 409) {
            taskToUpdate.sendStatus = 'conflict';
            taskToUpdate.serverText = ''; // Will be fetched when user clicks
          } else {
            taskToUpdate.sendStatus = 'not-sent';
          }
          this.updateTaskInList(taskToUpdate);
          this.pendingUpdates.set(task.id, taskToUpdate);
          this.savePendingUpdates();
        }
      });
    });
  }

  private updateTaskInList(updatedTask: Task): void {
    const tasks = this.tasksSubject.value;
    const index = tasks.findIndex(t => t.id === updatedTask.id);
    if (index !== -1) {
      tasks[index] = updatedTask;
      this.tasksSubject.next([...tasks]);
    }
  }

  markTasksAsRead(taskIds: number[]): void {
    taskIds.forEach(id => this.readTaskIds.add(id));
    this.saveReadTaskIds();

    const tasks = this.tasksSubject.value;
    tasks.forEach(task => {
      if (taskIds.includes(task.id)) {
        task.isRead = true;
      }
    });
    this.tasksSubject.next([...tasks]);
  }

  markTaskAsUnread(taskId: number): void {
    this.readTaskIds.delete(taskId);
    this.saveReadTaskIds();

    const tasks = this.tasksSubject.value;
    const task = tasks.find(t => t.id === taskId);
    if (task) {
      task.isRead = false;
      this.tasksSubject.next([...tasks]);
    }
  }

  updateTaskFromWebSocket(updatedTask: Task): void {
    const tasks = this.tasksSubject.value;
    const index = tasks.findIndex(t => t.id === updatedTask.id);

    if (index !== -1) {
      // Mark as unread
      this.readTaskIds.delete(updatedTask.id);
      this.saveReadTaskIds();

      // Update task
      tasks[index] = {
        ...tasks[index],
        ...updatedTask,
        isRead: false
      };

      // Keep pending status if exists
      if (this.pendingUpdates.has(updatedTask.id)) {
        const pending = this.pendingUpdates.get(updatedTask.id)!;
        tasks[index].sendStatus = pending.sendStatus;
        tasks[index].localText = pending.localText;
        tasks[index].serverText = updatedTask.text;
      }

      this.tasksSubject.next([...tasks]);
    }
  }

  getTagsWithUnreadCount(): TagInfo[] {
    const tasks = this.tasksSubject.value;
    const tagMap = new Map<string, TagInfo>();

    tasks.forEach(task => {
      if (!tagMap.has(task.tag)) {
        tagMap.set(task.tag, { tag: task.tag, unreadCount: 0 });
      }
      if (!task.isRead) {
        tagMap.get(task.tag)!.unreadCount++;
      }
    });

    return Array.from(tagMap.values()).sort((a, b) => a.tag.localeCompare(b.tag));
  }

  getTasksByTag(tag: string): Task[] {
    return this.tasksSubject.value.filter(t => t.tag === tag);
  }

  clearPendingUpdate(taskId: number): void {
    this.pendingUpdates.delete(taskId);
    this.savePendingUpdates();
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(() => error);
  }
}
