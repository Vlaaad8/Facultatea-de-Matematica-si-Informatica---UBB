import { Routes } from '@angular/router';
import { ZoneSetupPage } from './pages/zone-setup/zone-setup.page';
import { InventoryPage } from './pages/inventory/inventory.page';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/zone-setup',
    pathMatch: 'full',
  },
  {
    path: 'zone-setup',
    component: ZoneSetupPage,
  },
  {
    path: 'inventory',
    component: InventoryPage,
  },
];
