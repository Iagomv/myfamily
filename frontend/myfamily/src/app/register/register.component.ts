import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonItem,
  IonInput,
  IonText,
  IonButton,
} from '@ionic/angular/standalone';
import { ToastService } from '../shared/services/toast.service';
import { validateEmail, validatePassword } from 'src/app/utils/form.utils';
import { RouterModule } from '@angular/router';
import { ApiCallService } from '../shared/services/api-call.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonItem,
    IonInput,
    IonText,
    IonButton,
  ],
})
export class RegisterComponent {
  loading: boolean = false;
  email: string = 'test@example.com';
  username: string = 'testuser';
  password: string = 'testPassword1';
  repeatPassword: string = 'testPassword1';

  constructor(
    private toastService: ToastService,
    private registerService: ApiCallService,
    private router: Router
  ) {}

  onSubmit() {
    if (!this.validateForm()) {
      return;
    }
    this.loading = true;
    this.registerService
      .createUser(this.email, this.password, this.username)
      .subscribe({
        next: async (response) => {
          await this.toastService.showSuccess('Registration successful!', 1000);
          setTimeout(
            () => this.router.navigate(['/login'], { replaceUrl: true }),
            100
          );
        },
        error: (error) => {
          this.loading = false;
          this.toastService.showError(error.error.message);
        },
      });
  }

  private validateForm(): boolean {
    if (!this.email || !this.password || !this.repeatPassword) {
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
    if (this.password !== this.repeatPassword) {
      this.toastService.showError('Passwords do not match.');
      return false;
    }
    return true;
  }
}
