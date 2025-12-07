import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { IonicModule } from '@ionic/angular';
import { BottomTabBarComponent } from '../shared/components/bottom-tab-bar/bottom-tab-bar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [RouterOutlet, IonicModule, BottomTabBarComponent, CommonModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class HomePage implements OnInit {
  showBottomTabBar: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    // Check initial route
    this.updateTabBarVisibility(this.router.url);

    // Listen to route changes
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.updateTabBarVisibility(event.url);
      });
  }

  updateTabBarVisibility(url: string) {
    // Hide bottom tab bar on family-selection page
    this.showBottomTabBar = !url.includes('family-selection');
  }
}
