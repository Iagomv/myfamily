import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, catchError, finalize } from 'rxjs/operators';
import {
  CalendarEvent,
  PostCalendarEventDto,
} from 'src/app/shared/interfaces/calendar-event.interface';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { FamilyService } from 'src/app/shared/services/family.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Injectable({
  providedIn: 'root',
})
export class FamilyCalendarService {
  private eventsSubject = new BehaviorSubject<CalendarEvent[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public events$ = this.eventsSubject.asObservable();
  public loading$ = this.loadingSubject.asObservable();

  constructor(
    private apiService: ApiCallService,
    private familyService: FamilyService,
    private toastService: ToastService
  ) {}

  fetchCalendarEvents(): void {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return;
    }

    this.loadingSubject.next(true);
    this.apiService
      .getFamilyCalendarEvents(familyId)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: (events: any) => {
          const calendarEvents = Array.isArray(events) ? events : [];
          this.eventsSubject.next(calendarEvents);
        },
        error: (err: any) => {
          console.error('Error fetching calendar events:', err);
          this.toastService.showError('Error fetching calendar events');
        },
      });
  }

  postCalendarEvent(event: PostCalendarEventDto): Observable<any> {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('Error loading family data');
      return throwError(() => new Error('No family selected'));
    }

    return this.apiService.addCalendarEvent(familyId, event).pipe(
      tap({
        next: () => {
          this.toastService.showSuccess('Evento creado correctamente');
          this.fetchCalendarEvents();
        },
      }),
      catchError((error) => {
        console.error('Error creating calendar event:', error);
        this.toastService.showError('Error al crear el evento');
        return throwError(() => error);
      })
    );
  }

  deleteCalendarEvent(eventId: number): Observable<any> {
    return this.apiService.deleteCalendarEvent(eventId).pipe(
      tap({
        next: () => {
          this.toastService.showSuccess('Evento eliminado correctamente');
          this.fetchCalendarEvents();
        },
      }),
      catchError((error) => {
        console.error('Error deleting calendar event:', error);
        this.toastService.showError('Error al eliminar el evento');
        return throwError(() => error);
      })
    );
  }

  getEvents(): CalendarEvent[] {
    return this.eventsSubject.value;
  }

  isLoading(): boolean {
    return this.loadingSubject.value;
  }
}
