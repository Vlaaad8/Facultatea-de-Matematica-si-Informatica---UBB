export interface Movie{
    id: number,
    name: string,
    premierDate: Date,
    rating: number,
    running: boolean,
    owner_id: number,
    photoPath?: string | null,
    photoUrl?: string | null,
    latitude?: number | null,
    longitude?: number | null,
    locationLabel?: string | null
}