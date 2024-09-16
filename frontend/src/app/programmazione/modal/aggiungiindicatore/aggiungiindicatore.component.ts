import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { Subscription } from "rxjs";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormArray,
  FormControl,
  MinValidator,
} from "@angular/forms";
import {
  NgbActiveModal,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";
import {
  AuthenticationService,
  IndicatoriService,
  ProgrammazioneService,
} from "src/app/_services";
import { __asyncValues } from "tslib";
import { NgbDateCustomParserFormatter } from "src/app/_helpers/dateformat";
import { FieldsService } from "src/app/_services/fields.service";
import { User } from "src/app/_models";

@Component({
  templateUrl: "aggiungiindicatore.component.html",
  styleUrls: ["aggiungiindicatore.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class AggiungiindicatoreComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService,
    private indicatoriService: IndicatoriService,
    private fieldsService: FieldsService
  ) {}

  //parametri: per modificare
  @Input() id: number | undefined;

  @Input() idNodoPiano: number | undefined;

  @Input() readOnly = false;

  target: any = null;

  range = [];

  creaForm!: FormGroup;

  targetForm!: FormGroup;

  rangeForm!: FormGroup;

  codiceCompleto: string | undefined;

  loading = false;

  targetAnno: any | undefined;
  storicoAnno: any | undefined;

  anno: number | undefined;

  subs: Subscription[] = [];

  crescenti = [
    { value: "CRESCENTE", descrizione: "crescente" },
    { value: "DECRESCENTE", descrizione: "decrescente" },
  ];
  tipiIndicatore = ["Indicatore di risultato", "Indicatore di Valore Pubblico"];
  listaProspettve = [
    "crescita e innovazione",
    "economico-finanziaria",
    "processi interni",
    "soddisfazione utente",
  ];
  dimensioni = [
    "ambientale",
    "economico",
    "salute interna",
    "sanitario",
    "sociale",
  ];

  annoInizio = 0;
  //tipiIndicatore = [];

  indicatoriFormula = [];

  tipoFormula: any | undefined;

  page: string = "";

  setPage(page: string) {
    /**
     * o salvo o perdo i dati del tab
     * altrimenti sarebbero incoerenti
     */
    if (!this.page || this.page == "indicatore") {
      if (this.creaForm?.dirty) {
        Swal.fire({
          title: this.translate.instant("modifiche non salvate"),
          showDenyButton: true,
          showCancelButton: false,
          confirmButtonText: "Continua",
        }).then((result) => {
          if (result.isConfirmed) {
            this.ricaricaIndicatore();
            this.page = page;
          } else {
          }
        });
      } else {
        this.page = page;
      }
    } else {
      this.page = page;
    }
  }

  ngOnInit() {
    //console.log(">>>>>>>", this.id);
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.anno = this.currentUser.operator?.anno;
          this.loading = true;
          if (this.id) {
            this.ricaricaIndicatore();

            this.ricaricaTarget();

            this.ricaricaRange();
          } else {
            this.loading = false;

            let sub2: Subscription = this.indicatoriService
              .search(this.currentUser?.operator?.idEnte, 0, 100, null, null)
              .subscribe((result2) => {
                this.indicatoriFormula = result2.content;
              });
            this.subs.push(sub2);
            this.creaForm = this.formBuilder.group({
              specifica: [null, Validators.required],
              idIndicatore: [null, Validators.required],
              note: [""],
              strategico: [false],
              sviluppoSostenibile: [false],
              specificaNumeratore: [""],
              specificaDenominatore: [""],
              specificaPercentuale: [false],
              specificaDecimali: [null, [Validators.min(0), Validators.max(8)]],
              specificaDecimaliA: [
                null,
                [Validators.min(0), Validators.max(8)],
              ],
              specificaDecimaliB: [
                null,
                [Validators.min(0), Validators.max(8)],
              ],
              crescente: ["CRESCENTE"],
              dimensione: [null],
              fonte: [null],
              tipoIndicatore: [null],
              scadenzaIndicatore: [null],
              baseline: [null],
              prospettiva: [null],
            });

            this.checkChange();
          }
        }
      }
    );
    this.subs.push(sub1);
  }

  ricaricaRange() {
    let sub: Subscription = this.programmazioneService
      .getIndicatoreRange(this.idNodoPiano, this.id)
      .subscribe((result) => {
        this.range = result;
        let arrayForm = [];
        for (let i of result) {
          arrayForm.push(
            this.formBuilder.group({
              id: i.id,
              minimo: i.minimo,
              massimo: i.massimo,
              valore: i.valore,
              proporzionale: i.proporzionale,
            })
          );
        }
        this.rangeForm = this.formBuilder.group({
          range: this.formBuilder.array(arrayForm),
        });
      });
    this.subs.push(sub);
  }

  ricaricaTarget() {
    let sub: Subscription = this.programmazioneService
      .getIndicatoreTarget(this.idNodoPiano, this.id)
      .subscribe((result) => {
        this.target = result;
        this.annoInizio = result.annoInizio;

        if (result.targets) {
          result.targets.forEach((t: any) => {
            if (this.anno == t.anno && t.periodo == 4) {
              t.enabled = true;
              this.targetAnno = t;
            }
          });
        }
        if (result.storici) {
          result.storici.forEach((t: any) => {
            if (this.anno == t.anno && t.periodo == 4) {
              t.enabled = true;
              this.storicoAnno = t;
            }
          });
        }

        this.targetForm = this.formBuilder.group({
          //peso: [result.peso, [Validators.min(0), Validators.max(100)]],
        });

        this.raggruppa("storico", this.storicoAnno);
        this.raggruppa("target", this.targetAnno);
      });
    this.subs.push(sub);
  }
  raggruppa(nome: string, valutazione: any) {
    //console.log(">>>>>>>>>>raggruppa");
    let fg = new FormGroup({});
    let t = valutazione;
    let enab = t ? t.enabled : false;
    let vbo = t ? t.valoreBooleano : null;
    let cbo = new FormControl(vbo);
    if (!enab) cbo.disable({ onlySelf: true });
    fg.addControl("valoreBooleano", cbo);

    let vn = t ? t.valoreNumerico : null;
    let percentuale = t ? t.percentuale : false;
    //console.log("percentuale",percentuale)
    if (vn && percentuale) vn = Math.trunc(vn * 100);
    if (!enab) {
      fg.addControl(
        "valoreNumerico",
        new FormControl({ value: vn, disabled: true })
      );
    } else {
      if (percentuale) {
        fg.addControl(
          "valoreNumerico",
          new FormControl(vn, [Validators.min(0), Validators.max(100)])
        );
      } else {
        fg.addControl("valoreNumerico", new FormControl(vn));
      }
    }

    let va = t ? t.valoreNumericoA : null;
    let ca = new FormControl(va);
    if (!enab) ca.disable({ onlySelf: true });
    fg.addControl("valoreNumericoA", ca);

    let vb = t ? t.valoreNumericoB : null;
    let cb = new FormControl(vb);
    if (!enab) cb.disable({ onlySelf: true });
    fg.addControl("valoreNumericoB", cb);

    fg.addControl("valoreTemporale", cbo);
    let vt = t ? t.valoreTemporale : null;
    let vto = this.fieldsService.convertTimestampzToObject(vt);
    let ct = new FormControl(vto);
    if (!enab) ct.disable({ onlySelf: true });
    fg.addControl("valoreTemporale", ct);
    if (!enab) fg.disable({ onlySelf: true });
    this.targetForm.addControl(nome, fg);
  }

  ricaricaIndicatore() {
    let sub1: Subscription = this.programmazioneService
      .getIndicatorePiano(this.idNodoPiano, this.id)
      .subscribe((result) => {
        this.tipoFormula = result?.indicatore.tipoFormula;
        this.creaForm = this.formBuilder.group({
          specifica: [result.specifica, Validators.required],
          idIndicatore: [result.indicatore?.id, Validators.required],
          note: [result.note],
          strategico: [result.strategico],
          sviluppoSostenibile: [result.sviluppoSostenibile],
          specificaNumeratore: [result.specificaNumeratore],
          specificaDenominatore: [result.specificaDenominatore],
          specificaPercentuale: [result.specificaPercentuale],
          specificaDecimali: [
            result.specificaDecimali,
            [Validators.min(0), Validators.max(8)],
          ],
          specificaDecimaliA: [
            result.specificaDecimaliA,
            [Validators.min(0), Validators.max(8)],
          ],
          specificaDecimaliB: [
            result.specificaDecimaliB,
            [Validators.min(0), Validators.max(8)],
          ],
          crescente: [
            result.decrescente === true ? "DECRESCENTE" : "CRESCENTE",
          ],
          dimensione: [result.dimensione],
          fonte: [result.fonte],
          tipoIndicatore: [result.tipoIndicatore],
          scadenzaIndicatore: [result.scadenzaIndicatore],
          baseline: [result.baseline],
          prospettiva: [result.prospettiva],
        });
        this.loading = false;
        let sub2: Subscription = this.indicatoriService
          .search(this.currentUser?.operator?.idEnte, 0, 100, null, null)
          .subscribe((result2) => {
            this.indicatoriFormula = result2.content;
            this.updateIndicatore(result.indicatore?.id);
          });
        this.subs.push(sub2);
        this.checkChange();
      });
    this.subs.push(sub1);
  }

  getRanges(): FormArray {
    return this.rangeForm.get("range") as FormArray;
  }

  removeRangeRow(i: number) {
    const control = <FormArray>this.rangeForm.get("range");
    control.removeAt(i);
  }

  addRangeRow() {
    const control = <FormArray>this.rangeForm.get("range");
    //cerco il massimo più grande
    let massimoTemp = null;
    let minimoTemp = null;

    let incrementoIntero = 1;
    let incrementoDecimale = 0.01;
    if (this.rangeForm.get("range")?.value) {
      for (let i of this.rangeForm.get("range")?.value) {
        if (minimoTemp === null || i.minimo < minimoTemp) {
          minimoTemp = i.minimo;
        }
        if (massimoTemp === null || i.massimo > massimoTemp) {
          massimoTemp = i.massimo;
        }
      }
    }

    let massimo = null;
    let minimo = null;

    if (this.f?.crescente?.value === "DECRESCENTE") {
      if (minimoTemp !== null) {
        if (Math.ceil(minimoTemp) === minimo) {
          minimo = minimoTemp - incrementoIntero;
        } else {
          minimo = minimoTemp - incrementoDecimale;
        }
      }
    } else {
      if (massimoTemp !== null) {
        if (Math.ceil(massimoTemp) === massimoTemp) {
          massimo = massimoTemp + incrementoIntero;
        } else {
          massimo = massimoTemp + incrementoDecimale;
        }
      }
    }
    control.push(
      this.formBuilder.group({
        minimo: [massimo],
        massimo: [minimo],
        valore: [null],
        proporzionale: [false],
        id: [null],
      })
    );
  }

  checkChange() {
    let sub: Subscription =
      this.creaForm.controls.idIndicatore.valueChanges.subscribe((val) => {
        this.f.specificaPercentuale.setValue(null);
        this.f.specificaDecimali.setValue(null);
        this.f.specificaDecimaliA.setValue(null);
        this.f.specificaDecimaliB.setValue(null);
        this.f.specificaNumeratore.setValue(null);
        this.f.specificaDenominatore.setValue(null);
        this.updateIndicatore(val);
      });
    this.subs.push(sub);
  }

  updateIndicatore(val: number) {
    //console.log("-----updateIndicatore", val);
    const tipoIndicatore: any = this.indicatoriFormula.find(
      (val2: any) => val2.id === val
    );
    if (!tipoIndicatore || tipoIndicatore.tipoFormula == this.tipoFormula) {
      return;
    }

    this.tipoFormula = tipoIndicatore.tipoFormula;

    if (this.tipoFormula === "TIPO_FORMULA_DATA") {
      this.f.crescente.setValue("DECRESCENTE");
    } else {
      this.f.crescente.setValue("CRESCENTE");
    }
    if (this.tipoFormula === "TIPO_FORMULA_NUMERO") {
      this.f.specificaDecimali.setValue(this.tipoFormula?.decimali);
      this.f.specificaPercentuale.setValue(this.tipoFormula?.percentuale);
    }
    if (this.tipoFormula === "TIPO_FORMULA_RAPPORTO") {
      this.f.specificaPercentuale.setValue(this.tipoFormula?.percentuale);
      this.f.specificaDecimali.setValue(this.tipoFormula?.decimali);
      this.f.specificaDecimaliA.setValue(this.tipoFormula?.decimaliA);
      this.f.specificaDecimaliB.setValue(this.tipoFormula?.decimaliB);
    }
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmitRange() {
    //console.log("----onSubmitRange");
    if (this.readOnly) {
      return;
    }
    let value = this.rangeForm.value;
    this.loading = true;
    let sub: Subscription = this.programmazioneService
      .updateIndicatoreRange(this.idNodoPiano, this.id, value)
      .subscribe(
        (result) => {
          //this.activeModal.close("refresh");
          this.loading = false;
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio dell'indicatore: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  onSubmitTarget() {
    //console.log("----onSubmitTarget");
    if (this.readOnly) {
      return;
    }
    let value = this.targetForm.value;
    var tmp = new Array<any>();
    var k = 0;
    var aa = 0;
    var f: any;
    if (this.target) {
      let f = this.targetForm.get("target")?.value;
      this.targetAnno.valoreTemporale = null;
      this.targetAnno.valoreBooleano = null;
      if (f) {
        if (this.tipoFormula === "TIPO_FORMULA_DATA") {
          this.targetAnno.valoreTemporale = f.valoreTemporale;
        }
        this.targetAnno.valoreNumerico =
          f.valoreNumerico && this.targetAnno.percentuale
            ? f.valoreNumerico / 100
            : f.valoreNumerico;
        this.targetAnno.valoreNumericoA = f.valoreNumericoA;
        this.targetAnno.valoreNumericoB = f.valoreNumericoB;
        if (this.tipoFormula === "TIPO_FORMULA_BULEANO") {
          this.targetAnno.valoreBooleano = f.valoreBooleano;
        }
        tmp.push(this.targetAnno);
      }
      let fs = this.targetForm.get("storico")?.value;
      if (fs) {
        this.storicoAnno.valoreTemporale = null;
        this.storicoAnno.valoreBooleano = null;
        if (this.tipoFormula === "TIPO_FORMULA_DATA") {
          this.storicoAnno.valoreTemporale = fs.valoreTemporale;
        }
        this.storicoAnno.valoreNumerico =
          fs.valoreNumerico && this.storicoAnno.percentuale
            ? fs.valoreNumerico / 100
            : fs.valoreNumerico;
        this.storicoAnno.valoreNumericoA = fs.valoreNumericoA;
        this.storicoAnno.valoreNumericoB = fs.valoreNumericoB;
        if (this.tipoFormula === "TIPO_FORMULA_BULEANO") {
          this.storicoAnno.valoreBooleano = fs.valoreBooleano;
        }
      }
    }

    this.loading = true;

    let sub: Subscription = this.programmazioneService
      .updateIndicatoreTarget(this.idNodoPiano, this.id, {
        idIndicatorePiano: this.id,
        ...{ storico: this.storicoAnno, targets: tmp },
      })
      .subscribe(
        (result) => {
          //this.activeModal.close("refresh");
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("ok"),
            text: "Target Indicatore aggiornato",
            icon: "success",
            confirmButtonText: "Ok",
          });
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio dell'indicatore: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  onSubmit() {
    //console.log("onSummit indicatore");
    if (this.readOnly) {
      return;
    }
    let value = this.creaForm.value;
    value = { ...value, decrescente: value.crescente !== "CRESCENTE" };
    if (!value.specificaDenominatore) {
      delete value.specificaDenominatore;
    }
    if (!value.specificaNumeratore) {
      delete value.specificaNumeratore;
    }
    delete value.crescente;
    if (this.id !== undefined) {
      this.loading = true;
      let sub: Subscription = this.programmazioneService
        .updateIndicatorePiano(this.idNodoPiano, this.id, value)
        .subscribe(
          (result) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("ok"),
              text: "Indicatore aggiornato",
              icon: "success",
              confirmButtonText: "Ok",
            });
            //this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'indicatore: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub);
    } else {
      this.loading = true;
      let sub: Subscription = this.programmazioneService
        .setIndicatorePiano(this.idNodoPiano, value)
        .subscribe(
          (result) => {
            //this.activeModal.close("refresh");
            this.loading = false;

            //console.log(result);
            this.id = result;
            this.ricaricaIndicatore();

            this.ricaricaTarget();

            this.ricaricaRange();
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'indicatore: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub);
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  currentAnno(offset = 0) {
    if (!this.currentUser?.operator?.anno) {
      return "";
    }
    return this.currentUser?.operator?.anno + offset;
  }
  annoInizioTab(offset = 0) {
    if (!this.annoInizio) {
      return "";
    }
    return this.annoInizio + offset;
  }
}
