export interface MenuItem {
  code: number;
  name: string;
  price: number;
  quantity?: number;
  isEditing?: boolean;
  isSubmitting?: boolean;
  hasError?: boolean;
}
