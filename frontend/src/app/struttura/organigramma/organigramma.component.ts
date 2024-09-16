import { Component, OnInit, NgZone, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder } from '@angular/forms';
import { Permission, PermissionService, RisorseService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';

import { AlberoStruttureService } from "src/app/_services/albero.strutture.service";
import { environment } from 'src/environments/environment';
import { ActivatedRoute } from '@angular/router';
import { Pages, Schede } from './constants';
import { SchedaAlberoComponent } from './schede/scheda-albero/scheda-albero.component';

@Component({
  selector: "app-organigramma",
  templateUrl: "./organigramma.component.html",
  styleUrls: ["./organigramma.component.scss"],
  providers: [TranslatePipe],
})
export class OrganigrammaComponent implements OnInit, OnDestroy {
  constructor(
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    public alberoStrutturaService: AlberoStruttureService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private el: ElementRef
  ) {}

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  page = Pages.organigramma;

  sorter = null;

  closed: any | undefined;

  showTreeMobile = false;
  showTree = true;
  showData = true;

  currentScheda: string | undefined;

  @ViewChild("schedaAlbero", { static: false })
  schedaAlbero!: SchedaAlberoComponent;

  ngOnInit() {
    this.currentScheda = Schede.struttura;
    this.page = Pages.organigramma;
    this.alberoStrutturaService.updateScheda(this.currentScheda);
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          let sub2: Subscription = this.alberoStrutturaService
            ?.getMessage()
            .subscribe((message) => {
              //to do
              this.loading = false;
            });
            this.subs.push(sub2);

          //attenzione: se entriamo in questa pagina per la
          //prima volta manca la struttura
          //quindi la carichiamo e poi lei ci manda un messaggio
          //per alberoSubscription e carichiamo i dati che mancano
          if (!this.alberoStrutturaService?.strutturaAperta) {
            this.loading = true;
            this.alberoStrutturaService?.caricaStrutturaSearch(
              this.currentUser,
              this.alberoStrutturaService?.getSearchData()
            );
          } else {
            //altrimenti abbiamo già i dati che ci servono
          }
        }
      }
    );
    this.subs.push(sub1);
  }
  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  mostraAlberoMobile() {
    this.showTreeMobile = true;
  }
  mostraAlbero() {
    this.showTree = true;
  }
  nascondiAlbero() {
    this.showTree = false;
  }
  mostraData() {
    this.showData = true;
  }
  nascondiData() {
    this.showData = false;
  }
  mostraTutto() {
    this.showTree = true;
    this.showData = true;
  }
  cambiaScheda(scheda: any) {
    this.currentScheda = scheda;
    this.alberoStrutturaService.updateScheda(scheda);
    switch (this.currentScheda) {
      case Schede.risorse:
        this.page = Pages.risorseStruttura;
        break;
    }
  }

  confirmSearch(cerca: any) {
    //console.log("------->confirmSearch:",cerca);
    this.loading = true;
    this.alberoStrutturaService.updateSearchData(cerca);
  }
  vaiAStruttura() {
    this.cambiaScheda(Schede.struttura);
  }
  ricaricaFigli(params: any) {
    this.schedaAlbero.reloadChildren(
      this.alberoStrutturaService.currentStruttura
    );
  }
  ricaricaFigliPadre(vai: any) {
    this.schedaAlbero.reloadChildren(
      this.alberoStrutturaService.currentStruttura.puntatorePadre
    );
    if (vai === true)
      this.schedaAlbero.select(
        this.alberoStrutturaService.currentStruttura.puntatorePadre
      );
  }
}

