export interface Product {
  code: number;
  name: string;
}

export interface ProductResponse {
  total: number;
  page: number;
  products: Product[];
}

export interface Item {
  id?: number;
  code: number;
  quantity: number;
  status?: 'pending' | 'submitting' | 'submitted' | 'failed';
}

export interface DownloadState {
  isDownloading: boolean;
  currentPage: number;
  totalPages: number;
  hasError: boolean;
  errorPage?: number;
  isComplete: boolean;
}

export interface WebSocketMessage {
  event: string;
}
