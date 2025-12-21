import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { IonicModule, ModalController } from '@ionic/angular';

@Component({
  selector: 'app-icon-selector-modal',
  standalone: true,
  imports: [CommonModule, IonicModule],
  templateUrl: './icon-selector-modal.component.html',
  styleUrls: ['./icon-selector-modal.component.scss'],
})
export class IconSelectorModalComponent {
  @Input() iconNames: string[] = [];
  @Input() selectedIconName?: string;

  constructor(private modalController: ModalController) {}

  trackByName(_index: number, name: string) {
    return name;
  }

  getIconSrc(iconName: string): string {
    return `assets/icon/${iconName}.png`;
  }

  close(): void {
    this.modalController.dismiss(null, 'cancel');
  }

  select(iconName: string): void {
    this.modalController.dismiss({ iconName }, 'select');
  }
}
