import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CruscottoComponent } from './cruscotto/cruscotto.component';
import { IndicatoriComponent } from './indicatori/indicatori.component';
import { IndicatoridettaglioComponent } from './indicatori/indicatoridettaglio/indicatoridettaglio.component';
import { UtentiComponent } from './utenti/utenti.component';
import { UtentidettaglioComponent } from './utenti/utentidettaglio/utentidettaglio.component';
import { BIComponent } from './bi/bi.component';
import { AnagraficaComponent } from './anagrafiche/anagrafica.component';
import { AnagraficadettaglioComponent } from './anagrafiche/anagraficadettaglio/anagraficadettaglio.component';

const routes: Routes = [
  {
    path: '',
    component: CruscottoComponent,
    data: {
      title: 'Cruscotto',
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null
      }
    },
  },
  {
    path: 'Reports&Grafici',
    component: BIComponent,
    data: {
      title: 'Report e Grafici',
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null
      }
    },
  },
  {
    path: 'utenti',
    component: UtentiComponent,
    data: {
      title: 'Lista utenti',
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null
      }
    },
  },
  {
    path: 'utenti/dettaglio/:id',
    component: UtentidettaglioComponent,
    data: {
      title: 'Dettaglio utente',
    },
  },
  {
    path: 'utenti/crea',
    component: UtentidettaglioComponent,
    data: {
      title: 'Crea utente',
    },
  },
  {
    path: 'indicatori',
    component: IndicatoriComponent,
    data: {
      title: 'Lista indicatori',
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null
      }
    },
  },
  {
    path: 'indicatori/dettaglio/:indicatoreId',
    component: IndicatoridettaglioComponent,
    data: {
      title: 'Dettaglio indicatore',
    },
  },
  {
    path: 'indicatori/crea',
    component: IndicatoridettaglioComponent,
    data: {
      title: 'Crea indicatore',
    },
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SistemaRoutingModule { }
