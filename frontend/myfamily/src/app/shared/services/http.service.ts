import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

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
    options?: any,
    customHeaders?: { [header: string]: string }
  ) {
    const headers = this.getHeaders(customHeaders);
    return this.http.get<T>(`${this.baseUrl}/${endpoint}`, {
      ...options,
      headers,
    });
  }

  post<T>(
    endpoint: string,
    body: any,
    options?: any,
    customHeaders?: { [header: string]: string }
  ) {
    const headers = this.getHeaders(customHeaders);
    return this.http.post<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...options,
      headers,
    });
  }

  put<T>(
    endpoint: string,
    body: any,
    options?: any,
    customHeaders?: { [header: string]: string }
  ) {
    const headers = this.getHeaders(customHeaders);
    return this.http.put<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...options,
      headers,
    });
  }

  patch<T>(
    endpoint: string,
    body: any,
    options?: any,
    customHeaders?: { [header: string]: string }
  ) {
    const headers = this.getHeaders(customHeaders);
    return this.http.patch<T>(`${this.baseUrl}/${endpoint}`, body, {
      ...options,
      headers,
    });
  }

  delete<T>(
    endpoint: string,
    options?: any,
    customHeaders?: { [header: string]: string }
  ) {
    const headers = this.getHeaders(customHeaders);
    return this.http.delete<T>(`${this.baseUrl}/${endpoint}`, {
      ...options,
      headers,
    });
  }
}
