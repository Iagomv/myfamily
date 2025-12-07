import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { Family } from '../../interfaces/family.interface';

@Component({
  selector: 'app-family-resume-card',
  templateUrl: './family-resume-card.component.html',
  styleUrls: ['./family-resume-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class FamilyResumeCardComponent {
  @Input() family!: Family;
  @Output() viewClicked = new EventEmitter<Family>();

  onViewClick() {
    this.viewClicked.emit(this.family);
  }
}
