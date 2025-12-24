import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
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
import {
  DocumentCategoryDto,
  DocumentCategoryRequestDto,
  DocumentDtoResponse,
  DocumentRequest,
} from '../interfaces/documents-interface';

@Injectable({ providedIn: 'root' })
export class ApiCallService {
  constructor(private httpService: HttpService, private http: HttpClient) {}
  //* Login and Registration API calls
  createUser(email: string, password: string, username: string) {
    return this.httpService.post('users', { email, password, username });
  }

  login(email: string, password: string) {
    return this.httpService.post('users/login', { email, password });
  }

  //* Family Selection API calls
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

  //* Family Dashboard API calls
  getFamilyDashboard(familyId: number) {
    return this.httpService.get(`families/dashboard/${familyId}`);
  }

  //* Shopping List API calls
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

  //* Calendar Events API calls
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

  updateCalendarEvent(
    eventId: number,
    updateCalendarEventDto: PostCalendarEventDto
  ) {
    return this.httpService.put<CalendarEvent>(
      `calendar-events/${eventId}`,
      updateCalendarEventDto
    );
  }

  deleteCalendarEvent(eventId: number) {
    return this.httpService.delete<void>(`calendar-events/${eventId}`);
  }

  //* Profile API calls
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

  //* Documents API calls
  getFamilyDocuments(familyId: number): Observable<DocumentDtoResponse[]> {
    return this.httpService.get<DocumentDtoResponse[]>(`documents/${familyId}`);
  }

  uploadDocument(
    familyId: number,
    documentRequest: DocumentRequest
  ): Observable<void> {
    const formData = new FormData();
    formData.append('title', documentRequest.title);
    formData.append('categoryId', String(documentRequest.categoryId));
    if (documentRequest.fileData) {
      // Append both keys to increase compatibility with backends that expect
      // either 'file' or 'fileData'. Both point to the same File object.
      formData.append(
        'file',
        documentRequest.fileData,
        documentRequest.fileData.name
      );
      formData.append(
        'fileData',
        documentRequest.fileData,
        documentRequest.fileData.name
      );
    }

    // Debug: log form entries
    try {
      for (const [key, value] of (formData as any).entries()) {
        if (value instanceof File) {
          console.log('[ApiCallService] FormData entry:', key, {
            name: value.name,
            size: value.size,
            type: value.type,
          });
        } else {
          console.log('[ApiCallService] FormData entry:', key, value);
        }
      }
    } catch (err) {
      // ignore iteration errors in some environments
      console.warn(
        '[ApiCallService] Could not iterate FormData for debug',
        err
      );
    }

    const url = `${environment.apiUrl}/documents/${familyId}`;
    return this.http.post<void>(url, formData);
  }

  // Document Categories
  getDocumentCategories(familyId: number): Observable<DocumentCategoryDto[]> {
    return this.httpService.get<DocumentCategoryDto[]>(
      `document-categories/${familyId}`
    );
  }

  createDocumentCategory(
    familyId: number,
    documentCategoryRequestDto: DocumentCategoryRequestDto
  ): Observable<DocumentCategoryDto> {
    return this.httpService.post<DocumentCategoryDto>(
      `document-categories/${familyId}`,
      documentCategoryRequestDto
    );
  }

  deleteDocumentCategory(
    familyId: number,
    categoryId: number
  ): Observable<void> {
    return this.httpService.delete<void>(
      `document-categories/${familyId}/${categoryId}`
    );
  }
}
