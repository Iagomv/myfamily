import { bootstrapApplication } from '@angular/platform-browser';
import {
  RouteReuseStrategy,
  provideRouter,
  withPreloading,
  PreloadAllModules,
} from '@angular/router';
import {
  IonicRouteStrategy,
  provideIonicAngular,
} from '@ionic/angular/standalone';
import {
  provideHttpClient,
  withInterceptors,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { addIcons } from 'ionicons';
import {
  arrowBack,
  peopleOutline,
  close,
  heart,
  add,
  person,
  home,
  settings,
  chevronBack,
  chevronForward,
  trashOutline,
  cartOutline,
  checkboxOutline,
  checkmarkCircleOutline,
  checkmark,
  calendarOutline,
  key,
  people,
} from 'ionicons/icons';

import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { TokenInterceptor } from './app/shared/interceptors/token.interceptor';

// Register Ionicons for standalone mode
addIcons({
  'arrow-back': arrowBack,
  'people-outline': peopleOutline,
  close: close,
  heart: heart,
  add: add,
  person: person,
  home: home,
  settings: settings,
  'chevron-back': chevronBack,
  'chevron-forward': chevronForward,
  'trash-outline': trashOutline,
  'cart-outline': cartOutline,
  'checkbox-outline': checkboxOutline,
  'checkmark-circle-outline': checkmarkCircleOutline,
  checkmark: checkmark,
  'calendar-outline': calendarOutline,
  key: key,
  people: people,
});

bootstrapApplication(AppComponent, {
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    provideIonicAngular(),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    provideHttpClient(),
  ],
});
