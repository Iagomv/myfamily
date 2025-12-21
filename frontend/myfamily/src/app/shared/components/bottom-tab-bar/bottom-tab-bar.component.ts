import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonIcon } from '@ionic/angular/standalone';
import { Router, NavigationEnd } from '@angular/router';
import { addIcons } from 'ionicons';
import { home, cart, calendar, folder, person } from 'ionicons/icons';
import { filter } from 'rxjs/operators';

interface TabItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-bottom-tab-bar',
  templateUrl: './bottom-tab-bar.component.html',
  styleUrls: ['./bottom-tab-bar.component.scss'],
  standalone: true,
  imports: [CommonModule, IonIcon],
})
export class BottomTabBarComponent implements OnInit {
  tabs: TabItem[] = [
    { label: 'Casa', icon: 'home', route: '/home/family-dashboard' },
    { label: 'Compras', icon: 'cart', route: '/home/family-shopping' },
    { label: 'Calendario', icon: 'calendar', route: '/home/family-calendar' },
    { label: 'Documentos', icon: 'folder', route: '/home/family-docs' },
    { label: 'Perfil', icon: 'person', route: '/home/profile' },
  ];

  activeTab: string = 'Casa';

  constructor(private router: Router) {
    addIcons({ home, cart, calendar, folder, person });
  }

  ngOnInit() {
    // Set active tab based on current route
    this.updateActiveTab(this.router.url);

    // Listen to route changes
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.updateActiveTab(event.url);
      });
  }

  updateActiveTab(url: string) {
    const activeTabItem = this.tabs.find((tab) => url.includes(tab.route));
    if (activeTabItem) {
      this.activeTab = activeTabItem.label;
    }
  }

  selectTab(tab: TabItem) {
    this.activeTab = tab.label;
    this.router.navigate([tab.route]);
  }
}
