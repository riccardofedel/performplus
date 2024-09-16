import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegistrazioniComponent } from './registrazioni/registrazioni.component';
import { RegolamentoComponent } from './regolamento/regolamento.component';
import { PrioritaComponent } from './priorita/priorita.component';
import { ValutazioniComponent } from './valutazioni/valutazioni.component';
import { RegistrazionidettaglioComponent } from './registrazioni/registrazionidettaglio/registrazionidettaglio.component';
import { RegolamentodettaglioComponent } from './regolamento/regolamentodettaglio/regolamentodettaglio.component';
import { QuestionariComponent } from './questionari/questionari.component';
import { ValutazioneComponent } from './valutazione/valutazione.component';
import { AuthGuard } from "../_helpers/auth.guard";
import { RouteGuardAdmin } from '../_helpers/routeGuardAdmin';
import { AuthenticationService } from '../_services';
import { RouteGuardPerformance } from '../_helpers/routeGuardPerformance';
import { RouteGuardRisorsaOrResponsabile } from '../_helpers/routeGuardRisorsaOrResponsabille';
import { RouteGuardResponsabileOrAdmin } from '../_helpers/routeGuardResponsabileOrAdmin';
const routes: Routes = [
  {
    path: "",
    component: RegistrazioniComponent,
    canActivate: [RouteGuardPerformance],
    pathMatch: "full",
  },
  {
    path: "registrazioni",
    component: RegistrazioniComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      role: "AMMINISTRATORE",
      title: "Registrazioni",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "registrazioni/crea",
    component: RegistrazionidettaglioComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Crea utente",
    },
  },
  {
    path: "registrazioni/dettaglio/:registrazioneId",
    component: RegistrazionidettaglioComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Dettaglio Registrazione",
    },
  },
  {
    path: "regolamento",
    component: RegolamentoComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Regolamento",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "regolamento/crea",
    component: RegolamentodettaglioComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Crea regolamento",
    },
  },
  {
    path: "regolamento/regolamentodettaglio/:regolamentoId",
    component: RegolamentodettaglioComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Dettaglio Regolamento",
    },
  },
  {
    path: "priorita",
    component: PrioritaComponent,
    canActivate: [RouteGuardResponsabileOrAdmin],
    data: {
      title: "Priorità",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "valutazioni",
    component: ValutazioniComponent,
    canActivate: [RouteGuardResponsabileOrAdmin],
    data: {
      title: "Valutazioni",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "questionari",
    component: QuestionariComponent,
    canActivate: [RouteGuardAdmin],
    data: {
      title: "Questionari di valutazione",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "valutazione",
    component: ValutazioneComponent,
    canActivate: [RouteGuardRisorsaOrResponsabile],
    data: {
      title: "La propria valutazione",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PerformanceRoutingModule {
  constructor(
    private authenticationService: AuthenticationService,
  ) {}
  redirect(): string{
    return "";
  }
}
