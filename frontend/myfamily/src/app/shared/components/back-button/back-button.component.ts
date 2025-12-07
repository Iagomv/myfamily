import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-back-button',
  templateUrl: './back-button.component.html',
  styleUrls: ['./back-button.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class BackButtonComponent {
  @Input() onBack: () => void = () => {};

  handleBack() {
    this.onBack();
  }
}
