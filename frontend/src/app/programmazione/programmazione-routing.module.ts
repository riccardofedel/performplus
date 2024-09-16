import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IntroduzioneComponent } from './introduzione/introduzione.component';
import { ProgrammazioneComponent } from './programmazione-container/programmazione.component';


const routes: Routes = [
  // {
  //   path: "introduzione",
  //   component: IntroduzioneComponent,
  //   data: {
  //     title: "Introduzione",
  //     search: {
  //       filter: null,
  //       pgNum: null,
  //       pgSize: null,
  //       sort: null,
  //     },
  //   },
  // },
  // {
  //   path: "introduzione/:field",
  //   component: IntroduzioneComponent,
  //   data: {
  //     title: "Introduzione",
  //     search: {
  //       filter: null,
  //       pgNum: null,
  //       pgSize: null,
  //       sort: null,
  //     },
  //   },
  // },
  {
    path: "",
    component: ProgrammazioneComponent,
    data: {
      title: "Programmazione",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  },
  {
    path: "consuntivazione",
    component: ProgrammazioneComponent,
    data: {
      title: "Consuntivazione",
      search: {
        filter: null,
        pgNum: null,
        pgSize: null,
        sort: null,
      },
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProgrammazioneRoutingModule { }
