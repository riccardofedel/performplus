import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { NgSelectModule } from '@ng-select/ng-select';
import { PerformanceRoutingModule } from './performance-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from '../pagination/pagination.module';
import { NgxBootstrapIconsModule, allIcons } from 'ngx-bootstrap-icons';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { RegistrazioniComponent } from './registrazioni/registrazioni.component';
import { RegolamentoComponent } from './regolamento/regolamento.component';
import { PerformancebarComponent } from './performancebar.component';
import { PrioritaComponent } from './priorita/priorita.component';
import { ValutazioniComponent } from './valutazioni/valutazioni.component';
import { RegistrazionidettaglioComponent } from './registrazioni/registrazionidettaglio/registrazionidettaglio.component';
import { RegolamentodettaglioComponent } from './regolamento/regolamentodettaglio/regolamentodettaglio.component';
import { QuestionariComponent } from './questionari/questionari.component';

import { ElementoAlberoComponent } from './questionari/schede/scheda-albero/elemento-albero/elemento-albero.component';
import { SchedaAlberoComponent } from './questionari/schede/scheda-albero/scheda-albero.component';
import { SchedaRicercaComponent } from './questionari/schede/scheda-ricerca/scheda-ricerca.component';
import { SchedaQuestionarioComponent } from './questionari/schede/scheda-questionario/scheda-questionario.component';
import { SchedaTitoloComponent } from './questionari/schede/scheda-titolo/scheda-titolo.component';
import { ModificaPesoQuestionarioComponent } from './questionari/modal/modifica-peso/modifica-peso.component';
import { AggiungiquestionarioComponent } from './questionari/modal/aggiungiquestionario/aggiungiquestionario.component';
import { ModificaPrioritaComponent } from './priorita/pesaturapopup/pesaturapopup.component';
import { PubblicapopupComponent } from './valutazioni/pubblicazione-popup/pubblicazione-popup.component';
import { SchedaValutatoComponent } from './valutazioni/scheda-valutato/scheda-valutato.component';
import { ValutazioneComponent } from './valutazione/valutazione.component';
import { AccettazionepopupComponent } from './valutazione/accettazione-popup/accettazione-popup.component';


@NgModule({
  imports: [
    CommonModule,
    NgSelectModule,
    PerformanceRoutingModule,
    TranslateModule,
    FormsModule,
    AutocompleteLibModule,
    NgbDatepickerModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    PaginationModule,
  ],
  declarations: [
    RegistrazioniComponent,
    RegolamentoComponent,
    PerformancebarComponent,
    PrioritaComponent,
    ValutazioniComponent,
    RegistrazionidettaglioComponent,
    RegolamentodettaglioComponent,
    QuestionariComponent,
    AggiungiquestionarioComponent,
    ElementoAlberoComponent,
    SchedaAlberoComponent,
    SchedaRicercaComponent,
    SchedaQuestionarioComponent,
    SchedaTitoloComponent,
    ModificaPesoQuestionarioComponent,
    ModificaPrioritaComponent,
    PubblicapopupComponent,
    SchedaValutatoComponent,
    ValutazioneComponent,
    AccettazionepopupComponent,

  ],
})
export class PerformanceModule {}
