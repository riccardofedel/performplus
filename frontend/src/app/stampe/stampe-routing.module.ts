import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportEGraficiComponent } from './bi/bi.component';
import { StampeComponent } from './stampe.component';

const routes: Routes = [
  {
    path: "stampe",
    component: StampeComponent,
    data: {
      title: "Stampe",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "",
    component: ReportEGraficiComponent,
    data: {
      title: "Report e Grafici",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "report",
    component: ReportEGraficiComponent,
    data: {
      title: "Report e Grafici",
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
  exports: [RouterModule]
})
export class StampeRoutingModule { }
