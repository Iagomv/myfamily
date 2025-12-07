import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ModalController } from '@ionic/angular';
import { HeaderComponent } from '../../../../shared/components/header/header.component';

@Component({
  selector: 'app-join-family-modal',
  templateUrl: './join-family-modal.component.html',
  styleUrls: ['./join-family-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, HeaderComponent],
})
export class JoinFamilyModalComponent {
  invitationCode: string = '';

  constructor(private modalController: ModalController) {}

  cancel() {
    this.modalController.dismiss(null, 'cancel');
  }

  async pasteFromClipboard() {
    try {
      const text = await navigator.clipboard.readText();
      this.invitationCode = text.trim();
    } catch (err) {
      console.error('Failed to read clipboard:', err);
    }
  }

  submit() {
    if (this.validateInvitationCode()) {
      this.modalController.dismiss(
        { invitationCode: this.invitationCode },
        'submit'
      );
    }
  }

  private validateInvitationCode(): boolean {
    if (!this.invitationCode || this.invitationCode.trim().length === 0) {
      return false;
    }
    if (this.invitationCode.length !== 8) {
      return false;
    }
    return true;
  }

  isSubmitDisabled(): boolean {
    return !this.validateInvitationCode();
  }
}
