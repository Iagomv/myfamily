import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonButtons,
  IonIcon,
} from '@ionic/angular/standalone';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    BackButtonComponent,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonIcon,
  ],
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
