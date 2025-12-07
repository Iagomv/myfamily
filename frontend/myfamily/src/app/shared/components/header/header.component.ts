import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { BackButtonComponent } from '../back-button/back-button.component';
import { addIcons } from 'ionicons';
import {
  home,
  peopleOutline,
  arrowBack,
  menu,
  close,
  homeOutline,
  person,
  personOutline,
  settingsOutline,
  logOutOutline,
  addOutline,
  searchOutline,
} from 'ionicons/icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, BackButtonComponent],
})
export class HeaderComponent implements OnInit {
  @Input() title: string = '';
  @Input() icon?: string;
  @Input() color: string = 'header-toolbar';
  @Input() showBackButton: boolean = false;
  @Input() onBack?: () => void;

  constructor() {
    addIcons({
      home,
      peopleOutline,
      arrowBack,
      menu,
      close,
      homeOutline,
      person,
      personOutline,
      settingsOutline,
      logOutOutline,
      addOutline,
      searchOutline,
    });
  }

  ngOnInit() {}

  handleBack() {
    if (this.onBack) {
      this.onBack();
    }
  }
}
