import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
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
  imports: [CommonModule, IonicModule],
})
export class BottomTabBarComponent implements OnInit {
  tabs: TabItem[] = [
    { label: 'Dashboard', icon: 'home', route: '/home/family-dashboard' },
    { label: 'Shopping', icon: 'cart', route: '/home/family-shopping' },
    { label: 'Calendar', icon: 'calendar', route: '/home/family-calendar' },
    { label: 'Documents', icon: 'folder', route: '/home/family-docs' },
    { label: 'Profile', icon: 'person', route: '/home/profile' },
  ];

  activeTab: string = 'Dashboard';

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
