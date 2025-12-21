import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

export interface AuthTokenPayload {
  sub: string;
  userId: number;
  iat: number;
  exp: number;
}

@Injectable({ providedIn: 'root' })
export class SecurityService {
  private tokenKey = 'auth_token';

  // Store the token
  storeToken(token: string): void {
    const decodedToken = this.decodeToken<AuthTokenPayload>(token);
    const expirationTime = decodedToken?.exp ? decodedToken.exp * 1000 : 0; // Convert seconds to milliseconds
    localStorage.setItem(this.tokenKey, token);

    if (expirationTime) {
      localStorage.setItem('token_expiration', expirationTime.toString());
    } else {
      localStorage.removeItem('token_expiration');
    }
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

  private decodeToken<T>(token: string): T | null {
    try {
      return jwtDecode<T>(token);
    } catch {
      return null;
    }
  }

  getAuthTokenPayload(): AuthTokenPayload | null {
    const token = this.getToken();
    if (!token) return null;

    const decoded = this.decodeToken<AuthTokenPayload>(token);
    if (!decoded) return null;

    if (
      typeof decoded.userId !== 'number' ||
      !Number.isFinite(decoded.userId)
    ) {
      return null;
    }
    if (typeof decoded.sub !== 'string' || decoded.sub.trim().length === 0) {
      return null;
    }
    if (typeof decoded.exp !== 'number' || !Number.isFinite(decoded.exp)) {
      return null;
    }
    if (typeof decoded.iat !== 'number' || !Number.isFinite(decoded.iat)) {
      return null;
    }

    return decoded;
  }

  getUserIdFromToken(): number | null {
    return this.getAuthTokenPayload()?.userId ?? null;
  }

  getEmailFromToken(): string | null {
    return this.getAuthTokenPayload()?.sub ?? null;
  }

  getTokenExpirationTimeMs(): number | null {
    const exp = this.getAuthTokenPayload()?.exp;
    return exp ? exp * 1000 : null;
  }
}
