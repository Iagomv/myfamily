import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-dashboard-summary-card',
  templateUrl: './dashboard-summary-card.component.html',
  styleUrls: ['./dashboard-summary-card.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DashboardSummaryCardComponent {
  @Input() iconName: string = '';
  @Input() title: string = '';
  @Input() mainNumber: number = 0;
  @Input() mainCaption: string = '';
  @Input() rows: Array<{ label: string; value: string | number | null }> = [];
}
