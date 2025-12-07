import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ModalController } from '@ionic/angular';
import { HeaderComponent } from '../../../../shared/components/header/header.component';

@Component({
  selector: 'app-create-family-modal',
  templateUrl: './create-family-modal.component.html',
  styleUrls: ['./create-family-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, HeaderComponent],
})
export class CreateFamilyModalComponent {
  familyName: string = '';

  constructor(private modalController: ModalController) {}

  dismiss() {
    this.modalController.dismiss();
  }

  cancel() {
    this.modalController.dismiss(null, 'cancel');
  }

  submit() {
    if (this.validateFamilyName()) {
      this.modalController.dismiss({ familyName: this.familyName }, 'submit');
    }
  }

  private validateFamilyName(): boolean {
    if (!this.familyName || this.familyName.trim().length === 0) {
      return false;
    }
    if (this.familyName.length < 3 || this.familyName.length > 20) {
      return false;
    }
    return true;
  }

  isSubmitDisabled(): boolean {
    return !this.validateFamilyName();
  }
}
