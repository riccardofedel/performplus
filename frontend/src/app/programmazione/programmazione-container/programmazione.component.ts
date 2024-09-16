import { Component, OnInit, NgZone, OnDestroy, ElementRef, AfterViewInit, SimpleChanges, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { AlberoProgrammazioneService, Permission, PermissionService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { environment } from "src/environments/environment";
import { Schede,Pages,Azioni } from '../constants';
import { SchedaAlberoComponent } from '../schede/scheda-albero/scheda-albero.component';

@Component({
  selector: "app-programmazione",
  templateUrl: "./programmazione.component.html",
  styleUrls: ["./programmazione.component.scss"],
  providers: [TranslatePipe],
})
//, AfterViewInit
export class ProgrammazioneComponent implements OnInit, OnDestroy {
  constructor(
    private route: ActivatedRoute,
    public permissionService: PermissionService,

    public alberoProgrammazioneService: AlberoProgrammazioneService,
    public translate: TranslateService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,

    private authenticationService: AuthenticationService,
    private el: ElementRef
  ) {}

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  Permission = Permission;

  menuprog: boolean = false;

  nodo: any;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  sorter = null;

  closed: any | undefined;

  showTree = true;
  showData = true;

  currentScheda: string | undefined;

  page = "programmazione";

  @ViewChild("schedaAlbero", { static: false })
  schedaAlbero!: SchedaAlberoComponent;

  ngOnInit() {
    this.currentScheda = Schede.programmazione;
    this.page = Pages.programmazione;
    this.alberoProgrammazioneService.initializeClosed();
    this.alberoProgrammazioneService.programmazioneAperta=
    this.alberoProgrammazioneService.isConsuntivazione = false;
    //console.log("--------------->>>>>this.route", this.route);
    if (
      this.route &&
      this.route.snapshot &&
      this.route.snapshot.url &&
      this.route.snapshot.url[0] &&
      this.route.snapshot.url[0].path
    ) {
      if (
        this.route.snapshot.url[0].path
          .toLowerCase()
          .includes("consuntivazione")
      ) {
        this.currentScheda = Schede.consuntivazione;
        this.alberoProgrammazioneService.isConsuntivazione = true;
        this.page = Pages.consuntivazione;
      }
    }

    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          let sub2: Subscription = this.alberoProgrammazioneService
            ?.getMessage()
            .subscribe((message) => {
              //to do
              this.loading = false;
            });
          this.subs.push(sub2);
          if (!this.alberoProgrammazioneService?.programmazioneAperta) {
            this.loading = true;
            this.alberoProgrammazioneService?.caricaProgrammazioneSearch(
              this.currentUser,
              this.alberoProgrammazioneService?.getSearchData
            );
          }
        }
      }
    );
    this.subs.push(sub1);
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
    //console.log("cambiaScheda:", scheda);
    this.currentScheda = scheda;
    this.alberoProgrammazioneService.isConsuntivazione = false;
    switch (this.currentScheda) {
      case Schede.pesatura:
        this.page = Pages.pesatura;
        break;
      case Schede.risorse:
        this.page = Pages.risorse;
        break;
      case Schede.indicatori:
        this.page = Pages.indicatori;
        break;
      case Schede.consuntivazione:
        this.alberoProgrammazioneService.isConsuntivazione = true;
        this.page = Pages.consuntivazione;
        break;
      case Schede.risorse:
        this.page = Pages.risorse;
        break;
      case Schede.programmazione:
      default:
        this.page = Pages.programmazione;
        break;
    }
    this.alberoProgrammazioneService.updateScheda(scheda);
  }
  vaiAProgrammazione() {
    this.cambiaScheda(Schede.programmazione);
  }
  vaiARendicontazione() {
    this.cambiaScheda(Schede.consuntivazione);
  }
  confirmSearch(cerca: any) {
    //console.log("------->confirmSearch");
    this.loading = true;
    this.alberoProgrammazioneService.updateSearchData(cerca);
  }

  ricaricaFigli(params: any) {
    this.schedaAlbero.reloadChildren(
      this.alberoProgrammazioneService.currentProgrammazione
    );
  }
  ricaricaFigliPadre(vai: any) {
    this.schedaAlbero.reloadChildren(
      this.alberoProgrammazioneService.currentProgrammazione.puntatorePadre
    );
    if (vai === true)
      this.schedaAlbero.select(
        this.alberoProgrammazioneService.currentProgrammazione.puntatorePadre
      );
  }
  getPage() {
    if (!this.page) return Pages.programmazione;
    if (
      this.page !== Pages.programmazione &&
      this.page !== Pages.consuntivazione
    )
      return this.page;
    if (this.alberoProgrammazioneService.isConsuntivazione)
      return Pages.consuntivazione;
    return Pages.programmazione;
  }
}

