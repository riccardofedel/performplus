import { Component, OnInit, NgZone, ViewChild, OnDestroy, ElementRef, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { Risorsa, User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { StruttureService } from 'src/app/_services/strutture.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AggiungirisorsaComponent } from '../../model/aggiungirisorsa/aggiungirisorsa.component';
import { Permission } from 'src/app/_services';
import { PermissionService } from '../../../../_services/permission.service';
import { DisponibilitapopupComponent } from '../../model/disponibilitapopup/disponibilitapopup.component';
import { FieldsService } from 'src/app/_services/fields.service';
import { Azioni, Schede } from '../../constants';

@Component({
  selector: "scheda-risorse",
  templateUrl: "./scheda-risorse.component.html",
  styleUrls: ["./scheda-risorse.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaRisorseComponent implements OnInit, OnDestroy {
  constructor(
    private modalService: NgbModal,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private struttureService: StruttureService,
    private authenticationService: AuthenticationService,
    private el: ElementRef,
    public permissionService: PermissionService,
    public fieldsService: FieldsService
  ) {}

  Permission = Permission;

  anno = 0;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  paginationService: PaginationService | undefined;

  sorter = null;

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

  @Input() currentStruttura: any | undefined;
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
    if (!this.currentStruttura || !this.currentStruttura.id) {
      return;
    }
    this.loading = true;
    let sub1: Subscription = this.struttureService
      .getStruttura(this.currentStruttura.id)
      .subscribe((result) => {
        this.totaleRisorse = result.totaleRisorse;
        this.totaleRisorseEffettive = result.totaleRisorseEffettive;
      });
      this.subs.push(sub1);

    let sub2: Subscription = this.struttureService
      .getRisorseAssociate(this.currentStruttura.id)
      .subscribe((result) => {
        this.loading = false;
        this.tutteRisorse = result;
        this.totaleRisorse = result?.length;
        this.paginationService?.updateCount(result?.length ? result.length : 0);
        this.refreshTable();
      });
      this.subs.push(sub2);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiungiRisorsaPopup() {
    const modalRef = this.modalService.open(AggiungirisorsaComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentStruttura.id;
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
        let sub: Subscription = this.struttureService
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

  getCurrentLevel() {
    if (this.currentStruttura.level) {
      return this.currentStruttura.level - 1;
    }
    return 0;
  }

  disponibilitaPopup(risorsa: Risorsa) {
    const modalRef = this.modalService.open(DisponibilitapopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.risorsa = risorsa;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
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
      Permission.OrganigrammaStruttura,
      true
    );
    this.writer = this.permissionService.getPermission(
      this.currentUser,
      Permission.OrganigrammaStruttura,
      false
    );
  }
  ngOnChanges(changes: SimpleChanges) {
    //console.log("scheda-risorse changes:", changes);
    this.reload();
  }
  readAllowed() {
    return this.reader;
  }

  writeAllowed() {
    return this.writer;
  }

  vaiASchedaOrganigramma() {
    this.cambiaScheda.emit(Schede.struttura);
  }
  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case Azioni.aggiungiRisorsa:
        this.aggiungiRisorsaPopup();
        break;
    }
  }
}

