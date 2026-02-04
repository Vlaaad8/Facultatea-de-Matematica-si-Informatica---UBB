import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, finalize, map } from 'rxjs/operators';
import { AuditRequest, AuditResponse, InventoryItem } from '../models/inventory.model';
import { InventoryService } from './inventory.service';
import { ZoneService } from './zone.service';
import { ToastController } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class AuditService {
  private readonly apiUrl = 'http://localhost:3000';

  constructor(
    private http: HttpClient,
    private inventoryService: InventoryService,
    private zoneService: ZoneService,
    private toastController: ToastController
  ) { }

  async submitAudit(items: InventoryItem[]): Promise<void> {
    console.log('🚀 Starting audit submission with items:', items);

    const zone = this.zoneService.getZone();
    if (!zone) {
      await this.showToast('Zone not set', 'danger');
      return;
    }

    // Per requirement 8: Only submit items that:
    // - Have counted values
    // - Haven't been successfully submitted before OR have errors (need retry)
    // - Are not currently loading
    const auditItems = items.filter(item =>
      item.counted !== undefined &&
      item.counted !== null &&
      !item.isLoading &&
      (!item.hasBeenSubmitted || item.hasError)
    );

    console.log('📋 Filtered audit items:', auditItems);

    if (auditItems.length === 0) {
      await this.showToast('No items to audit or all items already submitted successfully', 'warning');
      return;
    }

    console.log(`📤 Submitting ${auditItems.length} items for audit (new + retries)`);

    // Per requirement 9: Set loading state for progress indicators
    auditItems.forEach(item => {
      this.inventoryService.setItemLoading(item.code, true);
      this.inventoryService.setItemError(item.code, false); // Clear previous errors
    });

    // Per requirement 7: Create parallel POST requests
    const auditRequests = auditItems.map(item => {
      const auditData: AuditRequest = {
        code: item.code,
        counted: item.counted!,
        zone
      };

      console.log(`📝 Creating request for item ${item.code}:`, auditData);
      return this.submitSingleAudit(auditData, item.code);
    });

    // Execute all requests in parallel with forkJoin
    forkJoin(auditRequests).subscribe({
      next: (results) => {
        console.log('📊 All audit results:', results);
        const successCount = results.filter(r => r.success).length;
        const errorCount = results.length - successCount;

        // Mark successful items as submitted (requirement 8)
        results.forEach((result, index) => {
          if (result.success) {
            this.inventoryService.setItemSubmitted(auditItems[index].code, true);
          }
        });

        // Per requirement 9: Global notifications for results
        if (successCount > 0) {
          this.showToast(`✅ ${successCount} items audited successfully`, 'success');
        }
        if (errorCount > 0) {
          this.showToast(`❌ ${errorCount} items failed. Check red items and retry.`, 'warning');
        }
      },
      error: (error) => {
        // Per requirement 9: Global notifications for IO errors
        console.error('💥 Network error during audit submission:', error);
        this.showToast('🔌 Network error - server unavailable. Please check connection.', 'danger');

        // Clear loading state for all items
        auditItems.forEach(item => {
          this.inventoryService.setItemLoading(item.code, false);
        });
      }
    });
  }

  private submitSingleAudit(auditData: AuditRequest, itemCode: number): Observable<{success: boolean, data?: AuditResponse, error?: string}> {
    console.log(`📤 Starting audit submission for item ${itemCode}:`, auditData);

    return this.http.post<AuditResponse>(`${this.apiUrl}/audit`, auditData).pipe(
      // Map successful response
      map((response: AuditResponse) => {
        console.log(`✅ Audit successful for item ${itemCode}:`, response);
        this.inventoryService.setItemError(itemCode, false);
        return { success: true, data: response };
      }),
      // Handle errors per requirement 8 (red font for errors)
      catchError(error => {
        console.error(`❌ Audit failed for item ${itemCode}:`, error);

        let errorMessage = 'Unknown error';

        // Handle specific server responses
        if (error.error?.text) {
          errorMessage = error.error.text;
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        } else if (error.status === 400) {
          // Per requirement 8: negative quantities and validation errors
          errorMessage = 'Invalid data (negative quantity not allowed)';
        } else if (error.status === 404) {
          errorMessage = 'Product not found on server';
        } else if (error.status === 0 || error.status === 500) {
          // Per requirement 9: IO errors (connectivity, server unavailable)
          errorMessage = 'Server unavailable - check connection';
        } else if (error.status >= 500) {
          errorMessage = 'Server error occurred';
        }

        // Set red font error per requirement 8
        this.inventoryService.setItemError(itemCode, true, errorMessage);
        return of({ success: false, error: errorMessage });
      }),
      // Per requirement 9: Clear loading indicator when done
      finalize(() => {
        this.inventoryService.setItemLoading(itemCode, false);
      })
    );
  }

  private async showToast(message: string, color: 'success' | 'danger' | 'warning' = 'success') {
    const toast = await this.toastController.create({
      message,
      duration: 3000,
      color,
      position: 'top'
    });
    await toast.present();
  }
}
