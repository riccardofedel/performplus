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
} from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { FormBuilder } from "@angular/forms";
import { Risorsa, User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { ProgrammazioneService } from "src/app/_services/programmazione.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { AggiungirisorsaProgComponent } from "src/app/programmazione/modal/aggiungirisorsaprog/aggiungirisorsaprog.component";
import { Permission, PermissionService } from "src/app/_services";
import { SimpleChanges, OnChanges } from '@angular/core';
import { FieldsService } from "src/app/_services/fields.service";

@Component({
  selector: "scheda-risorse",
  templateUrl: "./scheda-risorse.component.html",
  styleUrls: ["./scheda-risorse.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaRisorseComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public permissionService: PermissionService,
    private modalService: NgbModal,
    public translate: TranslateService,
    public fb: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService,
    public fieldsService: FieldsService
  ) {}

  Permission = Permission;

  anno = 0;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  paginationService: PaginationService | undefined;

  totaleRisorse: number | undefined;

  responsabile: Risorsa | undefined;

  totaleRisorseEffettive: number | undefined;

  intestazione: string | undefined;

  descrizione: string | undefined;

  codice: string | undefined;

  //solo quelle che visualizzo
  risorse: Risorsa[] = [];

  //tutte
  tutteRisorse: Risorsa[] = [];

  closed: any | undefined;

  reader: boolean | undefined;
  writer: boolean | undefined;

  @Input() currentProgrammazione: any | undefined;
  @Output() cambiaScheda: EventEmitter<any> = new EventEmitter<any>();

  ngOnInit() {
    this.reload();
  }

  refreshTable() {
    const first = this.paginationService?.getFirst()
      ? this.paginationService?.getFirst()
      : 0;
    const last = this.paginationService?.getLast()
      ? this.paginationService?.getLast()
      : 0;
    this.risorse = this.tutteRisorse?.slice(first - 1, last);
  }

  refreshDataTable() {
    if (!this.currentProgrammazione || !this.currentProgrammazione.id) {
      return;
    }
    this.loading = true;
    this.programmazioneService
      .getProgrammazione(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.totaleRisorse = result.totaleRisorse;
        this.totaleRisorseEffettive = result.totaleRisorseEffettive;
      });

    this.programmazioneService
      .getRisorseAssociate(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.loading = false;
        this.tutteRisorse = result;
        this.totaleRisorse = result?.length;
        this.paginationService?.updateCount(result?.length ? result.length : 0);
        this.refreshTable();
      });
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  aggiungiRisorsaPopup() {
    const modalRef = this.modalService.open(AggiungirisorsaProgComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentProgrammazione.id;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  elimina(id: number | undefined) {
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Rimuovi la risorsa #" + id,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.programmazioneService
          .rimuoviRisorsa(id)
          .subscribe(
            (result) => {
              this.refreshDataTable();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella rimozione della risorsa: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }

  getCurrentNodoTipoId() {
    if (this.currentProgrammazione?.tipoNodo) {
      return this.currentProgrammazione?.tipoNodo;
    }
    return 0;
  }

  readAllowed() {
    return this.reader;
  }

  writeAllowed() {
    return this.writer;
  }

  vaiASchedaProgrammazione() {
    this.cambiaScheda.emit("programmazione/scheda");
  }
  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case "aggiungiRisorsa":
        this.aggiungiRisorsaPopup();
        break;
    }
  }
  reload() {
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.anno = this.currentUser?.operator?.anno
          ? this.currentUser?.operator?.anno
          : new Date().getFullYear();
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
    this.reader = this.permissionService.getPermission(
      this.currentUser,
      Permission.DupProgrammazione,
      true
    );
    this.writer = this.permissionService.getPermission(
      this.currentUser,
      Permission.DupProgrammazione,
      false
    );
  }
  ngOnChanges(changes: SimpleChanges) {
    //console.log("schda-risorse changes:", changes);
    this.reload();
  }
}
