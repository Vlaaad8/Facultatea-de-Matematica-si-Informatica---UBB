export interface InventoryItem {
  code: number;
  name: string;
  quantity: number;
  counted?: number;
  isEditing?: boolean;
  isSending?: boolean;
  hasError?: boolean;
  errorMessage?: string;
  sentToServer?: boolean;
}
