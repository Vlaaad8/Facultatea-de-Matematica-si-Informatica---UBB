export interface Task {
  id: number;
  tag: string;
  text: string;
  version: number;
  // Client-side properties
  isRead?: boolean;
  sendStatus?: 'sending' | 'sent' | 'conflict' | 'not-sent';
  localText?: string; // For conflict resolution
  serverText?: string; // For conflict resolution
}

export interface TagInfo {
  tag: string;
  unreadCount: number;
}
