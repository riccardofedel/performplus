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

import { FormBuilder, FormGroup } from "@angular/forms";
import {
  Permission,
  PermissionService,
  ProgrammazioneService,
  TipoNodo,
} from "src/app/_services";
import { User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { FieldsService } from "src/app/_services/fields.service";
import { NoteassessoriComponent } from "src/app/programmazione/modal/noteassessori/noteassessori.component";
import { AggiunginodoComponent } from "src/app/programmazione/modal/aggiunginodo/aggiunginodo.component";
import { environment } from "src/environments/environment";
import { SimpleChanges, OnChanges } from "@angular/core";
import { Azioni, Schede } from "../../constants";
import { filter } from "rxjs/operators";
import { SpostaNodoComponent } from "../../modal/sposta-nodo/sposta-nodo.component";

enum TipoUtilizzo {
  Ok = "OK",
  UtilizzoParziale = "UTILIZZO_PARZIALE",
}

enum TipoDisponibile {
  Disponibile = "DISPONIBILE",
  NonDisponibile = "NON_DISPONIBILE",
}

@Component({
  selector: "scheda-nodo",
  templateUrl: "./scheda-nodo.component.html",
  styleUrls: ["./scheda-nodo.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaNodoComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    private modalService: NgbModal,
    public permissionService: PermissionService,
    public translate: TranslateService,
    public fb: FormBuilder,
    public fieldsService: FieldsService,
    public programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService
  ) {}

  TipoDisponibile = TipoDisponibile;

  TipoUtilizzo = TipoUtilizzo;

  Permission = Permission;

  nodo: any;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  TipoNodo = TipoNodo;

  paginationService: PaginationService | undefined;

  paginationRisorseService: PaginationService | undefined;

  paginationFigliService: PaginationService | undefined;

  selectedTab = "indicatori";

  loadedRisorse = false;

  //se nell'accordion sono mostrate
  varieShown: any = {};

  anno: number | undefined;
  idEnte: number | undefined;
  annoInizio = 0;
  annoFine = 0;

  @Input() currentProgrammazione: any | undefined;
  @Output() cambiaScheda = new EventEmitter<any>();
  @Output() ricaricaFigli: EventEmitter<any> = new EventEmitter<any>();
  @Output() ricaricaFigliPadre: EventEmitter<any> = new EventEmitter<any>();

  fields: string[] = [];

  risorse: any[] = [];
  responsabili: any[] = [];

  figli: any[] = [];
  tuttiFigli: any[] = [];

  //risorse per tabella nascosta, caricate tutte e mostrate a slice
  risorseAssociate: any[] = [];
  risorseAssociateMostrate: any[] = [];

  isLoadingRisorse = false;
  isLoadingResponsabili = false;
  isLoadingObiettiviStrategici = false;

  //solo quelle che visualizzo
  indicatori: any[] = [];

  //tutte
  tuttiIndicatori: any[] = [];

  closed: any | undefined;

  programmazione: any | undefined;

  cercaRisorseForm!: FormGroup;

  toggleValorePubblico = false;
  toggleBsc = false;

  ngOnInit() {
    this.cercaRisorseForm = this.fb.group({
      nomeRisorsa: [null],
    });
    this.reload();
  }

  resetRisorse() {
    this.loadedRisorse = false;
    this.selectedTab = this.getCurrentNodoTipoId() < 4 ? "figli" : "indicatori";
  }

  selectTab(tab: string) {
    this.selectedTab = tab;
    if (!this.loadedRisorse && tab == "risorse") {
      this.loading = true;
      let sub: Subscription = this.programmazioneService
        .getRisorseProgrammazione(this.nodo?.id)
        .subscribe((result) => {
          this.loading = false;
          this.risorseAssociate = result;
          let nomi: { [key: string]: any[] } = {};
          //metto i nomi ricercabili nell'array
          //per le risorse metto il nome vero
          //per igli obiettivi invece metto i nome della risorsa
          for (let i in this.risorseAssociate) {
            //se sono in ordine (obiettivo dopo la risorsa) funziona
            if (!this.risorseAssociate[i].idNodoPiano) {
              this.risorseAssociate[i].searchable =
                this.risorseAssociate[i].nome;
              nomi[this.risorseAssociate[i].idRisorsaUmana] =
                this.risorseAssociate[i].nome;
            } else {
              this.risorseAssociate[i].searchable =
                nomi[this.risorseAssociate[i].idRisorsaUmana];
            }
          }

          this.paginationRisorseService?.updateCount(
            this.risorseAssociate?.length ? this.risorseAssociate.length : 0
          );
          this.refreshRisorseTable(undefined);
        });
      this.subs.push(sub);
    }
  }

  onSubmitRisorse() {
    //console.log("---onSubmitRisorse");
    this.refreshRisorseTable(
      this.cercaRisorseForm?.controls?.nomeRisorsa?.value
    );
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

  refreshRisorseTable(search: string | undefined) {
    const first = this.paginationRisorseService?.getFirst()
      ? this.paginationRisorseService?.getFirst()
      : 0;
    const last = this.paginationRisorseService?.getLast()
      ? this.paginationRisorseService?.getLast()
      : 0;
    if (search) {
      this.risorseAssociateMostrate = this.risorseAssociate
        ?.filter(
          (val) =>
            val.searchable?.toLowerCase()?.indexOf(search?.toLowerCase()) !== -1
        )
        ?.slice(first - 1, last);
    } else {
      this.risorseAssociateMostrate = this.risorseAssociate?.slice(
        first - 1,
        last
      );
    }

    let odd = false;
    for (let i in this.risorseAssociateMostrate) {
      if (this.risorseAssociateMostrate[i].nodoPiano) {
        odd = !odd;
      }
      this.risorseAssociateMostrate[i].oddRow = odd;
    }
  }
  refreshFigliTable() {
    const first = this.paginationFigliService?.getFirst()
      ? this.paginationFigliService?.getFirst()
      : 0;
    const last = this.paginationFigliService?.getLast()
      ? this.paginationFigliService?.getLast()
      : 0;
    this.figli = this.tuttiFigli?.slice(first - 1, last);
  }

  refreshDataTable() {
    if (!this.currentProgrammazione || !this.currentProgrammazione.id) {
      return;
    }
    //console.log("-----refreshDataTable");
    this.loading = true;
    let sub: Subscription = this.programmazioneService
      .getProgrammazione(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.nodo = result;
        this.fields = result.fields;
        this.loading = false;
        this.annoInizio = result.annoInizio;
        this.annoFine = result.annoFine;
        this.tuttiIndicatori = result.indicatori
          ? result.indicatori.filter((r: any) => {
              return r.idIndicatorePiano !== null;
            })
          : result.indicatori;
        this.paginationService?.updateCount(
          this.tuttiIndicatori?.length ? this.tuttiIndicatori.length : 0
        );
        //console.log("---refreshDataTable");
        this.currentProgrammazione.item =
          this.nodo.codiceRidotto + " " + this.nodo.denominazione;

        this.refreshTable();

        if (this.getCurrentNodoTipoId() < 4) {
          this.loading = true;
          this.subs.push(
            this.programmazioneService
              .getFigli(this.nodo?.id)
              .subscribe((result) => {
                this.loading = false;
                this.tuttiFigli = result;

                this.paginationFigliService?.updateCount(
                  this.tuttiFigli?.length ? this.tuttiFigli.length : 0
                );
                this.refreshFigliTable();
              })
          );
        }
      });
    this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiungiNodoPopup() {
    const modalRef = this.modalService.open(AggiunginodoComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idPadre = this.currentProgrammazione.id;
    modalRef.componentInstance.livello = this.getCurrentNodoTipoId();
    modalRef.componentInstance.tipoNodo = null; //viene popolato dal servizio

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.aggiornaFigli();
        }
      },
      (reason) => {}
    );
  }

  spostaNodoPopup() {
    const modalRef = this.modalService.open(SpostaNodoComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentProgrammazione.id;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.aggiornaFigliPadre(true);
        }
      },
      (reason) => {}
    );
  }

  modificaNodoPopup() {
    const modalRef = this.modalService.open(AggiunginodoComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentProgrammazione.id;
    modalRef.componentInstance.livello = this.getCurrentNodoTipoId();
    modalRef.componentInstance.tipoNodo = this.getCurrentTipo();

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

  elimina() {
    const id = this.currentProgrammazione.id;
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Rimuovi #" + id,
    }).then((result) => {
      if (result.isConfirmed) {
        let sub: Subscription = this.programmazioneService
          .rimuoviProgrammazione(id)
          .subscribe(
            (result) => {
              this.aggiornaFigliPadre(true);
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella rimozione: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }
  aggiornaFigli() {
    this.ricaricaFigli.emit();
  }
  aggiornaFigliPadre(vai: boolean) {
    this.ricaricaFigliPadre.emit(vai);
  }
  gestioneIndicatori() {
    this.cambiaScheda.emit(Schede.indicatori);
  }

  gestionePesatura() {
    this.cambiaScheda.emit(Schede.pesatura);
  }

  getCurrentNodoTipoId() {
    if (this.currentProgrammazione && this.currentProgrammazione.tipoNodo) {
      return this.currentProgrammazione.tipoNodo;
    }
    return 0;
  }

  getCurrentTipo() {
    return this.nodo?.tipoNodo;
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

  noteAssessori() {
    const modalRef = this.modalService.open(NoteassessoriComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = this.currentProgrammazione.id;
    modalRef.componentInstance.livello = this.getCurrentNodoTipoId();
    modalRef.componentInstance.tipoNodo = this.getCurrentTipo();
    modalRef.componentInstance.noteAssessori = this.nodo?.noteAssessori;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          //aggiorno solo la parte a destra
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  canReadNoteAssessore() {
    return this.permissionService.isAdmin(this.currentUser);
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
      Permission.DupProgrammazione,
      false
    );
  }
  addAllowed() {
    if (!this.nodo?.enabling.read) {
      return false;
    }
    if (!this.nodo?.enabling.write) {
      if (this.nodo.tipoNodo === "OBIETTIVO") {
        if (
          this.currentUser?.operator?.ruolo === "POSIZIONE_ORGANIZZATIVA" ||
          this.currentUser?.operator?.ruolo === "REFERENTE"
        ) {
          // ok
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    return this.permissionService.getPermission(
      this.currentUser,
      Permission.DupProgrammazione,
      false
    );
  }
  gestioneRisorse() {
    this.cambiaScheda.emit(Schede.risorse);
  }
  visualizzaField(field: string) {
    return (
      this.fields === undefined ||
      this.fields === null ||
      this.fields.length == 0 ||
      this.fields.some((x) => x === field)
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    //console.log("scheda-nodo changes:", changes);
    this.nodo = changes.currentProgrammazione.currentValue;
    this.reload();
  }
  reload() {
    this.closed = {};
    this.selectedTab = this.getCurrentNodoTipoId() < 4 ? "figli" : "indicatori";
    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser.operator) {
          this.anno = this.currentUser.operator.anno;
          this.idEnte = this.currentUser.operator.idEnte;
        }
      })
    );

    this.refreshDataTable();
    this.paginationService?.reset(false);

    this.paginationService = new PaginationService();
    this.subs.push(
      this.paginationService.getMessage().subscribe((message) => {
        this.refreshTable();
      })
    );

    this.paginationRisorseService = new PaginationService();

    this.subs.push(
      this.paginationRisorseService.getMessage().subscribe((message) => {
        this.refreshRisorseTable(undefined);
      })
    );

    this.paginationFigliService = new PaginationService();
    this.subs.push(
      this.paginationFigliService.getMessage().subscribe((message) => {
        this.refreshFigliTable();
      })
    );
  }

  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {
    switch (cmd) {
      case Azioni.aggiungiNodo:
        this.aggiungiNodoPopup();
        break;
      case Azioni.modificaNodo:
        this.modificaNodoPopup();
        break;
      case Azioni.spostaNodo:
        this.spostaNodoPopup();
        break;
      case Azioni.noteAssessori:
        this.noteAssessori();
        break;
      case Azioni.elimina:
        this.elimina();
        break;
    }
  }

  annoInizioTab(offset = 0) {
    if (!this.annoInizio) {
      return "";
    }
    return this.annoInizio + offset;
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

  isAdmin() {
    return this.permissionService.isAdmin(this.currentUser);
  }
}
