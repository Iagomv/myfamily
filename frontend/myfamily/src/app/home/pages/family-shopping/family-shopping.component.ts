import {
  Component,
  OnInit,
  CUSTOM_ELEMENTS_SCHEMA,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule, ModalController } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { ShoppingItemCardComponent } from './shopping-item-card/shopping-item-card.component';
import { FloatingButtonComponent } from '../../../shared/components/floating-button/floating-button.component';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Family } from 'src/app/shared/interfaces/family.interface';
import { FamilyService } from 'src/app/shared/services/family.service';
import {
  AddShoppingItemsDto,
  AddShoppingItemDto,
  ShoppingItem,
  ShoppingCategory,
} from 'src/app/shared/interfaces/shopping.interface';
import { FamilyShoppingService } from './family-shopping.service';
import { AddItemModalComponent } from './add-item-modal/add-item-modal.component';

@Component({
  selector: 'app-family-shopping',
  templateUrl: './family-shopping.component.html',
  styleUrls: ['./family-shopping.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    HeaderComponent,
    ShoppingItemCardComponent,
    FloatingButtonComponent,
    AddItemModalComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyShoppingComponent implements OnInit {
  family: Family | null = null;
  shoppingCategories: ShoppingCategory[] = [];
  private newShoppingItems: AddShoppingItemsDto = {
    items: [],
  };
  shoppingItems: ShoppingItem[] = [];
  localShoppingItems: ShoppingItem[] = [];
  selectedCategory: ShoppingCategory | null = null;
  isSaving = false;

  constructor(
    private shoppingService: FamilyShoppingService,
    private toastService: ToastService,
    private familyService: FamilyService,
    private cdr: ChangeDetectorRef,
    private modalController: ModalController
  ) {}

  ngOnInit() {
    const selectedFamily = this.familyService.getSelectedFamily();
    if (!selectedFamily || !selectedFamily.id) {
      return;
    }

    this.family = selectedFamily;
    this.loadShoppingData();
    this.loadCategories();
  }

  private loadShoppingData() {
    if (!this.family || !this.family.id) {
      return;
    }

    this.shoppingService.getFamilyShoppingItems(this.family.id).subscribe({
      next: (items: ShoppingItem[]) => {
        this.shoppingItems = items;
        this.localShoppingItems = items.map((item) => ({ ...item }));
      },
      error: async (error: any) => {
        await this.toastService.showError('Error fetching shopping items', 400);
      },
    });
  }

  private loadCategories() {
    this.shoppingService.getShoppingCategories().subscribe({
      next: (categories: ShoppingCategory[]) => {
        this.shoppingCategories = categories;
      },
      error: async (error: any) => {
        await this.toastService.showError('Error fetching categories', 400);
      },
    });
  }

  getPendingItems(): ShoppingItem[] {
    return this.localShoppingItems
      .filter((item) => !item.isPurchased)
      .filter((item) =>
        this.selectedCategory
          ? item.categoryId === this.selectedCategory.id
          : true
      );
  }

  getBoughtItems(): ShoppingItem[] {
    return this.localShoppingItems
      .filter((item) => item.isPurchased)
      .filter((item) =>
        this.selectedCategory
          ? item.categoryId === this.selectedCategory.id
          : true
      );
  }

  getPendingItemsCount(): number {
    return this.getPendingItems().length;
  }

  getBoughtItemsCount(): number {
    return this.getBoughtItems().length;
  }

  onCategoryChange(event: any): void {
    const categoryId = event.detail.value;
    if (categoryId === null) {
      this.selectedCategory = null;
    } else {
      this.selectedCategory =
        this.shoppingCategories.find((c) => c.id === categoryId) || null;
    }
    this.cdr.detectChanges();
  }

  onItemStatusChange(item: ShoppingItem): void {
    const localItem = this.localShoppingItems.find((i) => i.id === item.id);
    if (localItem) {
      localItem.isPurchased = item.isPurchased;
      this.cdr.detectChanges();
    }
  }

  onDeleteItem(item: ShoppingItem): void {
    this.localShoppingItems = this.localShoppingItems.filter(
      (i) => i.id !== item.id
    );
    this.cdr.detectChanges();
  }

  /**
   * Get items that were deleted (exist in original but not in local)
   */
  private getDeletedItems(): ShoppingItem[] {
    return this.shoppingItems.filter(
      (originalItem) =>
        !this.localShoppingItems.find(
          (localItem) => localItem.id === originalItem.id
        )
    );
  }

  /**
   * Get items that were modified (status changes)
   */
  private getModifiedItems(): ShoppingItem[] {
    return this.localShoppingItems.filter((localItem) => {
      const originalItem = this.shoppingItems.find(
        (i) => i.id === localItem.id
      );
      return originalItem && originalItem.isPurchased !== localItem.isPurchased;
    });
  }

  /**
   * Check if there are pending changes compared to original data
   */
  hasPendingChanges(): boolean {
    return (
      this.getDeletedItems().length > 0 || this.getModifiedItems().length > 0
    );
  }

  async saveChanges(): Promise<void> {
    if (!this.hasPendingChanges()) {
      await this.toastService.showError('No hay cambios para guardar', 400);
      return;
    }

    this.isSaving = true;

    try {
      const deletedItems = this.getDeletedItems();
      const modifiedItems = this.getModifiedItems();

      for (const item of deletedItems) {
        await this.shoppingService.deleteItem(item.id).toPromise();
      }

      for (const item of modifiedItems) {
        await this.shoppingService.updateItemStatus(item).toPromise();
      }

      this.shoppingItems = [...this.localShoppingItems];
      await this.toastService.showSuccess(
        'Cambios guardados correctamente',
        200
      );
    } catch (error: any) {
      await this.toastService.showError('Error al guardar los cambios', 400);
    } finally {
      this.isSaving = false;
      // Trigger change detection to update button visibility
      this.cdr.detectChanges();
    }
  }

  /**
   * Cancel all pending changes and revert to original data
   */
  cancelChanges(): void {
    // Create deep copy of original data to ensure independent copies
    this.localShoppingItems = this.shoppingItems.map((item) => ({ ...item }));
    // Trigger change detection
    this.cdr.detectChanges();
  }

  async openAddItemModal(): Promise<void> {
    const modal = await this.modalController.create({
      component: AddItemModalComponent,
      cssClass: 'add-item-modal-centered',
      componentProps: {
        categories: this.shoppingCategories,
        selectedCategory: this.selectedCategory,
      },
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();

    await this.processItemAddition(role, data);
  }

  private async processItemAddition(role: string | undefined, data: any) {
    if (role === 'confirm' && data) {
      const newItemDto = data as AddShoppingItemDto;
      try {
        const result = await this.shoppingService
          .addShoppingItem(this.family!.id, newItemDto)
          .toPromise();

        if (result) {
          const newItem = Array.isArray(result) ? result[0] : result;
          this.localShoppingItems.push(newItem);

          await this.toastService.showSuccess(
            'Artículo agregado correctamente',
            200
          );

          // Also update the original items for comparison
          this.shoppingItems.push(newItem);
        }
      } catch (error: any) {
        await this.toastService.showError('Error al agregar artículo', 400);
      }

      this.cdr.detectChanges();
    }
  }

  private onCategorySelected(category: ShoppingCategory) {
    this.selectedCategory = category;
  }
}
