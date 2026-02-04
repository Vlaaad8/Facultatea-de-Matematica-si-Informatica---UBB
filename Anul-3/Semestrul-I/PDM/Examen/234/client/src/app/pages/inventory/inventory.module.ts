import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

import { InventoryPageRoutingModule } from './inventory-routing.module';
import { InventoryPage } from './inventory.page';
import { ProductSearchComponent } from '../../components/product-search/product-search.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    InventoryPageRoutingModule
  ],
  declarations: [
    InventoryPage,
    ProductSearchComponent
  ]
})
export class InventoryPageModule {}
