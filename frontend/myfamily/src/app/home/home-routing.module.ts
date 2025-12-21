import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePage } from './home.page';

const routes: Routes = [
  {
    path: '',
    component: HomePage,
    children: [
      {
        path: 'family-selection',
        loadComponent: () =>
          import('./pages/family-selection/family-selection.component').then(
            (m) => m.FamilySelectionComponent
          ),
      },
      {
        path: 'family-dashboard',
        loadComponent: () =>
          import('./pages/family-dashboard/family-dashboard.component').then(
            (m) => m.FamilyDashboardComponent
          ),
      },
      {
        path: 'family-calendar',
        loadComponent: () =>
          import('./pages/family-calendar/family-calendar.component').then(
            (m) => m.FamilyCalendarComponent
          ),
      },
      {
        path: 'family-shopping',
        loadComponent: () =>
          import('./pages/family-shopping/family-shopping.component').then(
            (m) => m.FamilyShoppingComponent
          ),
      },
      {
        path: 'family-docs',
        loadComponent: () =>
          import('./pages/family-docs/family-docs.component').then(
            (m) => m.FamilyDocsComponent
          ),
      },
      {
        path: 'profile',
        loadComponent: () =>
          import('./pages/profile/profile.component').then(
            (m) => m.ProfileComponent
          ),
      },
      {
        path: 'edit-profile',
        loadComponent: () =>
          import('./pages/edit-profile/edit-profile.component').then(
            (m) => m.EditProfileComponent
          ),
      },
      {
        path: '',
        redirectTo: 'family-selection',
        pathMatch: 'full',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
