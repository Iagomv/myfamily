import { Injectable } from '@angular/core';
import { ApiService as HttpService } from './http.service';
import {
  AddShoppingItemDto,
  AddShoppingItemsDto,
} from '../interfaces/shopping.interface';
import {
  CalendarEvent,
  PostCalendarEventDto,
} from '../interfaces/calendar-event.interface';
import { FamilyMemberIconUpdateRequest } from '../interfaces/family-member.interface';
import {
  ProfileInfo,
  UsersDto,
  UserUpdateRequest,
} from '../interfaces/profile.interface';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiCallService {
  constructor(private httpService: HttpService) {}
  // Login and Registration API calls
  createUser(email: string, password: string, username: string) {
    return this.httpService.post('users', { email, password, username });
  }

  login(email: string, password: string) {
    return this.httpService.post('users/login', { email, password });
  }

  // Family Selection API calls
  getMyFamilies() {
    return this.httpService.get('families/my');
  }

  createFamily(familyName: string) {
    return this.httpService.post('families', { familyName });
  }

  joinFamily(invitationCode: string) {
    return this.httpService.post('families/join', { invitationCode });
  }

  leaveFamily(familyId: number) {
    return this.httpService.delete(`families/leave/${familyId}`, {});
  }

  // Family Dashboard API calls
  getFamilyDashboard(familyId: number) {
    return this.httpService.get(`families/dashboard/${familyId}`);
  }

  // Shopping List API calls
  getShoppingCategories() {
    return this.httpService.get('shopping/categories');
  }

  getFamilyShoppingItemsGrouped(familyId: number) {
    return this.httpService.get(`shopping/family/${familyId}/items`);
  }

  addShoppingItem(familyId: number, addShoppingItemDto: AddShoppingItemDto) {
    return this.httpService.post(
      `shopping/shopping-item/${familyId}`,
      addShoppingItemDto
    );
  }

  addShoppingItems(familyId: number, addShoppingItemsDto: AddShoppingItemsDto) {
    return this.httpService.post(`shopping/shopping-items/${familyId}`, {
      addShoppingItemsDto,
    });
  }

  updateShoppingItem(itemId: number, addShoppingItemDto: AddShoppingItemDto) {
    return this.httpService.put(
      `shopping/shopping-item/${itemId}`,
      addShoppingItemDto
    );
  }

  updateShoppingItemStatus(itemId: number, isPurchased: boolean) {
    return this.httpService.patch(
      `shopping/shopping-item/${itemId}/status`,
      null,
      { params: { isPurchased } }
    );
  }

  deleteShoppingItem(itemId: number) {
    return this.httpService.delete(`shopping/shopping-item/${itemId}`);
  }

  // Calendar Events API calls
  getFamilyCalendarEvents(familyId: number) {
    return this.httpService.get<CalendarEvent[]>(`calendar-events/${familyId}`);
  }
  addCalendarEvent(
    familyId: number,
    addCalendarEventDto: PostCalendarEventDto
  ) {
    return this.httpService.post<CalendarEvent>(
      `calendar-events/${familyId}`,
      addCalendarEventDto
    );
  }

  deleteCalendarEvent(eventId: number) {
    return this.httpService.delete<void>(`calendar-events/${eventId}`);
  }

  // Profile API calls
  getProfileInfo(familyId: number) {
    return this.httpService.get<ProfileInfo>(`users/profile/${familyId}`);
  }

  updateSelectedIcon(
    familyMemberIconUpdateRequest: FamilyMemberIconUpdateRequest
  ): Observable<void> {
    return this.httpService.patch<void>(
      'family-members/icon-update',
      familyMemberIconUpdateRequest,
      {}
    );
  }

  updateUserInfo(
    userInfo: UserUpdateRequest,
    familyId: number
  ): Observable<UsersDto> {
    return this.httpService.put<UsersDto>(
      `users/update/${familyId}`,
      userInfo,
      {}
    );
  }

  updateUserPassword(userId: string, newPassword: string): Observable<void> {
    return this.httpService.patch<void>(`users/password/${userId}`, {
      newPassword,
    });
  }
}
