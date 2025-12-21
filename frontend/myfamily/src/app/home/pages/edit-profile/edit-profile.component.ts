import { CommonModule } from '@angular/common';
import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router } from '@angular/router';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { FamilyService } from 'src/app/shared/services/family.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { HeaderComponent } from 'src/app/shared/components/header/header.component';
import {
  ProfileInfo,
  UserUpdateRequest,
} from 'src/app/shared/interfaces/profile.interface';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, ReactiveFormsModule, HeaderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class EditProfileComponent implements OnInit {
  isLoading = false;
  isSaving = false;

  form = this.formBuilder.group({
    username: ['', [Validators.required]],
    familyMemberName: [''],
    email: ['', [Validators.required, Validators.email]],
    birthdate: [''],
  });

  constructor(
    private formBuilder: FormBuilder,
    private apiCallService: ApiCallService,
    private familyService: FamilyService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfileInfo();
  }

  goBack(): void {
    this.router.navigate(['/home/profile']);
  }

  private loadProfileInfo(): void {
    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('No se pudo determinar la familia actual.');
      return;
    }

    this.isLoading = true;
    this.apiCallService.getProfileInfo(familyId).subscribe({
      next: (profileInfo: ProfileInfo) => {
        this.form.patchValue({
          username: profileInfo.username ?? '',
          familyMemberName: profileInfo.familyMemberName ?? '',
          email: profileInfo.email ?? '',
          birthdate: this.normalizeDateForInput(profileInfo.birthdate),
        });
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.toastService.showError('No se pudo cargar tu perfil.');
      },
    });
  }

  private normalizeDateForInput(value: string | null | undefined): string {
    if (!value) return '';
    // Accept ISO timestamps and keep only YYYY-MM-DD for input[type=date]
    const match = String(value).match(/^\d{4}-\d{2}-\d{2}/);
    return match ? match[0] : '';
  }

  private buildUpdateRequest(): UserUpdateRequest {
    const raw = this.form.getRawValue();
    const body: UserUpdateRequest = {};

    const username = (raw.username ?? '').trim();
    const familyMemberName = (raw.familyMemberName ?? '').trim();
    const email = (raw.email ?? '').trim();
    const birthdate = (raw.birthdate ?? '').trim();

    if (username) body.username = username;
    if (familyMemberName) body.familyMemberName = familyMemberName;
    if (email) body.email = email;
    if (birthdate) body.birthdate = birthdate;

    return body;
  }

  submit(): void {
    if (this.isSaving) return;

    if (this.form.invalid) {
      this.toastService.showError('Revisa los campos del formulario.');
      this.form.markAllAsTouched();
      return;
    }

    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('No se pudo determinar la familia actual.');
      return;
    }

    const body = this.buildUpdateRequest();

    this.isSaving = true;
    this.apiCallService.updateUserInfo(body, familyId).subscribe({
      next: () => {
        this.isSaving = false;
        this.toastService.showSuccess('Perfil actualizado.', 1200);
        this.goBack();
      },
      error: (error) => {
        this.isSaving = false;
        const message =
          error?.error?.message || 'Error al actualizar el perfil.';
        this.toastService.showError(message);
      },
    });
  }
}
