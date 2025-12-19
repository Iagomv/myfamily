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
  PostCalendarEventDto,
  CalendarCategory,
} from 'src/app/shared/interfaces/calendar-event.interface';

interface CalendarCategoryOption {
  id: string;
  name: string;
}

@Component({
  selector: 'app-add-event-modal',
  templateUrl: './add-event-modal.component.html',
  styleUrls: ['./add-event-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AddEventModalComponent implements OnInit {
  @Input() categories: CalendarCategoryOption[] = [];
  @Input() defaultDate?: string;

  eventName: string = '';
  eventDescription: string = '';
  selectedCategory: string | null = null;
  eventDate: string = '';
  isSubmitting = false;

  constructor(private modalController: ModalController) {}

  ngOnInit() {
    this.eventDate = (this.defaultDate ?? new Date().toISOString()).slice(
      0,
      10
    );

    // Set default category if provided
    if (this.categories.length > 0) {
      this.selectedCategory = this.categories[0].id;
    }
  }

  /**
   * Create new PostCalendarEventDto object
   */
  private createPostCalendarEventDto(): PostCalendarEventDto {
    return {
      eventName: this.eventName.trim(),
      eventDescription: this.eventDescription.trim(),
      eventCategory: this.selectedCategory || '',
      eventDate: new Date(this.eventDate),
    };
  }

  /**
   * Validate form inputs
   */
  private validateForm(): boolean {
    if (!this.eventName || this.eventName.trim().length === 0) {
      return false;
    }
    if (!this.eventDescription || this.eventDescription.trim().length === 0) {
      return false;
    }
    if (!this.selectedCategory) {
      return false;
    }
    if (!this.eventDate) {
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
    const newEvent = this.createPostCalendarEventDto();

    try {
      await this.modalController.dismiss(newEvent, 'confirm');
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
      this.eventName.trim().length > 0 &&
      this.eventDescription.trim().length > 0 &&
      this.selectedCategory !== null &&
      this.eventDate.trim().length > 0
    );
  }
}
