import { TestBed } from '@angular/core/testing';

import { ParkingApi } from './parking-api';

describe('ParkingApi', () => {
  let service: ParkingApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParkingApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
