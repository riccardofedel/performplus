import { Component, OnInit, NgZone, OnDestroy, Output, EventEmitter, Input, OnChanges, SimpleChanges  } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder} from '@angular/forms';
import { AlberoStruttureService, StruttureService } from "src/app/_services";
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { environment } from 'src/environments/environment';
import { Schede } from "src/app/struttura/organigramma/constants";

@Component({
  selector: "scheda-albero",
  templateUrl: "./scheda-albero.component.html",
  styleUrls: ["./scheda-albero.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaAlberoComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public alberoStrutturaService: AlberoStruttureService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private struttureService: StruttureService,
    private authenticationService: AuthenticationService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  @Input() searchData: any | undefined;
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
    return this.alberoStrutturaService.closed[level];
  }
  toggle(struttura: any) {
    if (!this.alberoStrutturaService.closed[struttura.id]) {
      this.alberoStrutturaService.closed[struttura.id] = true;
    } else {
      this.alberoStrutturaService.closed[struttura.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!struttura.children) {
        let sub: Subscription = this.struttureService
          .getChildren(struttura.id)
          .subscribe((result) => {
            struttura.children = result;
            for (let i in result) {
              result[i].puntatorePadre = struttura;
              this.alberoStrutturaService.closed[result[i].id] = true;
            }
          });
          this.subs.push(sub);
      }
    }
  }
  reloadChildren(struttura: any) {
    let sub: Subscription = this.struttureService
      .getChildren(struttura.id)
      .subscribe((result) => {
        struttura.children = result;
        for (let i in result) {
          //per percorrere l'albero all'indietro
          result[i].puntatorePadre = struttura;
          this.alberoStrutturaService.closed[result[i].id] = true;
        }
      });
      this.subs.push(sub);
  }

  select(struttura: any) {
    //console.log("scheda-albero:", struttura);
    this.alberoStrutturaService.currentStruttura = struttura;
    this.alberoStrutturaService.updateStruttura(struttura.id);
    this.alberoStrutturaService.updateScheda(Schede.struttura);
  }
  ngOnChanges(changes: SimpleChanges) {
    this.reload();
    this.caricaAlbero(changes.searchData.currentValue);
  }
  caricaAlbero(searchParams: any) {
    this.alberoStrutturaService.caricaStrutturaSearch(
      this.currentUser,
      searchParams
    );
  }
  reload() {
    let sub: Subscription = 
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
      });
      this.subs.push(sub);
  }
}

