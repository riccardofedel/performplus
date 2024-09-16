import {
  Component,
  OnInit,
  NgZone,
  OnDestroy,
  Output,
  EventEmitter,
  Input,
  OnChanges,
  SimpleChanges,
} from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";

import { FormBuilder } from "@angular/forms";
import { AlberoQuestionariService, QuestionariService } from "src/app/_services";
import { User } from "src/app/_models";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { environment } from "src/environments/environment";

@Component({
  selector: "scheda-albero",
  templateUrl: "./scheda-albero.component.html",
  styleUrls: ["./scheda-albero.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaAlberoComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public alberoQuestionariService: AlberoQuestionariService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private questionariService: QuestionariService,
    private authenticationService: AuthenticationService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  idEnte: number | undefined;
  anno: number | undefined;

  @Input() searchData: any | undefined;
  @Input() cambioFigli: any | undefined;

  ngOnInit() {
    this.reload();
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  isClosed(level: string) {
    return this.alberoQuestionariService.closed[level];
  }
  toggle(nodo: any) {
    if (!this.alberoQuestionariService.closed[nodo.id]) {
      this.alberoQuestionariService.closed[nodo.id] = true;
    } else {
      this.alberoQuestionariService.closed[nodo.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!nodo.children) {
        let sub: Subscription = this.questionariService
          .getChildren(
            this.idEnte!,
            this.anno!,
            this.questionariService.toTipoNodo(nodo.tipoNodo),
            nodo.id
          )
          .subscribe((result) => {
            nodo.children = result;
            for (let i in result) {
              result[i].puntatorePadre = nodo;
              this.alberoQuestionariService.closed[result[i].id] = true;
            }
          });
        this.subs.push(sub);
      }
    }
  }
  reloadChildren(nodo: any) {
    let sub: Subscription = this.questionariService
      .getChildren(
        this.idEnte!,
        this.anno!,
        this.questionariService.toTipoNodo(nodo.tipoNodo),
        nodo.id
      )
      .subscribe((result) => {
        nodo.children = result;
        for (let i in result) {
          //per percorrere l'albero all'indietro
          result[i].puntatorePadre = nodo;
          this.alberoQuestionariService.closed[result[i].id] = true;
        }
      });
    this.subs.push(sub);
  }

  select(nodo: any) {
    //console.log("scheda-albero:", nodo);
    this.alberoQuestionariService.currentNodo = nodo;
    this.alberoQuestionariService.updateNodo(nodo.id);
    this.alberoQuestionariService.updateScheda("/questionari/scheda");
  }
  ngOnChanges(changes: SimpleChanges) {
    this.reload();
    if (
      changes.searchData &&
      (!changes.searchData.previousValue ||
        changes.searchData.previousValue != changes.searchData.currentValue)
    ) {
      this.caricaAlbero(changes.searchData.currentValue);
    }
  }
  caricaAlbero(searchParams: any) {
    this.alberoQuestionariService.caricaQuestionarioSearch(
      this.currentUser!,
      searchParams
    );
  }
  reload() {
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.idEnte = x.operator?.idEnte;
        this.anno = x.operator?.anno;
      }
    );
    this.subs.push(sub);
  }
}
