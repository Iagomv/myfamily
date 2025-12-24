import {
  Component,
  Input,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
  OnInit,
  OnDestroy,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule, AlertController } from '@ionic/angular';
import {
  CalendarEvent,
  CALENDAR_CATEGORIES,
} from 'src/app/shared/interfaces/calendar-event.interface';

@Component({
  selector: 'app-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class EventCardComponent implements OnInit, OnDestroy {
  @Input() event!: CalendarEvent;
  @Output() eventDeleted = new EventEmitter<CalendarEvent>();
  @Output() editRequested = new EventEmitter<CalendarEvent>();
  @ViewChild('eventCard', { static: true }) eventCard!: ElementRef<HTMLElement>;

  private touchStartX = 0;
  isSliding = false;
  slideDistance = 0;

  private readonly onTouchStartHandler = (event: TouchEvent) =>
    this.onTouchStart(event);
  private readonly onTouchMoveHandler = (event: TouchEvent) =>
    this.onTouchMove(event);
  private readonly onTouchEndHandler = (event: TouchEvent) =>
    this.onTouchEnd(event);

  constructor(private alertController: AlertController) {}

  ngOnInit(): void {
    const element = this.eventCard?.nativeElement;
    if (!element) return;

    element.addEventListener('touchstart', this.onTouchStartHandler, {
      passive: true,
    });
    element.addEventListener('touchmove', this.onTouchMoveHandler, {
      passive: true,
    });
    element.addEventListener('touchend', this.onTouchEndHandler, {
      passive: true,
    });
  }

  ngOnDestroy(): void {
    const element = this.eventCard?.nativeElement;
    if (!element) return;

    element.removeEventListener('touchstart', this.onTouchStartHandler);
    element.removeEventListener('touchmove', this.onTouchMoveHandler);
    element.removeEventListener('touchend', this.onTouchEndHandler);
  }

  getCategoryColor(): string {
    const category = CALENDAR_CATEGORIES.find(
      (cat) => cat.id === this.event.eventCategory
    );
    return category?.color || '#C7CEEA';
  }

  getCategoryName(): string {
    const category = CALENDAR_CATEGORIES.find(
      (cat) => cat.id === this.event.eventCategory
    );
    return category?.name || this.event.eventCategory;
  }

  async onDelete(): Promise<void> {
    const alert = await this.alertController.create({
      header: 'Eliminar Evento',
      message: `¿Estás seguro de que deseas eliminar "${this.event.eventName}"?`,
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'alert-button-cancel',
        },
        {
          text: 'Eliminar',
          role: 'destructive',
          cssClass: 'alert-button-danger',
          handler: () => {
            this.eventDeleted.emit(this.event);
          },
        },
      ],
    });

    await alert.present();
  }

  onEditClick(): void {
    this.editRequested.emit(this.event);
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
      this.slideDistance = Math.min(Math.abs(diff), 100);
    }
    // Undo sliding if swiping back left
    else if (diff > 20) {
      this.isSliding = false;
      this.slideDistance = 0;
    }
  }

  onTouchEnd(event: TouchEvent): void {
    const diff = this.touchStartX - event.changedTouches[0].screenX;

    // Swipe right (negative diff means moving right) to delete
    if (diff < -100) {
      this.onDelete();
    }

    // Reset sliding state
    this.isSliding = false;
    this.slideDistance = 0;
  }
}
