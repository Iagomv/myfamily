import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { SecurityService } from '../services/security.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private securityService: SecurityService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.securityService.isLoggedIn()) {
      return true;
    }

    // No token found, redirect to login
    this.router.navigate(['/login'], { replaceUrl: true });
    return false;
  }
}
