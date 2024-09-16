import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { NgSelectModule } from "@ng-select/ng-select";
import { ProgrammazioneRoutingModule } from "./programmazione-routing.module";
import { TranslateModule } from "@ngx-translate/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PaginationModule } from "../pagination/pagination.module";
import { NgxBootstrapIconsModule, allIcons } from "ngx-bootstrap-icons";
import { SchedaProgrammazionebarComponent } from './schede/scheda-programmazionebar/scheda-programmazionebar.component';
import { AutocompleteLibModule } from "angular-ng-autocomplete";
import { NgbDatepickerModule, NgbModalModule } from "@ng-bootstrap/ng-bootstrap";

import { EditorModule } from "@tinymce/tinymce-angular";
import { AggiunginodoComponent } from "./modal/aggiunginodo/aggiunginodo.component";
import { AggiungiindicatoreComponent } from "./modal/aggiungiindicatore/aggiungiindicatore.component";

import { ForzaturapopupComponent } from './modal/forzatura/forzaturapopup.component';
import { SafePipe } from "./modal/forzatura/safe.pipe";

import { ElementoAlberoComponent } from './schede/scheda-albero/elemento-albero/elemento-albero.component';
import { SchedaConsuntivazioneComponent } from './schede/scheda-consuntivazione/scheda-consuntivazione.component';
import { SchedaAlberoComponent } from './schede/scheda-albero/scheda-albero.component';
import { ProgrammazioneComponent } from './programmazione-container/programmazione.component';
import { SchedaIndicatoriComponent } from './schede/scheda-indicatori/scheda-indicatori.component';
import { SchedaPesaturaComponent } from './schede/scheda-pesatura/scheda-pesatura.component';
import { SchedaRicercaComponent } from './schede/scheda-ricerca/scheda-ricerca.component';
import { SchedaNodoComponent } from './schede/scheda-nodo/scheda-nodo.component';
import { SchedaTitoloComponent } from './schede/scheda-titolo/scheda-titolo.component';

import { AggiungiNotePopupComponent} from "./modal/aggiungi-note/aggiungi-note-popup.component";

import { ModificapopupComponent } from "./modal/modificapopup/modificapopup.component";
import { IntroduzioneComponent } from "./introduzione/introduzione.component";
import { PesaturapopupComponent } from "./modal/pesaturapopup/pesaturapopup.component";
import { RisultatopopupComponent } from "./modal/risultato/risultatopopup.component";
import { NoteassessoriComponent } from "./modal/noteassessori/noteassessori.component";
import { SchedaRisorseComponent } from './schede/scheda-risorse/scheda-risorse.component';
import { AggiungirisorsaProgComponent } from "./modal/aggiungirisorsaprog/aggiungirisorsaprog.component";
import { ForzaturaNodoPopupComponent} from "./modal/forzatura-nodo/forzatura-nodo-popup.component";
import { SpostaNodoComponent } from "./modal/sposta-nodo/sposta-nodo.component";


@NgModule({
  imports: [
    CommonModule,
    NgSelectModule,
    EditorModule,
    NgSelectModule,
    ProgrammazioneRoutingModule,
    TranslateModule,
    FormsModule,
    NgbModalModule,
    AutocompleteLibModule,
    NgbDatepickerModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    PaginationModule,
  ],
  declarations: [
    ElementoAlberoComponent,
    SafePipe,
    ModificapopupComponent,
    ForzaturapopupComponent,
    SchedaConsuntivazioneComponent,
    IntroduzioneComponent,
    AggiunginodoComponent,
    SchedaPesaturaComponent,
    PesaturapopupComponent,
    RisultatopopupComponent,
    SchedaAlberoComponent,
    ProgrammazioneComponent,
    NoteassessoriComponent,
    SchedaIndicatoriComponent,
    AggiungiindicatoreComponent,
    SchedaRicercaComponent,
    SchedaAlberoComponent,
    SchedaNodoComponent,
    SchedaTitoloComponent,
    ForzaturaNodoPopupComponent,
    AggiungiNotePopupComponent,
    SchedaProgrammazionebarComponent,
    SchedaRisorseComponent,
    AggiungirisorsaProgComponent,
    SpostaNodoComponent
  ],
})
export class ProgrammazioneModule {}
