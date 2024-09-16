import { Component, OnInit, NgZone, ViewChild, OnDestroy, ElementRef, Input, Output, EventEmitter, SimpleChanges, OnChanges } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder } from '@angular/forms';
import { ConsuntivazioneService, Permission, PermissionService, ProgrammazioneService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription, Subject } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FieldsService } from 'src/app/_services/fields.service';
import { AggiungiindicatoreComponent } from '../../modal/aggiungiindicatore/aggiungiindicatore.component';
import { RisultatopopupComponent } from '../../modal/risultato/risultatopopup.component';
import { ForzaturapopupComponent } from '../../modal/forzatura/forzaturapopup.component';
import { AggiungiNotePopupComponent } from '../../modal/aggiungi-note/aggiungi-note-popup.component';
import { ForzaturaNodoPopupComponent } from '../../modal/forzatura-nodo/forzatura-nodo-popup.component';

@Component({
  selector: "scheda-consuntivazione",
  templateUrl: "./scheda-consuntivazione.component.html",
  styleUrls: ["./scheda-consuntivazione.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaConsuntivazioneComponent
  implements OnInit, OnDestroy, OnChanges
{
  constructor(
    private router: Router,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private consuntivazioneService: ConsuntivazioneService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public fieldsService: FieldsService,
    private programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService,
    private el: ElementRef
  ) {}

  Permission = Permission;

  nodo: any;

  currentUser: User | undefined;

  loading = false;

  subs: Subscription[] = [];

  paginationService: PaginationService | undefined;

  //solo quelle che visualizzo
  indicatori: any[] = [];

  //tutte
  tuttiIndicatori: any[] = [];

  closed: any | undefined;

  programmazione: any | undefined;

  @Input() currentProgrammazione: any | undefined;
  @Output() cambiaScheda: EventEmitter<any> = new EventEmitter<any>();

  responsabili: any[] = [];
  risorse: any[] = [];
  obiettiviStrategici: any[] = [];

  isLoadingRisorse = false;
  isLoadingResponsabili = false;
  isLoadingObiettiviStrategici = false;

  keyword = "description";

  fields: string[] = [];
  annoInizio = 0;
  annoFine = 0;

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
    let odd = false;
    for (let i in this.indicatori) {
      if (this.indicatori[i].codice) {
        odd = !odd;
      }
      this.indicatori[i].oddRow = odd;
    }
  }

  refreshDataTable() {
    if (!this.currentProgrammazione || !this.currentProgrammazione.id) {
      return;
    }
    this.loading = true;
    let sub: Subscription = this.consuntivazioneService
      .getConsuntivazione(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.nodo = result;
        this.fields = result.fields;
        this.annoInizio = result.annoInizio;
        this.annoFine = result.annoFine;
        this.loading = false;
        this.tuttiIndicatori = result.consuntivoIndicatori;
        this.paginationService?.updateCount(
          this.tuttiIndicatori?.length ? this.tuttiIndicatori.length : 0
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

  viewIndicatorePopup(idIndicatore: number) {
    const modalRef = this.modalService.open(AggiungiindicatoreComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idNodoPiano = this.currentProgrammazione.id;
    modalRef.componentInstance.id = idIndicatore;
    modalRef.componentInstance.readOnly = true;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  risultatoPopup(idIndicatore: number, consuntivazione: any) {
    const modalRef = this.modalService.open(RisultatopopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idIndicatore = idIndicatore;
    modalRef.componentInstance.consuntivazione = consuntivazione;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  aggiungiNotePopup(idNodo: number, consuntivazione: any) {
    const modalRef = this.modalService.open(AggiungiNotePopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idNodoPiano = idNodo;
    modalRef.componentInstance.consuntivazione = consuntivazione;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  forzatureNodoPopup(idNodo: number, consuntivazione: any) {
    const modalRef = this.modalService.open(ForzaturaNodoPopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idNodoPiano = idNodo;
    modalRef.componentInstance.writeAllowed = this.writeAllowed();
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }
  forzaturaPopup(idIndicatore: number) {
    const modalRef = this.modalService.open(ForzaturapopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idIndicatore = idIndicatore;
    modalRef.componentInstance.writeAllowed = this.writeAllowed();
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
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

  currentAnno(offset = 0) {
    if (!this.currentUser?.operator?.anno) {
      return "";
    }
    return this.currentUser?.operator?.anno + offset;
  }

  onChangeSearchRisorsa($event: any) {
    if (!$event) {
      return;
    }
    this.isLoadingRisorse = true;
    let sub: Subscription = this.programmazioneService
      .getRisorse(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingRisorse = false;
        this.risorse = result.map((val: any) => {
          return { id: val.id, description: val.descrizione };
        });
      });
    this.subs.push(sub);
  }
  onChangeSearchResponsabile($event: any) {
    if (!$event) {
      return;
    }
    this.isLoadingResponsabili = true;
    let sub: Subscription = this.programmazioneService
      .getResponsabili(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingResponsabili = false;
        this.responsabili = result.map((val: any) => {
          return { id: val.id, description: val.descrizione };
        });
      });
    this.subs.push(sub);
  }

  readAllowed() {
    return this.nodo?.enabling?.read;
  }

  writeAllowed() {
    if (!this.nodo?.enabling.write) {
      return false;
    }
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.DupConsuntivazione,
      false
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    this.nodo = changes.currentProgrammazione.currentValue;
    this.reload();
  }
  reload() {
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
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
      case "note":
        this.aggiungiNotePopup(
          this.currentProgrammazione.id,
          this.currentProgrammazione
        );
        break;
      case "forzature":
        this.forzatureNodoPopup(
          this.currentProgrammazione.id,
          this.currentProgrammazione
        );
        break;
    }
  }
  visualizzaField(field: string) {
    return (
      this.fields === undefined ||
      this.fields?.length == 0 ||
      this.fields?.some((x) => x === field)
    );
  }

  getNodoPadre() {
    switch (this.getCurrentNodoTipoId()) {
      case 2:
        return "area";
      case 3:
        return "obiettivo";
      case 4:
        return "azione";
    }
    return "";
  }

  getCurrentTipo() {
    return this.nodo?.tipoNodo;
  }

  annoInizioTab(offset = 0) {
    if (!this.annoInizio) {
      return "";
    }
    return this.annoInizio + offset;
  }
}

