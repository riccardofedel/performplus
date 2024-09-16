import {
  Component,
  OnInit,
  NgZone,
  OnDestroy,
  ElementRef,
  ViewChild,
} from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";

import { FormBuilder } from "@angular/forms";
import { Permission, PermissionService } from "src/app/_services";
import { User } from "src/app/_models";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";

import { AlberoQuestionariService } from "src/app/_services/albero.questionari.service";
import { environment } from "src/environments/environment";
import { ActivatedRoute } from "@angular/router";
import { SchedaAlberoComponent } from "./schede/scheda-albero/scheda-albero.component";

@Component({
  selector: "app-questionari",
  templateUrl: "./questionari.component.html",
  styleUrls: ["./questionari.component.scss"],
  providers: [TranslatePipe],
})
export class QuestionariComponent implements OnInit, OnDestroy {
  constructor(
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    public alberoQuestionariService: AlberoQuestionariService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private el: ElementRef
  ) {}

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  page = "questionari";

  sorter = null;

  closed: any | undefined;

  showTreeMobile = false;
  showTree = true;
  showData = true;

  currentScheda: string | undefined;

  @ViewChild("schedaAlbero", { static: false })
  schedaAlbero!: SchedaAlberoComponent;

  ngOnInit() {
    this.currentScheda = "/questionari/scheda";
    this.page = "questionari";
    this.alberoQuestionariService.updateScheda(this.currentScheda);
    this.closed = {};
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
       //("--------BBBBBBB");
        if (this.currentUser) {
          let sub2: Subscription = this.alberoQuestionariService
            ?.getMessage()
            .subscribe((message) => {
            });
            this.subs.push(sub2);
          //attenzione: se entriamo in questa pagina per la
          //prima volta manca la questionari
          //quindi la carichiamo e poi lei ci manda un messaggio
          //per alberoSubscription e carichiamo i dati che mancano
          this.loading = false;
          if (!this.alberoQuestionariService?.nodoAperto) {
            this.alberoQuestionariService.caricaQuestionarioSearch(
              this.currentUser,
              this.alberoQuestionariService?.getSearchData()
            );
          }
        }else {
          this.loading = false;
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
    this.alberoQuestionariService.updateScheda(scheda);
    switch (this.currentScheda) {
      case "/questionari/risorse":
        this.page = "risorse-questionari";
        break;
    }
  }

  confirmSearch(cerca: any) {
    this.loading = true;
    this.alberoQuestionariService.updateSearchData(cerca);
  }
  vaiAQuestionari() {
    this.cambiaScheda("/questionari/scheda");
  }
  ricaricaFigli(params: any) {
    this.schedaAlbero.reloadChildren(this.alberoQuestionariService.currentNodo);
  }
  ricaricaFigliPadre(vai: any) {
    this.schedaAlbero.reloadChildren(
      this.alberoQuestionariService.currentNodo.puntatorePadre
    );
    if (vai === true)
      this.schedaAlbero.select(
        this.alberoQuestionariService.currentNodo.puntatorePadre
      );
  }
}
