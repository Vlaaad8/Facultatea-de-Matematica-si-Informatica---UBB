import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ParkingListPage } from './parking-list.page';

describe('ParkingListPage', () => {
  let component: ParkingListPage;
  let fixture: ComponentFixture<ParkingListPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(ParkingListPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
