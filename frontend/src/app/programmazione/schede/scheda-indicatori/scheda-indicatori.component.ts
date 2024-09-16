import { Component, OnInit, NgZone, OnDestroy, ElementRef, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder } from '@angular/forms';
import { AlberoProgrammazioneService, ProgrammazioneService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  PermissionService,
  Permission,
} from "src/app/_services/permission.service";
import { AggiungiindicatoreComponent } from '../../modal/aggiungiindicatore/aggiungiindicatore.component';
import { FieldsService } from 'src/app/_services/fields.service';


@Component({
  selector: "scheda-indicatori",
  templateUrl: "./scheda-indicatori.component.html",
  styleUrls: ["./scheda-indicatori.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaIndicatoriComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    private router: Router,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public alberoProgrammazioneService: AlberoProgrammazioneService,
    public translate: TranslateService,
    private programmazioneService: ProgrammazioneService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private el: ElementRef,
    public permissionService: PermissionService,
    public fieldsService: FieldsService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  paginationService: PaginationService | undefined;

  reader: boolean | undefined;
  writer: boolean | undefined;

  anno: number = 0;
  annoInizio = 0;
  //solo quelle che visualizzo
  indicatori: any[] = [];

  //tutte
  tuttiIndicatori: any[] = [];

  closed: any | undefined;

  Permission = Permission;

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
    this.indicatori = this.tuttiIndicatori?.slice(first - 1, last);
  }

  refreshDataTable() {
    if (this.currentProgrammazione === null) {
      return;
    }
    this.loading = true;
    this.annoInizio =
      this.alberoProgrammazioneService.currentProgrammazione.annoInizio;
    let sub: Subscription = this.programmazioneService
      .getIndicatoriAssociati(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.loading = false;
        this.tuttiIndicatori = result;
        this.paginationService?.updateCount(result?.length ? result.length : 0);
        this.refreshTable();
      });
      this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiungiIndicatorePopup() {
    const modalRef = this.modalService.open(AggiungiindicatoreComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idNodoPiano = this.currentProgrammazione.id;

    modalRef.result.then(
      (result) => {
        this.refreshDataTable();
      },
      (reason) => {}
    );
  }

  elimina(id: number | undefined) {
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Rimuovi l'indicatore #" + id,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.programmazioneService
          .rimuoviIndicatorePiano(this.currentProgrammazione.id, id)
          .subscribe(
            (result) => {
              this.refreshDataTable();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella rimozione dell'indicatore: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
          this.subs.push(sub);
      }
    });
  }

  modificaIndicatorePopup(idIndicatore: number | undefined) {
    const modalRef = this.modalService.open(AggiungiindicatoreComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idNodoPiano = this.currentProgrammazione.id;
    modalRef.componentInstance.id = idIndicatore;
    modalRef.result.then(
      (result) => {
        //rinfresco sempre per sicurezza, perché potrei aver salvato
        //senza chiudere e poi chiuso
        //if (result==="refresh") {
        this.refreshDataTable();
        //}
      },
      (reason) => {}
    );
  }

  getCurrentNodoTipoId() {
    if (this.currentProgrammazione?.tipoNodo) {
      return this.currentProgrammazione?.tipoNodo;
    }
    return 0;
  }

  reload() {
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;

        this.anno = this.currentUser?.operator?.anno
          ? this.currentUser?.operator?.anno
          : new Date().getFullYear();
        this.refreshDataTable();
        this.paginationService?.reset(false);
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
    //console.log("scheda-indicatori changes:", changes);
    this.reload();
  }
  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case "aggiungiIndicatore":
        this.aggiungiIndicatorePopup();
        break;
    }
  }
  readAllowed() {
    return this.reader;
  }

  writeAllowed() {
    return this.writer;
  }
}

