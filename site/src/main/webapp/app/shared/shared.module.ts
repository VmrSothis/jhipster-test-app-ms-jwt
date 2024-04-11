import { LOCALE_ID, NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import localeEsAr from '@angular/common/locales/es-AR'; 
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

import FindLanguageFromKeyPipe from './language/find-language-from-key.pipe';
import TranslateDirective from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';

registerLocaleData(localeEsAr);

/**
 * Application wide Module
 */
@NgModule({
  imports: [AlertComponent, AlertErrorComponent, FindLanguageFromKeyPipe, TranslateDirective],
  exports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    AlertComponent,
    AlertErrorComponent,
    TranslateModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
  ],
  providers: [ { provide: LOCALE_ID, useValue: 'es-Ar' } ],
})
export default class SharedModule {}
