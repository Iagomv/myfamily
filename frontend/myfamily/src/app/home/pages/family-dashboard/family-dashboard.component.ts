import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { InvitationCodeCardComponent } from '../../../shared/components/invitation-code-card/invitation-code-card.component';
import { MemberCardComponent } from '../../../shared/components/member-card/member-card.component';
import { DashboardEventCardComponent } from './dashboard-event-card/dashboard-event-card.component';
import { DashboardSummaryCardComponent } from './dashboard-summary-card/dashboard-summary-card.component';
import { FamilyDashboard } from 'src/app/shared/interfaces/family-dashboard.interface';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { FamilyService } from 'src/app/shared/services/family.service';
import {
  CalendarEvent,
  CALENDAR_CATEGORIES,
} from 'src/app/shared/interfaces/calendar-event.interface';

@Component({
  selector: 'app-family-dashboard',
  templateUrl: './family-dashboard.component.html',
  styleUrls: ['./family-dashboard.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    HeaderComponent,
    InvitationCodeCardComponent,
    MemberCardComponent,
    DashboardEventCardComponent,
    DashboardSummaryCardComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyDashboardComponent implements OnInit {
  familyDashboard?: FamilyDashboard;
  isLoading = true;
  loadError = false;

  pendingItemsCount = 0;
  upcomingEventsCount = 0;
  upcomingEvents: CalendarEvent[] = [];

  shoppingPendingCount = 0;
  shoppingPurchasedCount = 0;
  mostPurchasedCategoryName = '—';

  calendarTotalCount = 0;
  calendarPastCount = 0;
  calendarUpcomingCount = 0;
  calendarMostFrequentCategoryLabel = '—';

  currentMonth: string;

  constructor(
    private apiCallService: ApiCallService,
    private familyService: FamilyService,
    private router: Router
  ) {
    const now = new Date();
    this.currentMonth = now.toLocaleString('es-ES', { month: 'long' });
  }

  ngOnInit() {
    this.getFamilyDashboard();
  }

  private getFamilyDashboard() {
    const familyId = this.familyService.getFamilyId();

    if (!familyId) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.isLoading = true;
    this.loadError = false;

    this.apiCallService.getFamilyDashboard(familyId).subscribe({
      next: (dashboard: any) => {
        this.familyDashboard = dashboard as FamilyDashboard;
        this.computeDerivedState();
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.loadError = true;
      },
    });
  }

  private computeDerivedState(): void {
    const dashboard = this.familyDashboard;
    if (!dashboard) {
      this.pendingItemsCount = 0;
      this.upcomingEventsCount = 0;
      this.upcomingEvents = [];

      this.shoppingPendingCount = 0;
      this.shoppingPurchasedCount = 0;
      this.mostPurchasedCategoryName = '—';

      this.calendarTotalCount = 0;
      this.calendarPastCount = 0;
      this.calendarUpcomingCount = 0;
      this.calendarMostFrequentCategoryLabel = '—';
      return;
    }

    this.pendingItemsCount = (dashboard.shoppingItems ?? []).filter(
      (item) => !item.isPurchased
    ).length;

    const todayStart = new Date();
    todayStart.setHours(0, 0, 0, 0);

    this.upcomingEvents = (dashboard.calendarEvents ?? [])
      .filter((evt) => {
        const eventDate = new Date(evt.eventDate);
        return !Number.isNaN(eventDate.getTime()) && eventDate >= todayStart;
      })
      .sort(
        (a, b) =>
          new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime()
      );

    this.upcomingEventsCount = this.upcomingEvents.length;

    this.shoppingPendingCount =
      dashboard.shoppingItemsMonthlyStats?.pendingItems ??
      this.pendingItemsCount;
    this.shoppingPurchasedCount =
      dashboard.shoppingItemsMonthlyStats?.purchasedItems ?? 0;
    this.mostPurchasedCategoryName =
      dashboard.shoppingItemsMonthlyStats?.mostPurchasedCategory?.name ?? '—';

    this.calendarTotalCount =
      dashboard.calendarEventsMonthlyStats?.totalEvents ??
      (dashboard.calendarEvents ?? []).length;
    this.calendarPastCount =
      dashboard.calendarEventsMonthlyStats?.pastEvents ?? 0;
    this.calendarUpcomingCount =
      dashboard.calendarEventsMonthlyStats?.upcomingEvents ??
      this.upcomingEventsCount;
    this.calendarMostFrequentCategoryLabel = dashboard
      .calendarEventsMonthlyStats?.mostFrequentCategory
      ? this.getCategoryLabel(
          dashboard.calendarEventsMonthlyStats.mostFrequentCategory
        )
      : '—';
  }

  onChangeFamily(): void {
    this.router.navigate(['/home/family-selection']);
  }

  getCategoryLabel(eventCategory: string): string {
    const match = CALENDAR_CATEGORIES.find((c) => c.id === eventCategory);
    return match?.name ?? eventCategory;
  }
}
