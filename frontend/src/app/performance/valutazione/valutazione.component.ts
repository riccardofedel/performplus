import {
  Component,
  OnInit,
  NgZone,
  ViewChild,
  OnDestroy,
  ElementRef,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { FieldsService } from "src/app/_services/fields.service";

import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  NgForm,
  FormControl,
  AbstractControl,
} from "@angular/forms";
import {
  QuestionariService,
  Permission,
  PermissionService,
  RisorseService,
  StampeService,
} from "src/app/_services";
import { Risorsa, User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { ValutazioneService } from "src/app/_services";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import { environment } from "../../../environments/environment";
import { ItemsList } from "@ng-select/ng-select/lib/items-list";
import { AccettazionepopupComponent } from "./accettazione-popup/accettazione-popup.component";
import { SchedaValutatoComponent } from "../valutazioni/scheda-valutato/scheda-valutato.component";
import { saveAs as importedSaveAs } from "file-saver";

enum TipoUtilizzo {
  Ok = "OK",
  UtilizzoParziale = "UTILIZZO_PARZIALE",
}

enum TipoDisponibile {
  Disponibile = "DISPONIBILE",
  NonDisponibile = "NON_DISPONIBILE",
}

@Component({
  selector: "app-valutazione",
  templateUrl: "./valutazione.component.html",
  styleUrls: ["./valutazione.component.scss"],
  providers: [TranslatePipe],
})
export class ValutazioneComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    public questionarioService: QuestionariService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private valutazioneService: ValutazioneService,
    private authenticationService: AuthenticationService,
    public fieldsService: FieldsService,
    private stampeService: StampeService
  ) {}

  loading = false;
  loadedComportamenti = false;
  comportamentiForm!: FormGroup;
  paginationService: PaginationService | undefined;
  subs: Subscription[] = [];
  currentUser: User | undefined;
  selectedTab = "obiettivi";
  idRegistrazione: number | undefined;

  valutazione: any | undefined;
  risultato: any | undefined;
  totali: any | undefined;

  valutatori: any[] = [];
  obiettivi: any[] = [];
  cercaForm!: FormGroup;
  isLoadingValutati = false;
  isLoadingValutatori = false;
  registrazioni: any[] = [];

  statoScheda: any | undefined;
  stampabile = true;
  fase: number | undefined;
  tipoAccettazione = "";
  valutato: number | undefined;
  nomeValutato: string | undefined;
  admin = false;

  scheda: any | undefined;
  ngOnInit() {
    this.cercaForm = this.formBuilder.group({
      idValutatore: [null],
      idRegistrazione: [null, Validators.required],
    });
    this.comportamentiForm = this.formBuilder.group({});
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          //console.log("----valutazione currentUser:", this.currentUser);
          this.admin = this.currentUser.operator?.ruolo == "AMMINISTRATORE";
          this.valutato = this.currentUser.operator?.idRisorsa;
          this.liste();
        }
      }
    );
    this.subs.push(sub);
  }
  liste() {
    this.subs.push(
      this.valutazioneService.getRisorsa(this.valutato!).subscribe(
        (result) => {
          this.nomeValutato = result.cognome + " " + result.nome;
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Valutato non valido"),
            icon: "error",
            confirmButtonText: "Ok",
          });
          return;
        }
      )
    );

    this.subs.push(
      this.valutazioneService
        .getValutatori(
          this.currentUser!.operator!.idEnte,
          this.currentUser!.operator!.anno,
          undefined,
          this.valutato
        )
        .subscribe((result) => {
          this.valutatori = result;
          if (this.valutatori && this.valutatori.length > 0) {
            this.fCerca.idValutatore.setValue(this.valutatori[0].id);
            this.refresh();
          }
        })
    );
  }
  // convenience getter for easy access to form fields
  get fComportamenti() {
    return this.comportamentiForm?.controls;
  }
  get fCerca() {
    return this.cercaForm?.controls;
  }

  onChangeValutazione($event: any) {
    //console.log("valutazione:", $event);
    if (!$event || !$event.idRegistrazione) {
      this.scheda = undefined;
      return;
    }
    this.idRegistrazione = $event.idRegistrazione;
    this.leggiScheda();
    this.leggiStatoScheda();
    this._selectTab();
  }
  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  leggiScheda() {
    if (!this.idRegistrazione) return;
    this.subs.push(
      this.valutazioneService.leggiScheda(this.idRegistrazione!).subscribe({
        next: (result) => {
          this.scheda = result;
        },
        error: (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Valutazione non valida"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        },
      })
    );
  }

  leggiStatoScheda() {
    this.tipoAccettazione = "";
    this.fase = 0;
    if (!this.idRegistrazione) return;

    this.subs.push(
      this.valutazioneService
        .statoScheda(this.idRegistrazione)
        .subscribe((result) => {
          this.statoScheda = result;
          if (this.statoScheda) {
            this.fase = this.statoScheda.dataAccettazioneScheda ? 2 : 1;
            this.tipoAccettazione =
              this.fase == 1
                ? "presa visione scheda"
                : "presa visione valutazione";
          }
        })
    );
  }
  selectTab(tab: string) {
    this.selectedTab = tab;
    this._selectTab();
  }
  _selectTab() {
    if (!this.selectedTab) return;
    else if (this.selectedTab == "obiettivi") {
      this.reloadObiettivi();
    } else if (this.selectedTab == "risultato") {
      this.reloadRisultato();
    } else if (this.selectedTab == "totali") {
      this.reloadTotali();
    } else if (this.selectedTab == "statoScheda") {
      //
    }
  }
  reloadObiettivi() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.valutazioneService
      .getObiettivi(this.idRegistrazione!)
      .subscribe((result) => {
        this.loading = false;
        this.obiettivi = result;
      });
    this.subs.push(sub);
  }
  reloadRisultato() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.questionarioService
      .getRisultatoValutazione(this.idRegistrazione!)
      .subscribe((result) => {
        this.loading = false;
        this.risultato = result;
      });
    this.subs.push(sub);
  }
  reloadTotali() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.valutazioneService
      .totali(this.idRegistrazione!)
      .subscribe((result) => {
        this.loading = false;
        this.totali = result;
      });
    this.subs.push(sub);
  }

  refreshComportamenti() {
    //console.log("--------refreshComportamenti");
    this.comportamentiForm = this.formBuilder.group({});
    var values: any = {};
    for (let item of this.valutazione.items) {
      if (item.risposta && item.selected) {
        values["c_" + item.gruppo] = item.id;
      }
    }
    var controls: any = {};
    for (let item of this.valutazione.items) {
      if (item.gruppo && item.id == item.gruppo) {
        let v = values["c_" + item.gruppo];
        this.comportamentiForm.addControl(
          "c_" + item.gruppo,
          new FormControl<number | null>(v ? v : null, Validators.required)
        );
      }
    }
  }
  refresh() {
    //console.log("---refresh");
    if (this.vuoto(this.fCerca.idValutatore.value)) {
      return;
    }
    this.loading = true;

    this.registrazioni = [];
    this.idRegistrazione = undefined;
    const sort = "";
    let sub: Subscription = this.valutazioneService
      .registrazioniAll(this.fCerca!.idValutatore!.value, this.valutato!)
      .subscribe((result) => {
        this.loading = false;
        if (result) {
          this.registrazioni = result;
          if (this.registrazioni && this.registrazioni.length >= 1) {
            this.idRegistrazione = this.registrazioni[0].idRegistrazione;
            this.fCerca!.idRegistrazione!.setValue(this.idRegistrazione);
          }
          this.leggiScheda();
          this.leggiStatoScheda();
          this._selectTab();
        }
      });
    this.subs.push(sub);
  }

  loadValutatori(idValutato: number) {
    this.valutatori = [];
    this.isLoadingValutatori = true;
    let sub: Subscription = this.valutazioneService
      .getValutatori(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        undefined,
        idValutato
      )
      .subscribe((result) => {
        this.isLoadingValutatori = false;
        let trovato = false;
        this.valutatori = result;
        if (this.valutatori.length == 1) {
          this.fCerca.idValutatori.setValue(this.valutatori[0].id);
          trovato = true;
        } else if (
          this.valutatori &&
          !this.vuoto(this.fCerca.idValutatore.value)
        ) {
          this.valutatori.forEach((v: any) => {
            if (v.id == this.fCerca.idValutatore.value) {
              trovato = true;
            }
          });
        }

        if (!trovato) this.fCerca.idValutatore.setValue(null);

        this.refresh();
      });
    this.subs.push(sub);
  }
  onChangeValutatore($event: any) {
    //console.log("onChangeValutatore", $event);
    this.registrazioni = [];
    this.idRegistrazione = undefined;
    this.scheda = undefined;
    this.fCerca.idRegistrazione.setValue(null);
    if (!$event) {
      this.loadValutatori(this.valutato!);
    } else {
      this.refresh();
    }
  }

  pubblicaScheda() {
    const modalRef = this.modalService.open(AccettazionepopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idRegistrazione = this.idRegistrazione;
    modalRef.componentInstance.fase = 1;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.leggiStatoScheda();
        }
      },
      (reason) => {}
    );
  }
  pubblicaValutazione() {
    const modalRef = this.modalService.open(AccettazionepopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idRegistrazione = this.idRegistrazione;
    modalRef.componentInstance.fase = 2;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.leggiStatoScheda();
        }
      },
      (reason) => {}
    );
  }
  pubblicabile(): boolean {
    if (!this.fase) return false;
    if (!this.scheda || this.scheda.interim === true) return false;
    if (this.fase == 1) {
      return (
        this.statoScheda &&
        this.scheda.abilitaInvioScheda === true &&
        this.statoScheda.dataPubblicazioneScheda &&
        !this.statoScheda.dataAccettazioneScheda
      );
    }
    if (this.fase == 2) {
      return (
        this.statoScheda &&
        this.scheda.abilitaInvioValutazione === true &&
        !this.statoScheda.dataAccettazioneValutazione &&
        this.statoScheda.dataPubblicazioneValutazione
      );
    }
    return false;
  }
  schedaValutato() {
    const modalRef = this.modalService.open(SchedaValutatoComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.idValutato = this.valutato;
    modalRef.componentInstance.fase = 2;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.leggiStatoScheda();
        }
      },
      (reason) => {}
    );
  }
  vuoto(d: number | undefined): boolean {
    return d === null || d === undefined;
  }

  stampa() {
    this.loading = true;

    let sub: Subscription = this.stampeService
      .stampaScheda(this.idRegistrazione!)
      .subscribe(
        (result) => {
          const byteCharacters = atob(result.content);
          const byteNumbers = new Array(byteCharacters.length);
          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          const byteArray = new Uint8Array(byteNumbers);
          const blob = new Blob([byteArray], { type: result.contentType });
          importedSaveAs(blob, result.name);
          this.loading = false;
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("errore stampa") + ":" + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }
}
