import {
  Component,
  Input,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { ShoppingItem } from 'src/app/shared/interfaces/shopping.interface';

@Component({
  selector: 'app-shopping-item-card',
  templateUrl: './shopping-item-card.component.html',
  styleUrls: ['./shopping-item-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class ShoppingItemCardComponent {
  @Input() item!: ShoppingItem;
  @Output() statusChanged = new EventEmitter<ShoppingItem>();
  @Output() itemDeleted = new EventEmitter<ShoppingItem>();
  @ViewChild('itemCard') itemCard!: ElementRef;

  private touchStartX = 0;
  private touchEndX = 0;
  isSliding = false;
  slideDistance = 0;

  onStatusChange(): void {
    this.item.isPurchased = !this.item.isPurchased;
    this.statusChanged.emit(this.item);
  }

  onDelete(): void {
    this.itemDeleted.emit(this.item);
  }

  onTouchStart(event: TouchEvent): void {
    this.touchStartX = event.changedTouches[0].screenX;
    this.isSliding = false;
  }

  onTouchMove(event: TouchEvent): void {
    const currentX = event.changedTouches[0].screenX;
    const diff = this.touchStartX - currentX;

    // Start sliding after 20px movement to the right
    if (diff < -20) {
      this.isSliding = true;
      // Calculate slide distance (max 100px)
      this.slideDistance = Math.min(Math.abs(diff), 100);
    }
    // Undo sliding if swiping back left
    else if (diff > 20) {
      this.isSliding = false;
      this.slideDistance = 0;
    }
  }

  onTouchEnd(event: TouchEvent): void {
    this.touchEndX = event.changedTouches[0].screenX;
    const diff = this.touchStartX - this.touchEndX;

    // Swipe right (negative diff means moving right) to delete
    if (diff < -100) {
      // Swipe threshold of 100px
      this.onDelete();
    }

    // Reset sliding state
    this.isSliding = false;
    this.slideDistance = 0;
  }
}
