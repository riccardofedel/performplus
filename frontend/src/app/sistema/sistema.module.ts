import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { NgSelectModule } from '@ng-select/ng-select';
import { SistemaRoutingModule } from './sistema-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from '../pagination/pagination.module';
import { NgxBootstrapIconsModule, allIcons } from 'ngx-bootstrap-icons';
import { SistemabarComponent } from './sistemabar.component';
import { CruscottoComponent } from './cruscotto/cruscotto.component';
import { UtentiComponent } from './utenti/utenti.component';
import { UtentidettaglioComponent } from './utenti/utentidettaglio/utentidettaglio.component';
import { IndicatoriComponent } from './indicatori/indicatori.component';
import { IndicatoridettaglioComponent } from './indicatori/indicatoridettaglio/indicatoridettaglio.component';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { FlexmonsterPivotModule } from 'ng-flexmonster';
import { BIComponent } from './bi/bi.component';
import { UtentipasswordComponent } from './utenti/utentipassword/utentipassword.component';
import { ArrowModule } from '../arrow/arrow.module';
import { AnagraficaComponent } from './anagrafiche/anagrafica.component';
import { AnagraficabarComponent } from './anagrafiche/anagraficabar.component';
import { AnagraficadettaglioComponent } from './anagrafiche/anagraficadettaglio/anagraficadettaglio.component';
import { TemplateReports } from '../_services/bi.reports';
import { CsvDialogComponent } from './bi/csv/csv.dialog.component';
@NgModule({
  imports: [
    CommonModule,
    NgSelectModule,
    SistemaRoutingModule,
    TranslateModule,
    FormsModule,
    ArrowModule,
    NgbDatepickerModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    PaginationModule,
    FlexmonsterPivotModule,
  ],
  declarations: [
    CruscottoComponent,
    AnagraficaComponent,
    AnagraficadettaglioComponent,
    SistemabarComponent,
    AnagraficabarComponent,
    UtentiComponent,
    SistemabarComponent,
    UtentipasswordComponent,
    UtentidettaglioComponent,
    IndicatoriComponent,
    IndicatoridettaglioComponent,
    BIComponent,
    CsvDialogComponent,
  ],
})
export class SistemaModule {}
