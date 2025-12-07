import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ApiCallService } from '../../../shared/services/api-call.service';
import { Router } from '@angular/router';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Family } from 'src/app/shared/interfaces/family.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ModalController } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { CreateFamilyModalComponent } from './create-family-modal/create-family-modal.component';
import { JoinFamilyModalComponent } from './join-family-modal/join-family-modal.component';
import { HeaderComponent } from '../../../shared/components/header/header.component';
import { FamilyResumeCardComponent } from '../../../shared/components/family-resume-card/family-resume-card.component';
import { FamilyService } from '../../../shared/services/family.service';

@Component({
  selector: 'app-family-selection',
  templateUrl: './family-selection.component.html',
  styleUrls: ['./family-selection.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule,
    HeaderComponent,
    FamilyResumeCardComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilySelectionComponent implements OnInit {
  families: Family[] = [];

  constructor(
    private apiCallService: ApiCallService,
    private toastService: ToastService,
    private modalController: ModalController,
    private router: Router,
    private familyService: FamilyService // Inject FamilyService
  ) {}

  ngOnInit() {
    this.loadFamilies();
  }

  loadFamilies() {
    this.apiCallService.getMyFamilies().subscribe({
      next: (response: any) => {
        if (Array.isArray(response)) {
          this.families = response;
        } else if (response?.body && Array.isArray(response.body)) {
          this.families = response.body;
        }
        console.log('Families loaded:', this.families);
      },
      error: (error) => {
        console.error('Error loading families:', error);
        this.families = [];
      },
    });
  }

  async openCreateFamilyModal() {
    const modal = await this.modalController.create({
      component: CreateFamilyModalComponent,
      mode: 'ios',
    });
    await modal.present();

    const { data, role } = await modal.onDidDismiss();

    if (role === 'submit' && data?.familyName) {
      this.createFamily(data.familyName);
    }
  }

  async openJoinFamilyModal() {
    const modal = await this.modalController.create({
      component: JoinFamilyModalComponent,
      mode: 'ios',
    });
    await modal.present();

    const { data, role } = await modal.onDidDismiss();

    if (role === 'submit' && data?.invitationCode) {
      this.joinFamily(data.invitationCode);
    }
  }

  private createFamily(familyName: string) {
    this.apiCallService.createFamily(familyName).subscribe({
      next: (family) => {
        this.toastService.showSuccess('Family created successfully!', 1000);
        this.loadFamilies();
      },
      error: (error) => {
        this.toastService.showError(
          'Error creating family: ' + error.error.message
        );
      },
    });
  }

  private joinFamily(code: string) {
    this.apiCallService.joinFamily(code).subscribe({
      next: (family) => {
        this.toastService.showSuccess('Joined family successfully!', 1000);
        this.loadFamilies();
      },
      error: (error) => {
        this.toastService.showError(
          'Error joining family: ' + error.error.message
        );
      },
    });
  }

  goToFamilyDashboard(family: Family) {
    this.router
      .navigate(['/home/family-dashboard'], {
        state: { family: family },
      })
      .then((success) => {
        console.log('Navigation success:', success);
      })
      .catch((error) => {
        console.error('Navigation error:', error);
      });
  }

  onFamilyCardViewClicked(family: Family) {
    this.familyService.setSelectedFamily(family); // Save the selected family globally
    this.goToFamilyDashboard(family);
  }
}
