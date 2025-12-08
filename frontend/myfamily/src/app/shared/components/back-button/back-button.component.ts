import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonButton, IonIcon } from '@ionic/angular/standalone';

@Component({
  selector: 'app-back-button',
  templateUrl: './back-button.component.html',
  styleUrls: ['./back-button.component.scss'],
  standalone: true,
  imports: [CommonModule, IonButton, IonIcon],
})
export class BackButtonComponent {
  @Input() onBack: () => void = () => {};

  handleBack() {
    this.onBack();
  }
}
