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
  PostCalendarEventDto,
  CALENDAR_CATEGORIES,
} from 'src/app/shared/interfaces/calendar-event.interface';
import { FamilyCalendarService } from './family-calendar.service';
import { AddEventModalComponent } from './add-event-modal/add-event-modal.component';
import { BehaviorSubject, Observable, combineLatest } from 'rxjs';
import { map, shareReplay, startWith } from 'rxjs/operators';

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
    this.formatDateToISO(new Date())
  );
  private readonly selectedCategory$ = new BehaviorSubject<string>('');
  private readonly today = new Date();

  loading$: Observable<boolean>;
  upcomingEvents$: Observable<CalendarEvent[]>;
  pastEvents$: Observable<CalendarEvent[]>;
  highlightedDates$: Observable<HighlightedDate[]>;

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

    this.upcomingEvents$ = filteredEvents$.pipe(
      map((events) =>
        events.filter((event) => new Date(event.eventDate) >= this.today)
      )
    );

    this.pastEvents$ = filteredEvents$.pipe(
      map((events) =>
        events
          .filter((event) => new Date(event.eventDate) < this.today)
          .sort(
            (a, b) =>
              new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime()
          )
      )
    );

    this.highlightedDates$ = this.calendarService.events$.pipe(
      map((events) => this.generateHighlightedDates(events)),
      shareReplay(1)
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
      const selectedDateObj = new Date(selectedDate);
      filtered = filtered.filter(
        (event) => new Date(event.eventDate) >= selectedDateObj
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
      const dateStr = this.formatDateToISO(new Date(event.eventDate));
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

  private formatDateToISO(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  async openAddEventModal(): Promise<void> {
    const modal = await this.modalController.create({
      component: AddEventModalComponent,
      cssClass: 'add-item-modal-centered',
      componentProps: {
        categories: CALENDAR_CATEGORIES,
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

  ngOnDestroy() {
    this.selectedDate$.complete();
    this.selectedCategory$.complete();
  }
}
