import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from '../pagination/pagination.module';
import { NgxBootstrapIconsModule, allIcons } from 'ngx-bootstrap-icons';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { FlexmonsterPivotModule } from 'ng-flexmonster';
import { StampeComponent } from './stampe.component';
import { StampeRoutingModule } from './stampe-routing.module';
import { ReportEGraficiComponent } from './bi/bi.component';
import { StampebarComponent } from './stampebar.component';
@NgModule({
  imports: [
    CommonModule,
    NgSelectModule,
    StampeRoutingModule,
    TranslateModule,
    FormsModule,
    NgbDatepickerModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    PaginationModule,
    FlexmonsterPivotModule,
  ],
  declarations: [StampeComponent, ReportEGraficiComponent, StampebarComponent],
})
export class StampeModule {}
