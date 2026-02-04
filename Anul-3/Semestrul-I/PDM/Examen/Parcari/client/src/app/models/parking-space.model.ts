export interface ParkingSpace {
  id: number;
  number: string;
  takenBy: string;
  status?: 'taken' | 'free';
}

export interface ParkingSpaceUpdate {
  id: number;
  number: string;
  takenBy: string;
  status: 'taken' | 'free';
}
