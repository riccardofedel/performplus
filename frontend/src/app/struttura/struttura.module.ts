import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { NgSelectModule } from "@ng-select/ng-select";
import { StrutturaRoutingModule } from "./struttura-routing.module";
import { TranslateModule } from "@ngx-translate/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PaginationModule } from "../pagination/pagination.module";
import { NgxBootstrapIconsModule, allIcons } from "ngx-bootstrap-icons";
import { StrutturabarComponent } from "./strutturabar.component";
import { AmministratoriComponent } from "./amministratori/amministratori.component";
import { AmministratoridettaglioComponent } from "./amministratori/amministratoridettaglio/amministratoridettaglio.component";
import { NgbDatepickerModule, NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { RisorsedettaglioComponent } from "./risorse/risorsedettaglio/risorsedettaglio.component";
import { RisorseComponent } from "./risorse/risorse.component";
import { OrganigrammaComponent } from "./organigramma/organigramma.component";
import { AggiungistrutturaComponent } from "./organigramma/model/aggiungistruttura/aggiungistruttura.component";
import { AutocompleteLibModule } from "angular-ng-autocomplete";
import { AggiungirisorsaComponent } from "./organigramma/model/aggiungirisorsa/aggiungirisorsa.component";
import { DisponibilitapopupComponent } from "./organigramma/model/disponibilitapopup/disponibilitapopup.component";
import { SchedaRicercaComponent } from "./organigramma/schede/scheda-ricerca/scheda-ricerca.component";
import { SchedaRisorseComponent } from "./organigramma/schede/scheda-risorse/scheda-risorse.component";
import { SchedaTitoloComponent } from "./organigramma/schede/scheda-titolo/scheda-titolo.component";
import { ElementoAlberoComponent } from './organigramma/schede/scheda-albero/elemento-albero/elemento-albero.component';
import { SchedaAlberoComponent } from './organigramma/schede/scheda-albero/scheda-albero.component';
import { SchedaStrutturaComponent } from "./organigramma/schede/scheda-struttura/scheda-struttura.component";
import { ArrowModule } from "../arrow/arrow.module";

@NgModule({
  imports: [
    CommonModule,
    NgSelectModule,
    StrutturaRoutingModule,
    TranslateModule,
    FormsModule,
    NgbModule,
    AutocompleteLibModule,
    NgbDatepickerModule,
    NgxBootstrapIconsModule.pick(allIcons),
    ReactiveFormsModule,
    PaginationModule,
    ArrowModule,
  ],
  declarations: [
    DisponibilitapopupComponent,
    ElementoAlberoComponent,
    StrutturabarComponent,
    SchedaRisorseComponent,
    AmministratoriComponent,
    AmministratoridettaglioComponent,
    RisorseComponent,
    RisorsedettaglioComponent,
    SchedaAlberoComponent,
    OrganigrammaComponent,
    AggiungistrutturaComponent,
    AggiungirisorsaComponent,
    SchedaRicercaComponent,
    SchedaTitoloComponent,
    SchedaStrutturaComponent,
  ],
})
export class StrutturaModule {}
