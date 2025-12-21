import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { IonicModule, ModalController } from '@ionic/angular';
import { ApiCallService } from '../../../../shared/services/api-call.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-update-password-modal',
  templateUrl: './update-password-modal.component.html',
  styleUrls: ['./update-password-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IonicModule],
})
export class UpdatePasswordModalComponent implements OnInit {
  passwordForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  showNewPassword = false;
  showRepeatPassword = false;
  userId: number | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private modalController: ModalController,
    private apiCallService: ApiCallService,
    private securityService: SecurityService,
    private toastService: ToastService
  ) {
    this.passwordForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      repeatPassword: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit() {
    this.userId = this.securityService.getUserIdFromToken();
  }

  /**
   * Toggle password field visibility
   */
  togglePasswordVisibility(field: 'newPassword' | 'repeatPassword'): void {
    if (field === 'newPassword') {
      this.showNewPassword = !this.showNewPassword;
    } else {
      this.showRepeatPassword = !this.showRepeatPassword;
    }
  }

  /**
   * Reset form to initial state
   */
  private resetForm(): void {
    this.passwordForm.reset();
    this.showNewPassword = false;
    this.showRepeatPassword = false;
    this.errorMessage = '';
  }

  /**
   * Validates that both password fields match
   */
  get passwordsMatch(): boolean {
    const password = this.passwordForm.get('newPassword')?.value;
    const repeatPassword = this.passwordForm.get('repeatPassword')?.value;
    return password === repeatPassword && password !== '';
  }

  /**
   * Get error message for password field
   */
  getPasswordError(): string {
    const control = this.passwordForm.get('newPassword');
    if (control?.hasError('required')) {
      return 'Password is required';
    }
    if (control?.hasError('minlength')) {
      return 'Password must be at least 6 characters';
    }
    return '';
  }

  /**
   * Get error message for repeat password field
   */
  getRepeatPasswordError(): string {
    const control = this.passwordForm.get('repeatPassword');
    if (control?.hasError('required')) {
      return 'Please confirm your password';
    }
    if (control?.hasError('minlength')) {
      return 'Password must be at least 6 characters';
    }
    if (!this.passwordsMatch) {
      return 'Passwords do not match';
    }
    return '';
  }

  /**
   * Save new password
   */
  async onSave() {
    if (!this.passwordForm.valid || !this.passwordsMatch) {
      this.errorMessage = 'Please fix the errors above before saving';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    try {
      const newPassword = this.passwordForm.get('newPassword')?.value;

      await this.apiCallService
        .updateUserPassword(String(this.userId), newPassword)
        .toPromise();

      // Success - show toast, reset form and close the modal
      this.toastService.showSuccess('Contraseña actualizada correctamente');
      this.resetForm();
      this.modalController.dismiss({
        dismissed: false,
        passwordUpdated: true,
      });
    } catch (error: any) {
      const errorMsg =
        error?.error?.newPassword ||
        error?.error?.message ||
        'Failed to update password. Please try again.';
      this.errorMessage = errorMsg;
      this.toastService.showError(errorMsg);
    } finally {
      this.isLoading = false;
    }
  }

  /**
   * Dismiss the modal without saving
   */
  onDismiss() {
    this.modalController.dismiss({
      dismissed: true,
      passwordUpdated: false,
    });
  }
}
