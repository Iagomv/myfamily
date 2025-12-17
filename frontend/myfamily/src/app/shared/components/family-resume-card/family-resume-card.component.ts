import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonButton,
  IonIcon,
} from '@ionic/angular/standalone';
import { Family } from '../../interfaces/family.interface';

@Component({
  selector: 'app-family-resume-card',
  templateUrl: './family-resume-card.component.html',
  styleUrls: ['./family-resume-card.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonButton,
    IonIcon,
  ],
})
export class FamilyResumeCardComponent {
  @Input() family!: Family;
  @Input() familyIcon?: string; // Optional image URL
  @Output() viewClicked = new EventEmitter<Family>();
  @Output() leaveClicked = new EventEmitter<Family>();

  onViewClick() {
    this.viewClicked.emit(this.family);
  }

  onLeaveClick(event: Event) {
    event.stopPropagation();
    this.leaveClicked.emit(this.family);
  }
}
