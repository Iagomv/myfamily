import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FamilyMember } from '../../interfaces/family-member.interface';

@Component({
  selector: 'app-member-card',
  templateUrl: './member-card.component.html',
  styleUrls: ['./member-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class MemberCardComponent {
  @Input() member!: FamilyMember;
}
