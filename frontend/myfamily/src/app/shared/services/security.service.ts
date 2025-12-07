import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class SecurityService {
  private tokenKey = 'auth_token';

  // Store the token
  storeToken(token: string): void {
    const decodedToken: any = jwtDecode(token);
    const expirationTime = decodedToken.exp * 1000; // Convert seconds to milliseconds
    localStorage.setItem(this.tokenKey, token);
    localStorage.setItem('token_expiration', expirationTime.toString());
  }

  // Retrieve the token
  getToken(): string | null {
    const expiration = localStorage.getItem('token_expiration');
    if (expiration && new Date().getTime() > parseInt(expiration, 10)) {
      this.clearToken(); // Token expired, clear it
      return null;
    }
    return localStorage.getItem(this.tokenKey);
  }

  // Clear the token
  clearToken(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('token_expiration');
  }

  // Check if the user is logged in
  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }
}
