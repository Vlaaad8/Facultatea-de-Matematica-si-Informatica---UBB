export interface AuditRequest {
  code: number;
  counted: number;
  zone: string;
}

export interface AuditResponse {
  id: number;
  code: number;
  counted: number;
  zone: string;
  timestamp: Date;
}
