import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-invitation-code-card',
  templateUrl: './invitation-code-card.component.html',
  styleUrls: ['./invitation-code-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class InvitationCodeCardComponent {
  @Input() invitationCode: string = '';

  copyInvitationCode(): void {
    if (!this.invitationCode) {
      return;
    }
    navigator.clipboard.writeText(this.invitationCode).then(() => {
      console.log('Invitation code copied to clipboard');
    });
  }
}
