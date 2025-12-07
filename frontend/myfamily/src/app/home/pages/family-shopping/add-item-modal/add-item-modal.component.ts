import {
  Component,
  OnInit,
  Input,
  CUSTOM_ELEMENTS_SCHEMA,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ModalController } from '@ionic/angular';
import {
  AddShoppingItemDto,
  ShoppingCategory,
} from 'src/app/shared/interfaces/shopping.interface';

@Component({
  selector: 'app-add-item-modal',
  templateUrl: './add-item-modal.component.html',
  styleUrls: ['./add-item-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AddItemModalComponent implements OnInit {
  @Input() categories: ShoppingCategory[] = [];
  @Input() selectedCategory: ShoppingCategory | null = null;
  name: string = '';
  quantity: number = 1;
  selectedCategoryId: number | null = null;
  isSubmitting = false;

  constructor(private modalController: ModalController) {}

  ngOnInit() {
    // Set the selected category as default if provided
    if (this.selectedCategory) {
      this.selectedCategoryId = this.selectedCategory.id;
    }
  }

  /**
   * Create new AddShoppingItemDto object
   */
  private createAddShoppingItemDto(): AddShoppingItemDto {
    return {
      name: this.name.trim(),
      quantity: this.quantity,
      categoryId: this.selectedCategoryId || 0,
    };
  }

  /**
   * Validate form inputs
   */
  private validateForm(): boolean {
    if (!this.name || this.name.trim().length === 0) {
      return false;
    }
    if (this.quantity <= 0) {
      return false;
    }
    if (!this.selectedCategoryId) {
      return false;
    }
    return true;
  }

  /**
   * Handle form submission
   */
  async onSubmit(): Promise<void> {
    if (!this.validateForm()) {
      return;
    }

    this.isSubmitting = true;
    const newItem = this.createAddShoppingItemDto();

    try {
      await this.modalController.dismiss(newItem, 'confirm');
    } catch (error) {
      this.isSubmitting = false;
    }
  }

  /**
   * Handle cancel button
   */
  async onCancel(): Promise<void> {
    await this.modalController.dismiss();
  }

  /**
   * Check if form is valid
   */
  isFormValid(): boolean {
    return (
      this.name.trim().length > 0 &&
      this.quantity > 0 &&
      this.selectedCategoryId !== null
    );
  }
}
