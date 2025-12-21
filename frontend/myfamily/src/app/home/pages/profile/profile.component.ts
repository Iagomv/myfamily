import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { ApiCallService } from 'src/app/shared/services/api-call.service';
import { ProfileInfo } from 'src/app/shared/interfaces/profile.interface';
import { FamilyService } from 'src/app/shared/services/family.service';
import { ModalController } from '@ionic/angular';
import { IconSelectorModalComponent } from 'src/app/shared/components/icon-selector-modal/icon-selector-modal.component';
import { ToastService } from 'src/app/shared/services/toast.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { FamilyMemberIconUpdateRequest } from 'src/app/shared/interfaces/family-member.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, HeaderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ProfileComponent implements OnInit {
  profileInfo!: ProfileInfo;
  avatarUrl = 'assets/icon/default.png';
  isLoading = false;
  loadError = false;

  readonly availableIconNames = ['chibi1', 'chibi2', 'chibi3', 'chibi4'];

  constructor(
    private apiCallService: ApiCallService,
    private familyService: FamilyService,
    private modalController: ModalController,
    private toastService: ToastService,
    private securityService: SecurityService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getProfileInfo();
  }

  private getProfileInfo() {
    const familyId = this.familyService.getFamilyId();

    if (!familyId) {
      this.isLoading = false;
      this.loadError = true;
      return;
    }

    this.isLoading = true;
    this.loadError = false;

    this.apiCallService.getProfileInfo(familyId).subscribe({
      next: (profileInfo: any) => {
        this.profileInfo = profileInfo as ProfileInfo;
        this.avatarUrl = this.profileInfo.familyMemberIcon
          ? `assets/icon/${this.profileInfo.familyMemberIcon}.png`
          : 'assets/icon/default.png';
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.loadError = true;
      },
    });
  }

  onAvatarError(event: Event): void {
    const img = event.target as HTMLImageElement | null;
    if (!img) return;

    const fallback = 'assets/icon/default.png';
    if (img.src.endsWith(fallback)) return;

    img.src = fallback;
  }

  async openIconPicker(): Promise<void> {
    if (!this.profileInfo) return;

    const modal = await this.modalController.create({
      component: IconSelectorModalComponent,
      mode: 'ios',
      componentProps: {
        iconNames: this.availableIconNames,
        selectedIconName: this.profileInfo.familyMemberIcon,
      },
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();
    if (role !== 'select' || !data?.iconName) return;

    const selectedIconName = String(data.iconName);
    if (selectedIconName === this.profileInfo.familyMemberIcon) return;

    const familyId = this.familyService.getFamilyId();
    if (!familyId) {
      this.toastService.showError('No se pudo determinar la familia actual.');
      return;
    }

    const userId =
      this.securityService.getUserIdFromToken() ?? this.profileInfo.userId;
    if (!userId) {
      this.toastService.showError('No se pudo determinar el usuario actual.');
      return;
    }

    const body: FamilyMemberIconUpdateRequest = {
      familyId,
      userId,
      newIconName: selectedIconName,
    };

    this.apiCallService.updateSelectedIcon(body).subscribe({
      next: () => {
        this.profileInfo.familyMemberIcon = selectedIconName;
        this.avatarUrl = `assets/icon/${selectedIconName}.png`;
      },
      error: (error) => {
        const message =
          error?.error?.message || 'Error al actualizar el avatar.';
        this.toastService.showError(message);
      },
    });
  }

  goToEditProfile(): void {
    this.router.navigate(['/home/edit-profile']);
  }
}
