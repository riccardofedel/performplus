import {
  Component,
  OnInit,
  NgZone,
  ViewChild,
  OnDestroy,
  ElementRef,
  Input,
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
import { Subject, Subscription, take, takeUntil } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { ValutazioneService, RegistrazioniService } from "src/app/_services";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import { environment } from "../../../environments/environment";
import { ItemsList } from "@ng-select/ng-select/lib/items-list";
import { PubblicapopupComponent } from "./pubblicazione-popup/pubblicazione-popup.component";
import { SchedaValutatoComponent } from "./scheda-valutato/scheda-valutato.component";
import { saveAs as importedSaveAs } from "file-saver";

@Component({
  selector: "app-valutazioni",
  templateUrl: "./valutazioni.component.html",
  styleUrls: ["./valutazioni.component.scss"],
  providers: [TranslatePipe],
})
export class ValutazioniComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    public questionarioService: QuestionariService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private valutazioneService: ValutazioneService,
    private registrazioneService: RegistrazioniService,
    private authenticationService: AuthenticationService,
    public fieldsService: FieldsService,
    private stampeService: StampeService
  ) {
    this.valutatoRequest =
      this.router.getCurrentNavigation()!.extras.state?.idValutato;
    this.valutatoreRequest =
      this.router.getCurrentNavigation()!.extras.state?.idValutatore;
    this.registrazioneRequest =
      this.router.getCurrentNavigation()!.extras.state?.idRegistrazione;
  }

  loading = false;
  loadedComportamenti = false;
  comportamentiForm!: FormGroup;
  paginationService: PaginationService | undefined;
  subs: Subscription[] = [];
  currentUser: User | undefined;
  selectedTab = "obiettivi";
  idRegistrazione: number | undefined;
  idValutato: number | undefined;
  valutazione: any | undefined;
  risultato: any | undefined;
  totali: any | undefined;
  valutati: any[] = [];
  valutatori: any[] = [];
  obiettivi: any[] = [];
  cercaForm!: FormGroup;

  registrazioni: any[] = [];
  scheda: any | undefined;

  statoScheda: any | undefined;
  stampabile = true;
  fase: number | undefined;
  tipoPubblicazione = "";
  valutatore: number | undefined;
  nomeValutatore: string | undefined;
  admin = false;

  valutatoreRequest: number | undefined;
  valutatoRequest: number | undefined;
  registrazioneRequest: number | undefined;

  obiettiviPesati = true;

  ngOnInit() {
   //("---------->>>VALUTAZIONI");
    this.obiettiviPesati = true;
    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.admin =
            this.currentUser.operator?.ruolo == "AMMINISTRATORE" ||
            this.currentUser.operator?.ruolo == "SUPPORTO_SISTEMA";
          if (this.admin) {
            if (!this.vuoto(this.valutatoRequest)) {
              this.idValutato = this.valutatoRequest;
            }
            if (!this.vuoto(this.registrazioneRequest)) {
              this.idRegistrazione = this.registrazioneRequest;
            }
          } else {
              this.valutatore = this.currentUser.operator?.idRisorsa;
          }

          this.cercaForm = this.formBuilder.group({
            idValutatore: [
              this.valutatoreRequest ? this.valutatoRequest : this.valutatore,
              Validators.required,
            ],
            idValutato: [this.valutatoRequest, Validators.required],
            idRegistrazione: [this.registrazioneRequest, Validators.required],
          });
          this.comportamentiForm = this.formBuilder.group({});
          this.liste();
        }
      })
    );
  }

  liste() {
    if (!this.vuoto(this.registrazioneRequest)) {
      this.caricaRegistrazione();
    } else if (!this.vuoto(this.valutatoRequest)) {
      this.caricaValutato();
    } else if (!this.vuoto(this.valutatoreRequest)) {
      this.caricaValutatore();
    } else {
      this.caricaListe();
    }
  }

  caricaListe() {
    this.loading = true;
    if (!this.vuoto(this.valutatore)) {
      this.subs.push(
        this.valutazioneService.getRisorsa(this.valutatore!).subscribe({
          next: (result) => {
            this.loading = false;
            this.valutatori = [result];
            this.nomeValutatore = result.cognome + " " + result.nome;
            this.loadValutati(this.valutatore);
          },
          error: (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("Valutatore non valido"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          },
        })
      );
    } else {
      this.loading = true;
      this.subs.push(
        this.valutazioneService
          .getValutatori(
            this.currentUser!.operator!.idEnte,
            this.currentUser!.operator!.anno,
            undefined,
            undefined
          )
          .subscribe(
            (result) => {
              this.valutatori = result;
              this.loading = false;
            },
            (error) => {
              this.loading = false;
            }
          )
      );
    }
  }
  caricaValutato() {
    this.fCerca.idValutato.setValue(this.valutatoRequest);
    this.loading = true;
    this.subs.push(
      this.valutazioneService.getRisorsa(this.valutatoRequest!).subscribe({
        next: (result) => {
          this.loadValutatori(this.valutatoRequest);
          this.valutati = [result];
          this.loading = false;
          this.refresh();
        },
        error: (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Valutato non valido"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        },
      })
    );
  }

  caricaValutatore() {
    this.fCerca.idValutatore.setValue(this.valutatoreRequest);
    this.loading = true;
    this.subs.push(
      this.valutazioneService.getRisorsa(this.valutatoreRequest!).subscribe({
        next: (result) => {
          this.valutatori = [result];
          this.loading = false;
          this.loadValutati(this.valutatoreRequest);
        },
        error: (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Valutato non valido"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        },
      })
    );
  }
  caricaRegistrazione() {
    //console.log("----caricaRegistrazione");
    this.loading = true;
    this.subs.push(
      this.valutazioneService
        .leggiScheda(this.registrazioneRequest!)
        .subscribe({
          next: (result) => {
            this.registrazioni = [result];
            this.idRegistrazione = result.idRegistrazione;
            this.scheda = result;
            this.registrazioneService
              .getRegistrazione(this.registrazioneRequest!)
              .subscribe({
                next: (reg) => {
                  this.loading = false;
                  this.fCerca.idValutatore.setValue(reg.valutatore.id);
                  this.fCerca.idValutato.setValue(reg.valutato.id);
                  this.fCerca.idRegistrazione.setValue(reg.id);
                  this.valutatori = [reg.valutatore];
                  this.valutati = [reg.valutato];
                  this.loading = false;
                  this.leggiStatoScheda();
                  this._selectTab();
                },
                error: (err) => {
                  this.loading = false;
                  Swal.fire({
                    title: this.translate.instant("sorry"),
                    text: this.translate.instant("Registrazione non valida"),
                    icon: "error",
                    confirmButtonText: "Ok",
                  });
                },
              });
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
  // convenience getter for easy access to form fields
  get fComportamenti() {
    return this.comportamentiForm?.controls;
  }
  get fCerca() {
    return this.cercaForm?.controls;
  }
  onSubmitComportamenti() {
    this.loading = true;
    var selected: number[] = [];
    Object.keys(this.fComportamenti).forEach((key) => {
      selected.push(+this.fComportamenti[key].value);
    });
    var data: any = {};
    data.idRegistrazione = this.idRegistrazione!;
    data.risposte = selected;

    let sub: Subscription = this.valutazioneService
      .salvaComportamenti(data)
      .subscribe({
        next: (result: any) => {
          this.loading = false;
          this.reloadScheda(this.idRegistrazione!);
          this.reloadComportamenti();
        },
        error: (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("errore nel salvataggio"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        },
      });
    this.subs.push(sub);
  }

  onChangeValutazione($event: any) {
    //console.log("valutazione:", $event);
    if (!$event || !$event.idRegistrazione) {
      return;
    }
    this.idRegistrazione = $event.idRegistrazione;
    this.leggiScheda();
  }
  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  leggiScheda() {
    this.loading = true;
    this.subs.push(
      this.valutazioneService.leggiScheda(this.idRegistrazione!).subscribe({
        next: (result) => {
          this.scheda = result;
          this.loading = false;
          this.leggiStatoScheda();
          this._selectTab();
        },
        error: (err) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Registrazione non valida"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        },
      })
    );
  }
  leggiStatoScheda() {
    this.tipoPubblicazione = "";
    this.fase = 0;
    if (!this.idRegistrazione) return;
    this.idValutato = this.fCerca.idValutato.value;
    this.loading = true;
    this.subs.push(
      this.valutazioneService.statoScheda(this.idRegistrazione).subscribe(
        (result) => {
          this.loading = false;
          this.statoScheda = result;
          if (this.statoScheda) {
            this.fase = this.statoScheda.dataPubblicazioneScheda ? 2 : 1;
            this.tipoPubblicazione =
              this.fase == 1 ? "invio scheda" : "invio valutazione";
          }
        },
        (error) => {
          this.loading = false;
        }
      )
    );
  }
  selectTab(tab: string) {
    this.selectedTab = tab;
    this._selectTab();
  }
  _selectTab() {
    if (!this.selectedTab) return;
    if (!this.loadedComportamenti && this.selectedTab == "comportamenti") {
      this.reloadComportamenti();
    } else if (this.selectedTab == "obiettivi") {
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
    this.obiettiviPesati = true;
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.valutazioneService
      .getObiettivi(this.idRegistrazione!)
      .subscribe(
        (result) => {
          this.loading = false;
          this.obiettivi = result;
          //console.log("------verifica-----");
          this.obiettiviPesati = this.verificaCompleto(this.obiettivi);
          if (this.obiettiviPesati === false) {
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant(
                "Obiettivi/indicatori non completamente pesati"
              ),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        },
        (error) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  verificaCompleto(obiettivi: any[]): boolean {
    console.log("---verificaCompleto");
    if (
      this.scheda.performanceOrganizzativa === true ||
      !obiettivi ||
      obiettivi.length == 0
    )
      return true;
    let peso = 0;
    let pesoInd: number[] = [];
    let k = -1;
    for (let index = 0; index < obiettivi.length; index++) {
      const ob = obiettivi[index];
      if (ob.idSchedaValutazioneObiettivo) {
        k = k + 1;
        if (ob.pesoObiettivo) peso = peso + ob.pesoObiettivo;
        if (ob.pesoObiettivo) {
          pesoInd.push(0.0);
        } else{
          pesoInd.push(-100);
        }
      } else if (ob.idSchedaValutazioneIndicatore) {
        if (pesoInd[k]>=0){
          if (ob.pesoIndicatore) pesoInd[k] = pesoInd[k] + ob.pesoIndicatore;
        }
      }
    }
    let verifica = true;
    if (peso !== 100.0) {
      verifica = false;
    } else {
      for (let index = 0; index < pesoInd.length; index++) {
        const p = pesoInd[index];
        if (p !== 100.0 && p>=0) {
          verifica = false;
        }
      }
    }
    return verifica;
  }
  reloadRisultato() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.questionarioService
      .getRisultatoValutazione(this.idRegistrazione!)
      .subscribe(
        (result) => {
          this.loading = false;
          this.risultato = result;
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  reloadTotali() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.valutazioneService
      .totali(this.idRegistrazione!)
      .subscribe(
        (result) => {
          this.loading = false;
          this.totali = result;
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  reloadComportamenti() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    let sub: Subscription = this.questionarioService
      .getValutazione(this.idRegistrazione!)
      .subscribe(
        (result) => {
          this.loading = false;
          this.valutazione = result;
          this.refreshComportamenti();
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  refreshComportamenti() {
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
    if (
      this.vuoto(this.fCerca.idValutato.value) ||
      (this.vuoto(this.valutatore) &&
        this.vuoto(this.fCerca.idValutatore.value))
    ) {
      return;
    }

    this.loading = true;

    this.registrazioni = [];
    this.idRegistrazione = undefined;
    const sort = "";
    this.subs.push(
      this.valutazioneService
        .registrazioni(
          this.vuoto(this.valutatore)
            ? this.fCerca!.idValutatore!.value
            : this.valutatore,
          this.fCerca!.idValutato!.value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            if (result) {
              this.registrazioni = result;
              if (this.registrazioni && this.registrazioni.length >= 1) {
                this.idRegistrazione = this.registrazioni[0].idRegistrazione;
                this.fCerca!.idRegistrazione!.setValue(this.idRegistrazione);
              }
              this.leggiScheda();
            }
          },
          (err: any) => {
            this.loading = false;
          }
        )
    );
  }

  onChangeValutato($event: any) {
    //console.log("onChangeValutato;", $event);
    this.registrazioni = [];
    this.idRegistrazione = undefined;
    this.fCerca.idRegistrazione.setValue(null);
    this.scheda = undefined;
    this.refresh();
  }

  loadValutatori(idValutato: number | undefined) {
    this.valutatori = [];
    this.loading = true;
    let sub: Subscription = this.valutazioneService
      .getValutatori(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        undefined,
        idValutato
      )
      .subscribe(
        (result) => {
          this.loading = false;
          let trovato = false;
          this.valutatori = result;
          if (this.valutatori.length == 1) {
            this.fCerca.idValutatore.setValue(this.valutatori[0].id);
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
        },
        (err: any) => {
          this.loading = false;
          //console.log(err);
        }
      );
    this.subs.push(sub);
  }
  onChangeValutatore($event: any) {
    //console.log("onChangeValutatore", $event);
    this.registrazioni = [];
    this.idRegistrazione = undefined;
    this.fCerca.idRegistrazione.setValue(null);
    this.scheda = undefined;
    if (!this.valutatoRequest) {
      this.loadValutati($event.id);
    } else {
      this.caricaRegistrazioni($event.id, this.valutatoRequest);
    }
  }
  loadValutati(idValutatore: number | undefined) {
    this.valutati = [];
    this.loading = true;

    this.subs.push(
      this.valutazioneService
        .getValutati(
          this.currentUser!.operator!.idEnte,
          this.currentUser!.operator!.anno,
          undefined,
          idValutatore
        )
        .subscribe({
          next: (result) => {
            this.loading = false;
            let trovato = false;
            this.valutati = result;
            if (this.valutati && this.valutati.length == 1) {
              this.fCerca.idValutato.setValue(this.valutati[0].id);
              trovato = true;
            } else if (
              this.valutati &&
              !this.vuoto(this.fCerca.idValutato.value)
            ) {
              this.valutati.forEach((v: any) => {
                if (v.id == this.fCerca.idValutato.value) {
                  trovato = true;
                }
              });
            }

            if (!trovato) this.fCerca.idValutato.setValue(null);
            this.refresh();
          },
          error: (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("Load Valutati error"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          },
        })
    );
  }

  pubblicaScheda() {
    const modalRef = this.modalService.open(PubblicapopupComponent, {
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
    const modalRef = this.modalService.open(PubblicapopupComponent, {
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
    //console.log("---pubblicabile", this.admin);
    if (this.admin) return false;
    if (!this.fase) return false;
    if (!this.scheda || this.scheda.interim === true) return false;
    if (this.fase == 1) {
      return (
        this.statoScheda &&
        this.scheda.abilitaInvioScheda === true &&
        this.obiettivi &&
        this.obiettivi.length > 0 &&
        !this.statoScheda.dataPubblicazioneScheda
      );
    }
    if (this.fase == 2) {
      return (
        this.statoScheda &&
        this.scheda.abilitaInvioValutazione === true &&
        !this.statoScheda.dataPubblicazioneValutazione
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
    modalRef.componentInstance.idValutato = this.idValutato;
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

  caricaRegistrazioni(valutatore: number, valutato: number) {
    this.loading = true;
    this.subs.push(
      this.valutazioneService.registrazioni(valutatore, valutato).subscribe(
        (result) => {
          this.loading = false;
          if (result) {
            this.registrazioni = result;
            if (this.registrazioni && this.registrazioni.length >= 1) {
              this.idRegistrazione = this.registrazioni[0].idRegistrazione;
              this.fCerca!.idRegistrazione!.setValue(this.idRegistrazione);
            }
            this.leggiScheda();
          }
        },
        (err: any) => {
          this.loading = false;
        }
      )
    );
  }

  aggiornaScheda() {
    this.subs.push(
      this.registrazioneService
        .aggiornaScheda(this.idRegistrazione!)
        .subscribe({
          next: (result) => {
            this.reloadScheda(this.idRegistrazione!);
            this._selectTab();
          },
          error: (error) => {
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("Aggiornamento in errore"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          },
        })
    );
  }
  reloadScheda(id: number) {
    this.loading = true;
    this.subs.push(
      this.valutazioneService.leggiScheda(id).subscribe({
        next: (result) => {
          this.loading = false;
          this.scheda = result;
        },
        error: (error) => {
          this.loading = false;
        },
      })
    );
  }
  stampa() {
    this.loading = true;

    let sub: Subscription = this.stampeService
      .stampaScheda(this.cercaForm.value.idRegistrazione)
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
