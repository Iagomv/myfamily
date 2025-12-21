import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private getHeaders(customHeaders?: {
    [header: string]: string;
  }): HttpHeaders {
    const defaultHeaders = {
      Authorization: `Bearer ${localStorage.getItem('auth_token') || ''}`,
      'Content-Type': 'application/json',
    };

    return new HttpHeaders({
      ...defaultHeaders,
      ...customHeaders,
    });
  }

  get<T>(
    endpoint: string,
    options?: { params?: any },
    customHeaders?: { [header: string]: string }
  ): Observable<T> {
    const headers = this.getHeaders(customHeaders);
    return this.http.get<T>(`${this.baseUrl}/${endpoint}`, {
      ...(options ?? {}),
      headers,
      observe: 'body' as const,
    });
  }

  post<T>(
    endpoint: string,
    body: any,
    options?: { params?: any },
    customHeaders?: { [header: string]: string }
  ): Observable<T> {
    const headers = this.getHeaders(customHeaders);
    return this.http.post<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...(options ?? {}),
      headers,
      observe: 'body' as const,
    });
  }

  put<T>(
    endpoint: string,
    body: any,
    options?: { params?: any },
    customHeaders?: { [header: string]: string }
  ): Observable<T> {
    const headers = this.getHeaders(customHeaders);
    return this.http.put<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...(options ?? {}),
      headers,
      observe: 'body' as const,
    });
  }

  patch<T>(
    endpoint: string,
    body: any,
    options?: { params?: any },
    customHeaders?: { [header: string]: string }
  ): Observable<T> {
    const headers = this.getHeaders(customHeaders);
    return this.http.patch<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...(options ?? {}),
      headers,
      observe: 'body' as const,
    });
  }

  delete<T>(
    endpoint: string,
    options?: { params?: any },
    customHeaders?: { [header: string]: string }
  ): Observable<T> {
    const headers = this.getHeaders(customHeaders);
    return this.http.delete<T>(`${this.baseUrl}/${endpoint}`, {
      ...(options ?? {}),
      headers,
      observe: 'body' as const,
    });
  }
}
