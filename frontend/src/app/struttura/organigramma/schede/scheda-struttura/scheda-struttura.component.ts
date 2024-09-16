import {
  Component,
  OnInit,
  NgZone,
  ViewChild,
  OnDestroy,
  ElementRef,
  Input,
  Output,
  EventEmitter
} from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";

import {
  FormBuilder,
  FormGroup
} from "@angular/forms";
import {
  AlberoStruttureService,
  Permission,
  PermissionService
} from "src/app/_services";
import { Risorsa, User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { FieldsService } from "src/app/_services/fields.service";
import { environment } from "src/environments/environment";
import { SimpleChanges, OnChanges } from '@angular/core';
import { StruttureService } from "src/app/_services/strutture.service";
import { AggiungistrutturaComponent } from "../../model/aggiungistruttura/aggiungistruttura.component";
import { Azioni } from "../../constants";
import { filter } from 'rxjs/operators';

enum TipoUtilizzo {
  Ok = "OK",
  UtilizzoParziale = "UTILIZZO_PARZIALE",
}

enum TipoDisponibile {
  Disponibile = "DISPONIBILE",
  NonDisponibile = "NON_DISPONIBILE",
}

@Component({
  selector: "scheda-struttura",
  templateUrl: "./scheda-struttura.component.html",
  styleUrls: ["./scheda-struttura.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaStrutturaComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    private modalService: NgbModal,
    public permissionService: PermissionService,
    public translate: TranslateService,
    public fb: FormBuilder,
    public fieldsService: FieldsService,
    private strutturaService: StruttureService,
    private alberoStrutturaService: AlberoStruttureService,
    private authenticationService: AuthenticationService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  paginationService: PaginationService | undefined;

  paginationRisorseService: PaginationService | undefined;

  loadedRisorse = false;

  @Input() currentStruttura: any | undefined;
  @Output() cambiaScheda = new EventEmitter<any>();
  @Output() ricaricaFigli: EventEmitter<any> = new EventEmitter<any>();
  @Output() ricaricaFigliPadre: EventEmitter<any> = new EventEmitter<any>();

  totaleRisorse: number | undefined;

  responsabile: Risorsa | undefined;

  totaleRisorseEffettive: number | undefined;

  intestazione: string | undefined;

  descrizione: string | undefined;

  codiceRidotto: string | undefined;

  codice: string | undefined;

  interim = false;

  //solo quelle che visualizzo
  risorse: any[] = [];

  //tutte
  datiStruttura: any | undefined;

  closed: any | undefined;

  struttura: any | undefined;

  cercaRisorseForm!: FormGroup;

  TipoUtilizzo = TipoUtilizzo;

  TipoDisponibile = TipoDisponibile;

  inizioValidita: string | undefined;

  fineValidita: string | undefined;

  anno: number | undefined;

  tipoStruttura: string | undefined;

  tipologiaStruttura: string | undefined;
  ngOnInit() {
    this.reload();
  }
  onSubmitCercaRisorse() {
    this.refreshTable();
  }

  refreshTable() {
    this.loading = true;

    const pgNum = this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    const testo = this.cercaRisorseForm.controls["nome"].value;
    //console.log("cercaRisorseForm", testo);

    const sort = "";
    let sub: Subscription = this.strutturaService
      .getDipendenti(
        this.struttura.id,
        this.anno!,
        pgNum - 1,
        pgSize,
        testo,
        undefined
      )
      .subscribe(
        (result: any) => {
          this.loading = false;
          this.risorse = result.content;
          this.paginationService?.updateCount(result.totalElements);
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }

  refreshDataTable() {
    //console.log("refreshDataTable:", this.currentStruttura);
    if (!this.currentStruttura) return;
    this.loading = true;
    let sub: Subscription = this.strutturaService
      .getStruttura(this.currentStruttura.id)
      .subscribe((result) => {
        this.loading = false;
        this.datiStruttura = result;
        this.intestazione = result.intestazione;
        this.tipoStruttura=result.tipoStruttura?.descrizione;
        this.tipologiaStruttura = result.tipologiaStruttura?.descrizione;
        this.descrizione = result.descrizione;
        (this.interim = result.interim),
          (this.inizioValidita = this.fieldsService.convertDate(
            result.inizioValidita
          ));
        this.fineValidita = this.fieldsService.convertDate(result.fineValidita);

        this.codice = result.codice;
        this.codiceRidotto = result.codiceRidotto;
        this.totaleRisorse = result.totaleRisorse;
        this.responsabile = result.responsabile;
        this.totaleRisorseEffettive = result.totaleRisorseEffettive;
        this.currentStruttura.item =
          this.datiStruttura.codiceRidotto +
          " " +
          this.datiStruttura.intestazione;

        this.refreshTable();
      });
    this.subs.push(sub);
  }

  aggiornaFigli() {
    this.ricaricaFigli.emit();
  }
  aggiornaFigliPadre(vai: boolean) {
    this.ricaricaFigliPadre.emit(vai);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiungiStrutturaPopup() {
    const modalRef = this.modalService.open(AggiungistrutturaComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idPadre = this.currentStruttura.id;
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

  modificaStrutturaPopup() {
    const modalRef = this.modalService.open(AggiungistrutturaComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentStruttura.id;
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

  getCurrentLevel() {
    if (this.currentStruttura.level) {
      return this.currentStruttura.level - 1;
    }
    return 0;
  }

  eliminaStruttura() {
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina la struttura #" + this.currentStruttura?.id,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.strutturaService
          .deleteStruttura(this.currentStruttura?.id)
          .subscribe(
            (result) => {
              const currentLevel = this.getCurrentLevel();
              this.aggiornaFigli();
              this.aggiornaFigliPadre(true);
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella cancellazione della struttura: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    //console.log("scheda-struttura changes:", changes);
    this.struttura = changes.currentStruttura.currentValue;
    this.reload();
  }
  reload() {
    //console.log("currentStruttura:",this.currentStruttura);
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub1);
    this.refreshDataTable();
    this.paginationService?.reset(false);

    this.paginationService = new PaginationService();
    let sub2: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refreshTable();
      });
    this.subs.push(sub2);

    this.cercaRisorseForm = this.fb.group({
      nome: [""],
    });

    this.closed = {};
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.anno = this.currentUser.operator?.anno;
      }
    );
    this.subs.push(sub);
  }

  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case Azioni.aggiungiStruttura:
        this.aggiungiStrutturaPopup();
        break;
      case Azioni.modificaStruttura:
        this.modificaStrutturaPopup();
        break;
      case Azioni.elimina:
        this.eliminaStruttura();
        break;
    }
  }
  readAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.OrganigrammaStruttura,
      true
    );
  }

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.OrganigrammaStruttura,
      false
    );
  }
}
