import { CalendarEvent } from './calendar-event.interface';
import { ShoppingItem } from './shopping.interface';
import { familyMember } from './family-member.interface';
import { CalendarEventMonthlyStats } from './calendar-event.interface';
import { ShoppingItemsMonthlyStats } from './shopping.interface';

export interface FamilyDashboard {
  id: number;
  familyName: string;
  invitationCode: string;
  calendarEvents: CalendarEvent[];
  shoppingItems: ShoppingItem[];
  familyMembers: familyMember[];
  shoppingItemsMonthlyStats: ShoppingItemsMonthlyStats;
  calendarEventsMonthlyStats: CalendarEventMonthlyStats;
}
