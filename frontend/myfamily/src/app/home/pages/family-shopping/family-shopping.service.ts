import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import {
  ShoppingItem,
  ShoppingCategory,
  AddShoppingItemsDto,
  AddShoppingItemDto,
} from 'src/app/shared/interfaces/shopping.interface';

@Injectable({
  providedIn: 'root',
})
export class FamilyShoppingService {
  constructor(
    private apiCallService: ApiCallService,
    private toastService: ToastService
  ) {}

  /**
   * Fetch shopping items for a family (grouped by category)
   * and flatten them into a single array
   */
  getFamilyShoppingItems(familyId: number): Observable<ShoppingItem[]> {
    return this.apiCallService.getFamilyShoppingItemsGrouped(familyId).pipe(
      map((response: any) => {
        // Response is an array of { category, items } objects
        // Flatten all items into a single array
        if (Array.isArray(response)) {
          const allItems: ShoppingItem[] = [];
          response.forEach((group: any) => {
            if (group.items && Array.isArray(group.items)) {
              allItems.push(...group.items);
            }
          });
          return allItems;
        }
        return [];
      })
    );
  }

  /**
   * Fetch all shopping categories
   */
  getShoppingCategories(): Observable<ShoppingCategory[]> {
    return this.apiCallService
      .getShoppingCategories()
      .pipe(map((response: any) => response as ShoppingCategory[]));
  }

  /**
   * Update shopping item status (mark as purchased/unpurchased)
   */
  updateItemStatus(item: ShoppingItem): Observable<any> {
    return this.apiCallService.updateShoppingItemStatus(
      item.id,
      item.isPurchased
    );
  }

  /**
   * Delete a shopping item
   */
  deleteItem(itemId: number): Observable<any> {
    return this.apiCallService.deleteShoppingItem(itemId);
  }

  /**
   * Add new shopping item to a family
   */
  addShoppingItem(
    familyId: number,
    addShoppingItemDto: AddShoppingItemDto
  ): Observable<any> {
    return this.apiCallService
      .addShoppingItem(familyId, addShoppingItemDto)
      .pipe(map((response: any) => response as ShoppingItem[]));
  }
}
