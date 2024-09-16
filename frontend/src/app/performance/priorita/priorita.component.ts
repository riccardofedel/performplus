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
  FormArray,
} from "@angular/forms";
import {
  AlberoStruttureService,
  Permission,
  PermissionService,
  PrioritaPiService,
  RegistrazioniService,
  RisorseService,
  ValutazioneService,
} from "src/app/_services";
import { Risorsa, User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription, take, throwIfEmpty } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
//import { AggiungistrutturaComponent } from './aggiungistruttura/aggiungistruttura.component';
//import { AggiungirisorsaComponent } from './aggiungirisorsa/aggiungirisorsa.component';
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import { ModificaPrioritaComponent } from "./pesaturapopup/pesaturapopup.component";

enum TipoUtilizzo {
  Ok = "OK",
  UtilizzoParziale = "UTILIZZO_PARZIALE",
}

enum TipoDisponibile {
  Disponibile = "DISPONIBILE",
  NonDisponibile = "NON_DISPONIBILE",
}

@Component({
  selector: "app-priorita",
  templateUrl: "./priorita.component.html",
  styleUrls: ["./priorita.component.scss"],
  providers: [TranslatePipe],
})
export class PrioritaComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    public alberoService: AlberoStruttureService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private prioritaService: PrioritaPiService,
    private authenticationService: AuthenticationService,
    private valutazioneService: ValutazioneService,
    public fieldsService: FieldsService,
    private registrazioneService: RegistrazioniService
  ) {}

  loading = false;
  cercaForm!: FormGroup;
  saveForm!: FormGroup;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  items: any[] = [];

  valutati: any[] = [];
  valutatori: any[] = [];
  registrazioni: any[] = [];

  idRegistrazione: number | undefined;

  valutatore: number | undefined;
  nomeValutatore: string | undefined;
  admin = false;

  scheda: any | undefined;

  statoScheda: any | undefined;
  fase: number | undefined;

  ngOnInit() {
    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.admin = this.currentUser.operator?.ruolo == "AMMINISTRATORE";
          if (!this.admin) {
            this.valutatore = this.currentUser.operator?.idRisorsa;
          }
          this.cercaForm = this.formBuilder.group({
            idValutatore: [this.valutatore, Validators.required],
            idValutato: [null, Validators.required],
            idRegistrazione: [null, Validators.required],
          });
          this.liste();
          this.saveForm = this.formBuilder.group({
            pesi: new FormArray<FormGroup>([]),
          });
        }
      })
    );
  }

  refresh() {
    //console.log("---refresh");
    this.leggiScheda();
    this.refreshDataTable();
  }
  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }
  refreshDataTable() {
    //console.log("----refreshDataTable");
    const sort = "";
    if (
      !(
        this.f!.idValutatore!.value &&
        this.f!.idValutato!.value &&
        this.f!.idRegistrazione!.value
      )
    ) {
      return;
    }
    this.items = [];
    this.pesiFormArray.clear();
    this.loading = true;
    let sub: Subscription = this.prioritaService
      .elenco(
        this.f!.idValutatore!.value,
        this.f!.idValutato!.value,
        this.f!.idRegistrazione!.value
      )
      .subscribe(
        (result) => {
          if (result) {
            for (let i = 0; i < result.length; i++) {
              const r = result[i];
              this.items.push(r);
              if (r.indicatori) {
                for (let j = 0; j < r.indicatori.length; j++) {
                  const ind = r.indicatori[j];
                  ind.idRegistrazione = r.idRegistrazione;
                  this.items.push(ind);
                }
              }
            }
            this.refreshTable();
          }
          this.loading = false;
        },
        (error) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }

  refreshTable() {
    //console.log("---refreshTable");
    if (!this.items) {
      return;
    }

    let odd = false;
    for (let i = 0; i < this.items.length; i++) {
      const t = this.items[i];
      if (t.codice) {
        odd = !odd;
      }
      t.oddRow = odd;
      if (t.idNodo) {
        this.pesiFormArray.push(
          this.formBuilder.group({
            pesoNodo: [
              t.peso,
              [Validators.required, Validators.min(0.0), Validators.max(100.0)],
            ],
            pesoIndicatore: [null],
          })
        );
      } else {
        this.pesiFormArray.push(
          this.formBuilder.group({
            pesoNodo: [null],
            pesoIndicatore: [
              t.peso,
              [Validators.required, Validators.min(0.0), Validators.max(100.0)],
            ],
          })
        );
      }
    }
  }
  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  liste() {
    if (!this.vuoto(this.valutatore)) {
      this.loading = true;
      this.subs.push(
        this.valutazioneService.getRisorsa(this.valutatore!).subscribe(
          (result) => {
            this.valutatori = [result];
            this.nomeValutatore = result.cognome + " " + result.nome;
            this.loading = false;
            this.loadValutati(this.valutatore);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("Valutatore non valido"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        )
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
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: this.translate.instant("Valutatori non caricati"),
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          )
      );
    }
  }
  modificaPesoNodo(pr: any) {
    const modalRef = this.modalService.open(ModificaPrioritaComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = pr.id;
    modalRef.componentInstance.idNodo = pr.idNodo;
    modalRef.componentInstance.idIndicatorePiano = pr.idIndicatorePiano;
    modalRef.componentInstance.idRegistrazione = pr.idRegistrazione;
    modalRef.result.then(
      (result) => {
        //console.log("----modifica peso nodo,", result);
        if (result == "refresh") this.refresh();
      },
      (error) => {
        Swal.fire({
          title: this.translate.instant("sorry"),
          text: "Errore nel salvataggio: " + error,
          icon: "error",
          confirmButtonText: "Ok",
        });
      }
    );
  }
  modificaPesoIndicatore(pr: any) {
    const modalRef = this.modalService.open(ModificaPrioritaComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = pr.id;
    modalRef.componentInstance.idNodo = pr.idNodo;
    modalRef.componentInstance.idIndicatorePiano = pr.idIndicatorePiano;
    modalRef.componentInstance.idRegistrazione = pr.idRegistrazione;
    modalRef.result.then(
      (result) => {
        //console.log("----modifica peso indicatore,", result);
        if (result == "refresh") this.refresh();
      },
      (error) => {
        Swal.fire({
          title: this.translate.instant("sorry"),
          text: "Errore nel salvataggio: " + error,
          icon: "error",
          confirmButtonText: "Ok",
        });
      }
    );
  }
  onChangeValutazione($event: any) {
    //console.log("valutazione:", $event);
    if (!$event || !$event.idRegistrazione) {
      return;
    }
    this.idRegistrazione = $event.idRegistrazione;
    this.refresh();
  }
  // VALUTATORE-VALUTATO
  onChangeValutato($event: any) {
    this.refreshVal();
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
          let trovato = false;
          this.valutatori = result;
          if (this.valutatori.length == 1) {
            this.f.idValutatore.setValue(this.valutatori[0].id);
            trovato = true;
          } else if (
            this.valutatori &&
            !this.vuoto(this.f.idValutatore.value)
          ) {
            for (let index = 0; index < this.valutatori.length; index++) {
              const v = this.valutatori[index];
              if (v.id == this.f.idValutatore.value) {
                trovato = true;
              }
            }
          }
          this.loading = false;
          if (!trovato) this.f.idValutatore.setValue(null);
          if (trovato && this.f.idValutato) {
            this.refreshVal();
          } else {
            this.refresh();
          }
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  onChangeValutatore($event: any) {
    //console.log("onChangeValutatore", $event);
    this.scheda = undefined;
    this.loadValutati($event?.id);
    if (!$event) {
      this.f.idValutato.setValue(null);
    }
  }
  loadValutati(idValutatore: number | undefined) {
    this.valutati = [];
    this.registrazioni = [];
    this.idRegistrazione = undefined;
    this.f.idRegistrazione.setValue(null);

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
            let trovato = false;

            this.valutati = result;
            if (this.valutati && this.valutati.length == 1) {
              this.f.idValutato.setValue(this.valutati[0].id);
              trovato = true;
            } else if (this.valutati && !this.vuoto(this.f.idValutato.value)) {
              for (let index = 0; index < this.valutati.length; index++) {
                const v = this.valutati[index];
                if (v.id == this.f.idValutato.value) {
                  trovato = true;
                }
              }
            }
            if (!trovato) this.f.idValutato.setValue(null);
            this.loading = false;
            this.refreshVal();
          },
          error: (error) => {
            this.loading = false;
          },
        })
    );
  }
  refreshVal() {
    //console.log("---refreshVal");
    if (
      this.vuoto(this.f.idValutato.value) ||
      (this.vuoto(this.valutatore) && this.vuoto(this.f.idValutatore.value))
    ) {
      return;
    }
    this.loading = true;

    this.registrazioni = [];
    this.idRegistrazione = undefined;
    const sort = "";
    let sub: Subscription = this.valutazioneService
      .registrazioni(
        this.valutatore ? this.valutatore : this.f!.idValutatore!.value,
        this.f!.idValutato!.value
      )
      .subscribe(
        (result) => {
          this.loading = false;
          if (result) {
            this.registrazioni = result;
            if (this.registrazioni && this.registrazioni.length >= 1) {
              this.idRegistrazione = this.registrazioni[0].idRegistrazione;
              this.f!.idRegistrazione!.setValue(this.idRegistrazione);
            }
            this.refresh();
          }
        },
        (err: any) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }
  vuoto(d: number | undefined): boolean {
    return d === null || d === undefined;
  }

  leggiScheda() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    this.subs.push(
      this.valutazioneService.leggiScheda(this.idRegistrazione!).subscribe({
        next: (result) => {
          this.scheda = result;
          this.loading = false;
          this.leggiStatoScheda();
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
  rimuovoPerformance() {
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Rimuovi Performance organizzativa",
    }).then((result) => {
      if (result.isConfirmed) {
        this.subs.push(
          this.valutazioneService
            .performanceOrganizzativa(
              this.scheda.idSchedaValutazione,
              this.scheda.idRegistrazione!,
              false
            )
            .subscribe({
              next: (result) => {
                this.refresh();
              },
              error: (err) => {
                Swal.fire({
                  title: this.translate.instant("sorry"),
                  text: this.translate.instant("Modifica in errore:" + err),
                  icon: "error",
                  confirmButtonText: "Ok",
                });
              },
            })
        );
      }
    });
  }
  aggiungiPerformance() {
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Passa a Performance organizzativa",
    }).then((result) => {
      if (result.isConfirmed) {
        this.subs.push(
          this.valutazioneService
            .performanceOrganizzativa(
              this.scheda.idSchedaValutazione,
              this.scheda.idRegistrazione!,
              true
            )
            .subscribe({
              next: (result) => {
                this.refresh();
              },
              error: (err) => {
                Swal.fire({
                  title: this.translate.instant("sorry"),
                  text: this.translate.instant("Modifica in errore:" + err),
                  icon: "error",
                  confirmButtonText: "Ok",
                });
              },
            })
        );
      }
    });
  }
  getPesi(): FormArray {
    return this.saveForm.get("pesi") as FormArray;
  }
  onSubmit() {
    this.loading = true;
    const control = <FormArray>this.saveForm.get("pesi");
    const values = control.value;
    let data: any = {
      idRegistrazione: this.idRegistrazione,
      nodi: [],
      indicatori: [],
    };
    for (let i = 0; i < this.items.length; i++) {
      const element = this.items[i];
      const pn = values[i];
      if (element.idNodo) {
        element.peso = pn.pesoNodo;
        data.nodi.push({ id: element.idNodo, peso: element.peso });
      } else {
        element.peso = pn.pesoIndicatore;
        data.indicatori.push({
          id: element.idIndicatorePiano,
          peso: element.peso,
        });
      }
    }

    this.subs.push(
      this.prioritaService.aggiornaPesi(data).subscribe(
        (result: any) => {
          Swal.fire({
            text: "Aggiornamento avvenuto con successo",
            icon: "success",
            showConfirmButton: false,
            timer: 1000,
          });
          this.loading = true;
          this.refresh();
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("Aggiornamento non corretto"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      )
    );
  }
  get pesiFormArray() {
    return <FormArray<FormGroup>>this.saveForm.get("pesi");
  }

  completaPesatura() {
    // pesa tutto
    // this.loading=true;
    //console.log("-----aggiornaScheda");
    const nodi: any[] = [];
    let peso = 0.0;
    let conPeso = 0;
    let p = 0.0;
    this.items.forEach((e, index) => {
      if (e.idNodo) {
        nodi.push(index);
        if (e.peso && e.peso > 0) {
          peso = peso + e.peso;
          conPeso = conPeso + 1;
        }
      }
    });
    if (conPeso < nodi.length && peso < 100.0) {
      p = (100.0 - peso) / (nodi.length - conPeso);
    }
    nodi.forEach((n, index) => {
      if (!this.items[n].peso || this.items[n].peso === 0) {
        this.items[n].peso = p.toFixed(2);
        this.pesiFormArray.value[n].pesoNodo = this.items[n].peso;
      }
      if (!this.items[n].sommaPesi || this.items[n].sommaPesi === 0) {
        const m =
          index < nodi.length - 1 ? nodi[index + 1] - 1 : this.items.length - 1;
        const nf = m - n;
        const pf = 100.0 / nf;
        for (let t = n + 1; t <= m; t++) {
          const element = this.items[t];
          element.peso = pf.toFixed(2);
          this.pesiFormArray.value[t].pesoIndicatore = element.peso;
        }
        this.items[n].sommaPesi = 100.0;
      }
    });
  }
  leggiStatoScheda() {
    if (!this.idRegistrazione) return;
    this.loading = true;
    this.subs.push(
      this.valutazioneService.statoScheda(this.idRegistrazione).subscribe(
        (result) => {
          this.loading = false;
          this.statoScheda = result;
          if (this.statoScheda) {
            this.fase = this.statoScheda.dataPubblicazioneScheda ? 2 : 1;
          }
        },
        (error) => {
          this.loading = false;
        }
      )
    );
  }
}
