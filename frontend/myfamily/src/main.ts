import { bootstrapApplication } from '@angular/platform-browser';
import { LOCALE_ID } from '@angular/core';
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
  cameraOutline,
  createOutline,
  key,
  people,
  statsChartOutline,
  shareSocialOutline,
  copyOutline,
  personCircleOutline,
  eyeOutline,
  eyeOffOutline,
} from 'ionicons/icons';

import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { TokenInterceptor } from './app/shared/interceptors/token.interceptor';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';

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
  'camera-outline': cameraOutline,
  'create-outline': createOutline,
  key: key,
  people: people,
  'stats-chart-outline': statsChartOutline,
  'share-social-outline': shareSocialOutline,
  'copy-outline': copyOutline,
  'person-circle-outline': personCircleOutline,
  'eye-outline': eyeOutline,
  'eye-off-outline': eyeOffOutline,
});

// Register Spanish locale data used by Angular pipes (DatePipe, etc.)
registerLocaleData(localeEs, 'es-ES');

bootstrapApplication(AppComponent, {
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    provideIonicAngular(),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    provideHttpClient(),
    { provide: LOCALE_ID, useValue: 'es-ES' },
  ],
});
