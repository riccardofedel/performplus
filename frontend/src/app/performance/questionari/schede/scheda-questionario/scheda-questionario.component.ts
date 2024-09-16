import {
  Component,
  OnInit,
  NgZone,
  ViewChild,
  OnDestroy,
  ElementRef,
  Input,
  Output,
  EventEmitter,
  SimpleChanges,
  OnChanges,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";

import { FormBuilder } from "@angular/forms";
import {
  QuestionariService,
  Permission,
  PermissionService,
  ProgrammazioneService,
} from "src/app/_services";
import { User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { FieldsService } from "src/app/_services/fields.service";

import { ModificaPesoQuestionarioComponent } from "../../modal/modifica-peso/modifica-peso.component";
import { AggiungiquestionarioComponent } from "../../modal/aggiungiquestionario/aggiungiquestionario.component";
import { TipoNodoQuestionario } from "src/app/_services/questionari.service";

@Component({
  selector: "scheda-questionario",
  templateUrl: "./scheda-questionario.component.html",
  styleUrls: ["./scheda-questionario.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaQuestionarioComponent
  implements OnInit, OnDestroy, OnChanges
{
  constructor(
    private router: Router,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private questionariService: QuestionariService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public fieldsService: FieldsService,
    private programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService,
    private el: ElementRef
  ) {}

  Permission = Permission;

  nodo: any;

  idEnte = 0;
  anno: number | undefined;

  currentUser: User | undefined;

  loading = false;

  subs: Subscription[] = [];

  paginationService: PaginationService | undefined;

  //solo quelle che visualizzo
  items: any[] = [];

  tuttiItems: any[] = [];

  closed: any | undefined;

  @Input() currentNodo: any | undefined;
  @Output() cambiaScheda: EventEmitter<any> = new EventEmitter<any>();
  @Output() ricaricaFigli: EventEmitter<any> = new EventEmitter<any>();
  @Output() ricaricaFigliPadre: EventEmitter<any> = new EventEmitter<any>();

  keyword = "description";

  ngOnInit() {
    this.reload();
  }

  refreshTable() {
    if (!this.tuttiItems) {
      this.loading = false;
      this.items = [];
      return;
    }
    const first = this.paginationService?.getFirst()
      ? this.paginationService?.getFirst()
      : 0;
    const last = this.paginationService?.getLast()
      ? this.paginationService?.getLast()
      : 0;
    this.items = this.tuttiItems?.slice(first - 1, last);
    let odd = false;
    for (let i in this.items) {
      if (this.items[i].codice) {
        odd = !odd;
      }
      this.items[i].oddRow = odd;
    }
  }

  refreshDataTable() {
    //console.log("----refreshDataTable");
    if (!this.currentNodo || !this.currentNodo.id) {
      this.loading = false;
      return;
    }
    this.loading = true;
    let sub: Subscription = this.questionariService
      .getNodo(this.idEnte, this.getCurrentNodoTipoId(), this.currentNodo!.id)
      .subscribe((result) => {
        this.nodo = result;
        //console.log("------NODO:", this.nodo);

        this.loading = false;
        this.tuttiItems = result.figli;
        this.paginationService?.updateCount(
          this.tuttiItems?.length ? this.tuttiItems.length : 0
        );
        this.refreshTable();
      });
      this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiungiNodoPopup() {
    const modalRef = this.modalService.open(AggiungiquestionarioComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    const tipo = this.questionariService.toTipoNodo(this.currentNodo.tipoNodo);
    modalRef.componentInstance.id = undefined;
    modalRef.componentInstance.tipo = undefined;
    modalRef.componentInstance.idPadre = this.currentNodo.id;
    modalRef.componentInstance.tipoPadre = tipo;
    //console.log("----TIPOF:", tipo);
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.aggiornaFigli();
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  modificaNodoPopup() {
    const modalRef = this.modalService.open(AggiungiquestionarioComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    //console.log("-------modificaNodoPopup", this.nodo);
    modalRef.componentInstance.id = this.nodo.id;
    modalRef.componentInstance.tipo = this.nodo.tipo;
    modalRef.componentInstance.idPadre = undefined;
    modalRef.componentInstance.tipoPadre = undefined;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.aggiornaFigliPadre(false);
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  modificaPesoPopup(nodo: any) {
    const modalRef = this.modalService.open(ModificaPesoQuestionarioComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.nodo = nodo;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.aggiornaFigliPadre(false);
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }
  getCurrentNodoTipoId(): TipoNodoQuestionario {
    return this.questionariService.toTipoNodo(this.currentNodo?.tipoNodo);
  }

  readAllowed() {
    return this.currentNodo?.enabling?.read;
  }

  writeAllowed() {
    if (!this.currentNodo?.enabling?.write) {
      return false;
    }
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.Questionari,
      false
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    this.reload();
  }
  reload() {
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.idEnte = this.currentUser.operator!.idEnte;
        this.anno = this.currentUser.operator!.anno;
        this.loading = false;
        this.paginationService?.reset(false);
        this.refreshDataTable();
      }
    );
    this.subs.push(sub1);
    this.paginationService = new PaginationService();
    let sub2: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refreshTable();
      });
      this.subs.push(sub2);
  }
  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case "aggiungiNodo":
        this.aggiungiNodoPopup();
        break;
      case "modificaNodo":
        this.modificaNodoPopup();
        break;
      case "elimina":
        this.eliminaNodo();
        break;
    }
  }

  eliminaNodo() {
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina il nodo #" + this.currentNodo?.id,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.questionariService
          .deleteNodo(this.getCurrentNodoTipoId(), this.currentNodo!.id)
          .subscribe(
            (result) => {
              const currentLevel = this.getCurrentLevel();
              this.aggiornaFigli();
              this.aggiornaFigliPadre(true);
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella cancellazione della nodo: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
          this.subs.push(sub);
      }
    });
    
  }
  getCurrentLevel() {
    if (!this.currentNodo) return -1;
    if (this.currentNodo.level) {
      return this.currentNodo.level - 1;
    }
    return 0;
  }

  aggiornaFigli() {
    this.ricaricaFigli.emit();
  }
  aggiornaFigliPadre(vai: boolean) {
    this.ricaricaFigliPadre.emit(vai);
  }
}
