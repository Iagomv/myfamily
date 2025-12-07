import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { Family } from '../../../shared/interfaces/family.interface';
import { HeaderComponent } from '../../../shared/components/header/header.component';

@Component({
  selector: 'app-family-dashboard',
  templateUrl: './family-dashboard.component.html',
  styleUrls: ['./family-dashboard.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, HeaderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyDashboardComponent implements OnInit {
  family: Family | undefined;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    this.family = navigation?.extras?.state?.['family'];
  }

  ngOnInit() {}
}
