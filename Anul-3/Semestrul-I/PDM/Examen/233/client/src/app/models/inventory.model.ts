export interface InventoryItem {
  code: number;
  name: string;
  quantity: number;
  counted?: number;
  isEditing?: boolean;
  hasError?: boolean;
  isLoading?: boolean;
  errorMessage?: string;
  hasBeenSubmitted?: boolean;
}

export interface AuditRequest {
  code: number;
  counted: number;
  zone: string;
}

export interface AuditResponse {
  id?: number;
  code: number;
  counted: number;
  zone: string;
  timestamp?: Date;
  text?: string;
}

export enum FilterType {
  ALL = 'all',
  DISCREPANCIES = 'discrepancies'
}
