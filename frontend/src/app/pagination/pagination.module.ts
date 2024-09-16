import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup, Validators, FormBuilder } from "@angular/forms";
import { TranslateService, TranslateModule, TranslateLoader  } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { AuthGuard } from '../_helpers/auth.guard';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { PaginationComponent } from "../pagination";



@NgModule({
  imports: [
CommonModule,
        ReactiveFormsModule,
        FormsModule,
        HttpClientModule,
        TranslateModule,
        FormsModule, ReactiveFormsModule,
  ],
  declarations: [
                 PaginationComponent,
                 ],
  exports: [ TranslateModule, PaginationComponent]
})
export class PaginationModule { }
