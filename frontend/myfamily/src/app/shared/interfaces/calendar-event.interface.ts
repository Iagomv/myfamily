export interface CalendarEvent {
  id: number;
  eventName: string;
  eventDescription: string;
  eventDate: Date;
  eventCategory: string;
}

export enum CalendarCategory {
  BIRTHDAY = 'Cumpleaños',
  MEDICAL = 'Médico',
  KIDS = 'Niños',
  APPOINTMENT = 'Cita',
  ONLINE = 'Online',
  OTHER = 'Otros',
}

export interface PostCalendarEventDto {
  eventName: string;
  eventDescription: string;
  eventCategory: string;
  eventDate: Date;
}

export const CALENDAR_CATEGORIES = [
  { id: 'BIRTHDAY', name: CalendarCategory.BIRTHDAY, color: '#df8afeff' },
  { id: 'MEDICAL', name: CalendarCategory.MEDICAL, color: '#f86c6eff' },
  { id: 'KIDS', name: CalendarCategory.KIDS, color: '#FFE66D' },
  { id: 'APPOINTMENT', name: CalendarCategory.APPOINTMENT, color: '#95E1D3' },
  { id: 'ONLINE', name: CalendarCategory.ONLINE, color: '#6f9dffff' },
  { id: 'OTHER', name: CalendarCategory.OTHER, color: '#C7CEEA' },
];
