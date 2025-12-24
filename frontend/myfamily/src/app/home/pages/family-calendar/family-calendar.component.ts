import {
  Component,
  OnInit,
  CUSTOM_ELEMENTS_SCHEMA,
  OnDestroy,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule, ModalController } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { FloatingButtonComponent } from '../../../shared/components/floating-button/floating-button.component';
import { EventCardComponent } from './event-card/event-card.component';
import {
  CalendarEvent,
  CALENDAR_CATEGORIES,
  PostCalendarEventDto,
} from 'src/app/shared/interfaces/calendar-event.interface';
import { FamilyCalendarService } from './family-calendar.service';
import { AddEventModalComponent } from './add-event-modal/add-event-modal.component';
import { BehaviorSubject, Observable, combineLatest } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

interface HighlightedDate {
  date: string;
  textColor: string;
  backgroundColor: string;
  border: string;
}

@Component({
  selector: 'app-family-calendar',
  templateUrl: './family-calendar.component.html',
  styleUrls: ['./family-calendar.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    FormsModule,
    HeaderComponent,
    EventCardComponent,
    FloatingButtonComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyCalendarComponent implements OnInit, OnDestroy {
  readonly CALENDAR_CATEGORIES = CALENDAR_CATEGORIES;

  private readonly selectedDate$ = new BehaviorSubject<string>(
    this.toISODateOnly(new Date())
  );
  private readonly selectedCategory$ = new BehaviorSubject<string>('');
  private readonly today = new Date();

  loading$: Observable<boolean>;
  upcomingEvents$!: Observable<CalendarEvent[]>;
  pastEvents$!: Observable<CalendarEvent[]>;
  highlightedDates$!: Observable<HighlightedDate[]>;

  constructor(
    private calendarService: FamilyCalendarService,
    private modalController: ModalController
  ) {
    this.loading$ = this.calendarService.loading$;

    const filteredEvents$ = combineLatest([
      this.calendarService.events$,
      this.selectedDate$,
      this.selectedCategory$,
    ]).pipe(
      map(([events, selectedDate, selectedCategory]) =>
        this.getFilteredEvents(events, selectedDate, selectedCategory)
      ),
      shareReplay(1)
    );

    this.setUpcomingEvents(filteredEvents$);

    this.setPastEvents(filteredEvents$);

    this.setHighlightedDates();
  }

  private setHighlightedDates() {
    this.highlightedDates$ = this.calendarService.events$.pipe(
      map((events) => this.generateHighlightedDates(events)),
      shareReplay(1)
    );
  }

  private setPastEvents(filteredEvents$: Observable<CalendarEvent[]>) {
    this.pastEvents$ = filteredEvents$.pipe(
      map((events) => {
        const todayDateOnly = this.toISODateOnly(this.today);
        return events
          .filter(
            (event) => this.toISODateOnly(event.eventDate) < todayDateOnly
          )
          .sort(
            (a, b) =>
              new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime()
          );
      })
    );
  }

  private setUpcomingEvents(filteredEvents$: Observable<CalendarEvent[]>) {
    this.upcomingEvents$ = filteredEvents$.pipe(
      map((events) => {
        const todayDateOnly = this.toISODateOnly(this.today);
        return events.filter(
          (event) => this.toISODateOnly(event.eventDate) >= todayDateOnly
        );
      })
    );
  }

  ngOnInit() {
    this.calendarService.fetchCalendarEvents();
  }

  onDateChange(date: string | string[] | null | undefined): void {
    if (date && typeof date === 'string') {
      this.selectedDate$.next(date);
    }
  }

  onCategoryChange(category: string | string[] | null | undefined): void {
    if (typeof category === 'string') {
      this.selectedCategory$.next(category);
    }
  }

  private getFilteredEvents(
    events: CalendarEvent[],
    selectedDate: string,
    selectedCategory: string
  ): CalendarEvent[] {
    let filtered = events;

    if (selectedDate) {
      const selectedDateOnly = this.toISODateOnly(selectedDate);
      filtered = filtered.filter(
        (event) => this.toISODateOnly(event.eventDate) >= selectedDateOnly
      );
    }

    if (selectedCategory) {
      filtered = filtered.filter(
        (event) => event.eventCategory === selectedCategory
      );
    }

    return filtered;
  }

  private generateHighlightedDates(events: CalendarEvent[]): HighlightedDate[] {
    const groupedByDate: Map<string, CalendarEvent[]> = new Map();

    events.forEach((event) => {
      const dateStr = this.toISODateOnly(event.eventDate);
      if (!groupedByDate.has(dateStr)) {
        groupedByDate.set(dateStr, []);
      }
      groupedByDate.get(dateStr)!.push(event);
    });

    return Array.from(groupedByDate.entries()).map(([date, dateEvents]) => {
      const firstEvent = dateEvents[0];
      const category = CALENDAR_CATEGORIES.find(
        (cat) => cat.id === firstEvent.eventCategory
      );
      const color = category?.color || '#C7CEEA';

      return {
        date,
        textColor: '#000000',
        backgroundColor: color,
        border: `1px solid black`,
      };
    });
  }

  private toISODateOnly(value: unknown): string {
    if (!value) return '';

    if (typeof value === 'string') {
      // Handles both 'YYYY-MM-DD' and full ISO strings like 'YYYY-MM-DDTHH:mm:ss.sssZ'
      return value.slice(0, 10);
    }

    if (value instanceof Date) {
      // Use UTC date portion to avoid local timezone shifting the day.
      return value.toISOString().slice(0, 10);
    }

    const asDate = new Date(value as any);
    return isNaN(asDate.getTime()) ? '' : asDate.toISOString().slice(0, 10);
  }

  async openAddEventModal(): Promise<void> {
    const modal = await this.modalController.create({
      component: AddEventModalComponent,
      cssClass: 'add-item-modal-centered',
      breakpoints: [0.5, 0.75, 0.95],
      initialBreakpoint: 0.75,
      handle: true,
      backdropBreakpoint: 0.5,
      componentProps: {
        categories: CALENDAR_CATEGORIES,
        defaultDate: this.selectedDate$.value,
      },
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();

    if (role === 'confirm' && data) {
      this.calendarService.postCalendarEvent(data).subscribe();
    }
  }

  onEventDeleted(event: CalendarEvent): void {
    this.calendarService.deleteCalendarEvent(event.id).subscribe();
  }

  async onEditEvent(event: CalendarEvent): Promise<void> {
    const modal = await this.modalController.create({
      component: AddEventModalComponent,
      cssClass: 'add-item-modal-centered',
      breakpoints: [0.5, 0.75, 0.95],
      initialBreakpoint: 0.75,
      handle: true,
      backdropBreakpoint: 0.5,
      componentProps: {
        categories: CALENDAR_CATEGORIES,
        defaultDate: this.toISODateOnly(event.eventDate),
        editEvent: event,
      },
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();

    await this.processEventEdit(event, role, data);
  }

  private async processEventEdit(
    originalEvent: CalendarEvent,
    role: string | undefined,
    data: any
  ) {
    if (role === 'confirm' && data) {
      const updateDto = data as PostCalendarEventDto;
      this.calendarService
        .updateCalendarEvent(originalEvent.id, updateDto)
        .subscribe();
    }
  }

  ngOnDestroy() {
    this.selectedDate$.complete();
    this.selectedCategory$.complete();
  }
}
