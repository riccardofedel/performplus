import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AmministratoriComponent } from "./amministratori/amministratori.component";
import { AmministratoridettaglioComponent } from "./amministratori/amministratoridettaglio/amministratoridettaglio.component";
import { RisorseComponent } from "./risorse/risorse.component";
import { OrganigrammaComponent } from "./organigramma/organigramma.component";
import { RisorsedettaglioComponent } from "./risorse/risorsedettaglio/risorsedettaglio.component";

const routes: Routes = [
  {
    path: "amministratori",
    component: AmministratoriComponent,
    data: {
      title: "Lista amministratori",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "amministratori/dettaglio/:amministratoreId",
    component: AmministratoridettaglioComponent,
    data: {
      title: "Dettaglio amministratore",
    },
  },
  {
    path: "amministratori/crea",
    component: AmministratoridettaglioComponent,
    data: {
      title: "Crea amministratore",
    },
  },
  {
    path: "risorse",
    component: RisorseComponent,
    data: {
      title: "Lista risorse",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "risorse/dettaglio/:risorsaId",
    component: RisorsedettaglioComponent,
    data: {
      title: "Dettaglio risorsa",
    },
  },
  {
    path: "risorse/crea",
    component: RisorsedettaglioComponent,
    data: {
      title: "Crea risorsa",
    },
  },
  {
    path: "",
    component: OrganigrammaComponent,
    data: {
      title: "Organigramma",
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
export class StrutturaRoutingModule {}
