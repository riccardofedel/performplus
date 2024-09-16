import { Component, OnInit, NgZone, OnDestroy, Output, EventEmitter, Input, OnChanges, SimpleChanges  } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder } from '@angular/forms';
import { AlberoProgrammazioneService, ProgrammazioneService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: "scheda-albero",
  templateUrl: "./scheda-albero.component.html",
  styleUrls: ["./scheda-albero.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaAlberoComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public alberoProgrammazioneService: AlberoProgrammazioneService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  @Input() searchData: any | undefined;
  @Input() consuntivazione: boolean | undefined;
  //@Output() selectNode: EventEmitter<any> = new EventEmitter<any>();

  ngOnInit() {
    this.reload();
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  isClosed(level: string) {
    return this.alberoProgrammazioneService.closed[level];
  }
  toggle(programmazione: any) {
    if (!this.alberoProgrammazioneService.closed[programmazione.id]) {
      this.alberoProgrammazioneService.closed[programmazione.id] = true;
    } else {
      this.alberoProgrammazioneService.closed[programmazione.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!programmazione.children) {
        let sub: Subscription = this.programmazioneService
          .getChildren(programmazione.id)
          .subscribe((result) => {
            programmazione.children = result;
            for (let i in result) {
              result[i].puntatorePadre = programmazione;
              this.alberoProgrammazioneService.closed[result[i].id] = true;
            }
          });
          this.subs.push(sub);
      }
    }
  }
  reloadChildren(programmazione: any) {
    let sub: Subscription = this.programmazioneService
      .getChildren(programmazione.id, this.consuntivazione)
      .subscribe((result) => {
        programmazione.children = result;
        for (let i in result) {
          //per percorrere l'albero all'indietro
          result[i].puntatorePadre = programmazione;
          this.alberoProgrammazioneService.closed[result[i].id] = true;
        }
      });
      this.subs.push(sub);
  }

  select(programmazione: any) {
    this.alberoProgrammazioneService.currentProgrammazione = programmazione;
    this.alberoProgrammazioneService.updateProgrammazione(programmazione.id);
  }
  ngOnChanges(changes: SimpleChanges) {
    /*console.log(
      "scheda-albero changes:",
      changes?.searchData?.currentValue !== changes?.searchData?.previousValue,
      changes
    );*/
    if (
      changes?.searchData?.currentValue !== changes?.searchData?.previousValue
    ) {
      this.reload();
      this.caricaAlbero(changes.searchData.currentValue);
    }
  }
  caricaAlbero(searchParams: any) {
    this.alberoProgrammazioneService.consuntivazione(
      this.consuntivazione ? this.consuntivazione : false
    );
    this.alberoProgrammazioneService.caricaProgrammazioneSearch(
      this.currentUser,
      searchParams,
      this.consuntivazione
    );
  }
  reload() {
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub);
  }
}

