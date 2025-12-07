import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';

@Component({
  selector: 'app-family-calendar',
  templateUrl: './family-calendar.component.html',
  styleUrls: ['./family-calendar.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, HeaderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyCalendarComponent implements OnInit {
  constructor() {}
  ngOnInit() {}
}
