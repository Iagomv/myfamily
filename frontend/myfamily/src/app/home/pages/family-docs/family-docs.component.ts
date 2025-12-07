import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { HeaderComponent } from '../../../shared/components/header/header.component';

@Component({
  selector: 'app-family-docs',
  templateUrl: './family-docs.component.html',
  styleUrls: ['./family-docs.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, HeaderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FamilyDocsComponent implements OnInit {
  constructor() {}
  ngOnInit() {}
}
