import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, Subject, throwError, timer } from 'rxjs';
import { catchError, retry, tap, map } from 'rxjs/operators';
import { Task, LocalTask, TaskStatus } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly API_URL = 'http://localhost:3000';
  private readonly WS_URL = 'ws://localhost:3000';

  private tasksSubject = new BehaviorSubject<LocalTask[]>([]);
  public tasks$ = this.tasksSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  private errorSubject = new Subject<string>();
  public error$ = this.errorSubject.asObservable();

  private ws: WebSocket | null = null;
  private reconnectInterval = 5000;

  constructor(private http: HttpClient) {
    this.initWebSocket();
  }

  private initWebSocket(): void {
    try {
      this.ws = new WebSocket(this.WS_URL);

      this.ws.onmessage = (event) => {
        try {
          const updatedTask: Task = JSON.parse(event.data);
          this.handleTaskUpdate(updatedTask);
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.ws.onclose = () => {
        console.log('WebSocket closed, reconnecting...');
        setTimeout(() => this.initWebSocket(), this.reconnectInterval);
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.ws?.close();
      };
    } catch (error) {
      console.error('Error creating WebSocket:', error);
      setTimeout(() => this.initWebSocket(), this.reconnectInterval);
    }
  }

  private handleTaskUpdate(updatedTask: Task): void {
    const currentTasks = this.tasksSubject.value;
    const index = currentTasks.findIndex(t => t.id === updatedTask.id);

    if (index !== -1) {
      const localTask = currentTasks[index];


      if (localTask.status === TaskStatus.SENDING) {
        return;
      }


      currentTasks[index] = {
        ...updatedTask,
        status: TaskStatus.SYNCED,
        read: false
      };

      this.tasksSubject.next([...currentTasks]);
      this.saveToLocalStorage(currentTasks);
    }
  }

  loadTasks(): Observable<Task[]> {
    this.loadingSubject.next(true);

    return this.http.get<Task[]>(`${this.API_URL}/task`).pipe(
      retry(3),
      tap(tasks => {

        const existingTasks = this.loadFromLocalStorage();
        const existingTasksMap = new Map(existingTasks.map(t => [t.id, t]));


        const localTasks: LocalTask[] = tasks.map(task => {
          const existing = existingTasksMap.get(task.id);


          if (existing && existing.status !== TaskStatus.SYNCED) {
            return {
              ...task,
              status: existing.status,
              read: existing.read,
              modifiedText: existing.modifiedText,
              serverText: existing.serverText
            };
          }


          return {
            ...task,
            status: TaskStatus.SYNCED,
            read: existing?.read || false
          };
        });

        this.tasksSubject.next(localTasks);
        this.saveToLocalStorage(localTasks);
        this.loadingSubject.next(false);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next('Failed to load tasks from server');

        const cachedTasks = this.loadFromLocalStorage();
        if (cachedTasks.length > 0) {
          this.tasksSubject.next(cachedTasks);
        }

        return throwError(() => error);
      })
    );
  }

  getTaskById(id: number): Observable<Task> {
    this.loadingSubject.next(true);

    return this.http.get<Task>(`${this.API_URL}/task/${id}`).pipe(
      retry(3),
      tap(() => this.loadingSubject.next(false)),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(`Failed to load task ${id}`);
        return throwError(() => error);
      })
    );
  }

  updateTask(task: LocalTask): void {
    const currentTasks = this.tasksSubject.value;
    const index = currentTasks.findIndex(t => t.id === task.id);

    if (index !== -1) {
      // Mark as sending
      currentTasks[index] = {
        ...task,
        status: TaskStatus.SENDING,
        modifiedText: task.text
      };

      this.tasksSubject.next([...currentTasks]);
      this.saveToLocalStorage(currentTasks);

      // Send to server in background
      this.sendTaskToServer(task);
    }
  }

  private sendTaskToServer(task: LocalTask): void {
    this.http.put<Task>(`${this.API_URL}/task/${task.id}`, {
      id: task.id,
      tag: task.tag,
      text: task.text,
      version: task.version
    }).subscribe({
      next: (updatedTask) => {
        // Update successful
        const currentTasks = this.tasksSubject.value;
        const index = currentTasks.findIndex(t => t.id === task.id);

        if (index !== -1) {
          currentTasks[index] = {
            ...updatedTask,
            status: TaskStatus.SYNCED,
            read: currentTasks[index].read,
            modifiedText: undefined,
            serverText: undefined
          };

          this.tasksSubject.next([...currentTasks]);
          this.saveToLocalStorage(currentTasks);
        }
      },
      error: (error: HttpErrorResponse) => {
        const currentTasks = this.tasksSubject.value;
        const index = currentTasks.findIndex(t => t.id === task.id);

        if (index !== -1) {
          if (error.status === 409) {
            // Version conflict - store server text for comparison
            this.getTaskById(task.id).subscribe({
              next: (serverTask) => {
                const updatedTasks = this.tasksSubject.value;
                const updatedIndex = updatedTasks.findIndex(t => t.id === task.id);
                if (updatedIndex !== -1) {
                  updatedTasks[updatedIndex] = {
                    ...updatedTasks[updatedIndex],
                    status: TaskStatus.VERSION_CONFLICT,
                    serverText: serverTask.text,
                    version: serverTask.version // Update to server version for conflict resolution
                  };
                  this.tasksSubject.next([...updatedTasks]);
                  this.saveToLocalStorage(updatedTasks);
                }
              },
              error: () => {
                // If we can't load server version, still mark as conflict
                currentTasks[index].status = TaskStatus.VERSION_CONFLICT;
                this.tasksSubject.next([...currentTasks]);
                this.saveToLocalStorage(currentTasks);
              }
            });
          } else {
            // Network error or other error
            currentTasks[index].status = TaskStatus.NOT_SENT;
            this.tasksSubject.next([...currentTasks]);
            this.saveToLocalStorage(currentTasks);
          }
        }
      }
    });
  }

  retryTask(task: LocalTask): void {
    const currentTasks = this.tasksSubject.value;
    const index = currentTasks.findIndex(t => t.id === task.id);

    if (index !== -1) {
      currentTasks[index].status = TaskStatus.SENDING;
      this.tasksSubject.next([...currentTasks]);
      this.saveToLocalStorage(currentTasks);

      // Use modified text if it exists
      const taskToSend = {
        ...task,
        text: task.modifiedText || task.text
      };

      this.sendTaskToServer(taskToSend);
    }
  }

  markTasksAsRead(tag: string): void {
    const currentTasks = this.tasksSubject.value;
    const updatedTasks = currentTasks.map(task => {
      if (task.tag === tag) {
        return { ...task, read: true };
      }
      return task;
    });

    this.tasksSubject.next(updatedTasks);
    this.saveToLocalStorage(updatedTasks);
  }

  getTagGroups(): Map<string, { unreadCount: number; tasks: LocalTask[] }> {
    const tasks = this.tasksSubject.value;
    const tagGroups = new Map<string, { unreadCount: number; tasks: LocalTask[] }>();

    tasks.forEach(task => {
      if (!tagGroups.has(task.tag)) {
        tagGroups.set(task.tag, { unreadCount: 0, tasks: [] });
      }

      const group = tagGroups.get(task.tag)!;
      group.tasks.push(task);

      if (!task.read) {
        group.unreadCount++;
      }
    });

    return tagGroups;
  }

  private saveToLocalStorage(tasks: LocalTask[]): void {
    try {
      localStorage.setItem('tasks', JSON.stringify(tasks));
    } catch (error) {
      console.error('Error saving to localStorage:', error);
    }
  }

  private loadFromLocalStorage(): LocalTask[] {
    try {
      const data = localStorage.getItem('tasks');
      return data ? JSON.parse(data) : [];
    } catch (error) {
      console.error('Error loading from localStorage:', error);
      return [];
    }
  }

  ngOnDestroy(): void {
    this.ws?.close();
  }
}
