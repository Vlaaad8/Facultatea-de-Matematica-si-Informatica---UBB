export interface Task {
  id: number;
  tag: string;
  text: string;
  version: number;
}

export interface TagGroup {
  tag: string;
  unreadCount: number;
  tasks: Task[];
}

export enum TaskStatus {
  SYNCED = 'synced',
  SENDING = 'sending',
  NOT_SENT = 'not_sent',
  VERSION_CONFLICT = 'version_conflict'
}

export interface LocalTask extends Task {
  status: TaskStatus;
  read: boolean;
  modifiedText?: string;
  serverText?: string;
}
