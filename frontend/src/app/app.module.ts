import { LOCALE_ID, NgModule, ɵDEFAULT_LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { ErrorInterceptor } from './_helpers/error.interceptor';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { NgxBootstrapIconsModule, allIcons } from 'ngx-bootstrap-icons';
import { FlexmonsterPivotModule } from 'ng-flexmonster';
import { NgSelectModule } from '@ng-select/ng-select';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
import { environment } from 'src/environments/environment';
import { ApppasswordComponent } from './apppassword/apppassword.component';
import localeIt from '@angular/common/locales/it';
import { registerLocaleData } from '@angular/common';
import { ExcelService } from './_services/excel.service';
import { SSOInterceptor } from './_helpers/sso.interceptor';
import { LoginSSOComponent } from './login-sso/login-sso.component';
import { LogoutComponent } from './logout/logout.component';
registerLocaleData(localeIt);
// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new MultiTranslateHttpLoader(http, [
      {prefix: "./assets/i18n/", suffix: ".json"},
      {prefix: "./assets/i18n/", suffix: `.${environment.codice_ente}.json`},
  ]);
  //return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginSSOComponent,
    LogoutComponent,
    HomeComponent,
    ApppasswordComponent,
  ],
  imports: [
    BrowserModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    NgSelectModule,
    HttpClientModule,
    FlexmonsterPivotModule,
    TranslateModule.forRoot({
      defaultLanguage: "it",
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: SSOInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: LOCALE_ID, useValue: "it" },
    ExcelService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
