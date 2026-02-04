export interface Asset {
  id: number;
  name: string;
  takenBy: string | null;
  desiredBy: string[];
}

export type AssetStatus = 'red' | 'green' | 'yellow' | 'white';
