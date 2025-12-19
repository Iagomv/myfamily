import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import {
  CalendarEvent,
  CALENDAR_CATEGORIES,
} from 'src/app/shared/interfaces/calendar-event.interface';

@Component({
  selector: 'app-dashboard-event-card',
  templateUrl: './dashboard-event-card.component.html',
  styleUrls: ['./dashboard-event-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DashboardEventCardComponent {
  @Input() event!: CalendarEvent;
  readonly CALENDAR_CATEGORIES = CALENDAR_CATEGORIES;

  getCategoryLabel(eventCategory: string): string {
    const match = CALENDAR_CATEGORIES.find((c) => c.id === eventCategory);
    return match?.name ?? eventCategory;
  }

  getCategoryColor(eventCategory: string): string {
    const match = CALENDAR_CATEGORIES.find((c) => c.id === eventCategory);
    return match?.color ?? 'transparent';
  }

  formatEventDate(isoDate: string): string {
    const date = new Date(isoDate);
    if (Number.isNaN(date.getTime())) {
      return '';
    }

    const now = new Date();
    const today = new Date(now);
    today.setHours(0, 0, 0, 0);
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    const day = new Date(date);
    day.setHours(0, 0, 0, 0);

    const hasTime = date.getHours() !== 0 || date.getMinutes() !== 0;
    const timePart = hasTime
      ? `, ${date.toLocaleTimeString('es-ES', {
          hour: 'numeric',
          minute: '2-digit',
        })}`
      : '';

    if (day.getTime() === today.getTime()) {
      return `Hoy${timePart}`;
    }

    if (day.getTime() === tomorrow.getTime()) {
      return `Mañana${timePart}`;
    }

    const datePart = date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: 'short',
    });

    return `${datePart}${timePart}`;
  }
}
