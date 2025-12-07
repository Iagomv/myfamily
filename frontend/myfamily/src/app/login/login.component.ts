import { Component, OnInit } from '@angular/core';
import { ApiCallService } from '../shared/services/api-call.service';
import { validateEmail, validatePassword } from 'src/app/utils/form.utils';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule, Router } from '@angular/router';
import { ToastService } from '../shared/services/toast.service';
import { SecurityService } from '../shared/services/security.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, RouterModule],
})
export class LoginComponent implements OnInit {
  email: string = 'test@example.com';
  password: string = 'testPassword1';
  loading: boolean = false;

  constructor(
    private loginService: ApiCallService,
    private toastService: ToastService,
    private router: Router,
    private securityService: SecurityService
  ) {}

  ngOnInit() {
    this.checkToken();
  }

  onSubmit() {
    if (!this.validateForm()) {
      return;
    }
    this.loading = true;
    this.loginService.login(this.email, this.password).subscribe({
      next: async (response: any) => {
        this.securityService.storeToken(response.token);
        await this.toastService.showSuccess('Login successful!', 1000);
        setTimeout(
          () => this.router.navigate(['/home/family-selection'], { replaceUrl: true }),
          100
        );
      },
      error: (error: any) => {
        this.loading = false;
        this.toastService.showError(error.error.message);
      },
    });
  }

  private validateForm(): boolean {
    if (!this.email || !this.password) {
      this.toastService.showError('All fields are required.');
      return false;
    }
    if (!validateEmail(this.email)) {
      this.toastService.showError('Invalid email format.');
      return false;
    }
    if (!validatePassword(this.password)) {
      this.toastService.showError(
        'Password must be 8-20 characters long, include uppercase, lowercase letters, and a number.'
      );
      return false;
    }
    return true;
  }
  private setLoadingState(isLoading: boolean) {
    this.loading = isLoading;
  }
  private checkToken() {
    if (this.securityService.isLoggedIn()) {
      this.router.navigate(['/home'], { replaceUrl: true });
    }
  }
}
