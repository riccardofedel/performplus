import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { NgbDateCustomParserFormatter } from '../../_helpers/dateformat';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { CruscottoService, Permission, PermissionService, UtentiService } from 'src/app/_services';
import { User, Utente } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { FieldsService } from 'src/app/_services/fields.service';
import { suitSpade } from 'ngx-bootstrap-icons';

@Component({
  selector: "app-cruscotto",
  templateUrl: "./cruscotto.component.html",
  styleUrls: ["./cruscotto.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class CruscottoComponent implements OnInit, OnDestroy {
  constructor(
    private router: Router,
    private fieldsService: FieldsService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public permissionService: PermissionService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private authenticationService: AuthenticationService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private utentiService: UtentiService,
    private cruscottoService: CruscottoService
  ) {}

  Permission = Permission;

  model: any;

  subs: Subscription[] = [];
  currentUser?: User;

  statoAvanzamentoPiano = "";
  statoAvanzamentoConsuntivazione = "";

  cruscottoForm!: FormGroup;

  inizializzazioneAnno: any | undefined;

  loading = false;

  dateFields = [
    "dataInizializzazioneAnno",
    "dataAttivazioneInserimentoInterventiDa",
    "dataAttivazioneInserimentoInterventiA",
    "dataAttivazioneInserimentoIndicatoriDa",
    "dataAttivazioneInserimentoIndicatoriA",
    "dataValidazionePiano",
    "dataChiusuraPiano",
    "dataApprovazioneFinale",
    "dataConsuntivazioneS1Da",
    "dataConsuntivazioneS1A",
    "dataConsuntivazioneS2Da",
    "dataConsuntivazioneS2A",
    "dataConsuntivazione04A",
    "dataCreazioneSchedaDa",
    "dataCreazioneSchedaA",
    "dataConfermaSchedaDa",
    "dataConfermaSchedaA",
    "dataPerformanceOrgDa",
    "dataPerformanceOrgA",
    "dataCompletamentoSchedaDa",
    "dataCompletamentoSchedaA",
    "dataValidazioneSchedaDa",
    "dataValidazioneSchedaA",
    "dataChiusuraPI",
  ];

  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        //console.log("currentUser", x);
        if (this.currentUser) {
          this.loading = true;
          let sub2: Subscription = this.cruscottoService
            .getCruscotto(
              this.currentUser.operator ? this.currentUser.operator?.anno : 0,
              this.currentUser.operator ? this.currentUser.operator?.idEnte : 0
            )
            .subscribe((result) => {
              this.loading = false;
              this.statoAvanzamentoPiano = result.statoAvanzamentoPiano
                ? result.statoAvanzamentoPiano + "%"
                : "";
              this.statoAvanzamentoConsuntivazione =
                result.statoAvanzamentoConsuntivazione == null
                  ? ""
                  : result.statoAvanzamentoConsuntivazione + "%";
              const dataForm: any = {
                flagAttivazioneInserimentoInterventi: [
                  result.flagAttivazioneInserimentoInterventi,
                ],
                flagAttivazioneInserimentoIndicatori: [
                  result.flagAttivazioneInserimentoIndicatori,
                ],
                flagValidazionePiano: [result.flagValidazionePiano],
                flagChiusuraPiano: [result.flagChiusuraPiano],
                flagConsuntivazioneS1: [result.flagConsuntivazioneS1],
                flagFaseVariazioni: [result.flagFaseVariazioni],
                flagConsuntivazioneS2: [result.flagConsuntivazioneS2],
                flagInizializzazioneAnno: [result.flagInizializzazioneAnno],
                flagCreazioneScheda: [result.flagCreazioneScheda],
                flagConfermaScheda: [result.flagConfermaScheda],
                flagPerformanceOrg:[result.flagPerformanceOrg],
                flagCompletamentoScheda: [result.flagCompletamentoScheda],
                flagValidazioneScheda: [result.flagValidazioneScheda],
                flagApprovazioneFinale: [result.flagApprovazioneFinale],
                flagChiusuraPI: [result.flagChiusuraPI],
              };
              for (let entry of this.dateFields) {
                dataForm[entry] = result[entry];
              }
              this.inizializzazioneAnno = result.dataInizializzazioneAnno;
              this.cruscottoForm = this.formBuilder.group(dataForm);
            });
          this.subs.push(sub2);
        }
      }
    );
    this.subs.push(sub1);
  }
  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  // convenience getter for easy access to form fields
  get f() {
    return this.cruscottoForm.controls;
  }

  onSubmit() {
    if (!this.writeAllowed()) {
      return;
    }
    this.loading = true;
    const cruscottoValue = this.cruscottoForm.value;

    for (let entry of this.dateFields) {
      cruscottoValue[entry] = this.f[entry].value;
    }
    //console.log("cruscotto.value", cruscottoValue);
    let sub: Subscription = this.cruscottoService
      .updateCruscotto(
        this.currentUser?.operator ? this.currentUser?.operator?.anno : 0,
        this.currentUser?.operator ? this.currentUser?.operator?.idEnte : 0,
        cruscottoValue
      )
      .subscribe(
        (result) => {
          this.loading = false;
          this.statoAvanzamentoPiano = result.statoAvanzamentoPiano
            ? result.statoAvanzamentoPiano + "%"
            : "";
          this.statoAvanzamentoConsuntivazione =
            result.statoAvanzamentoConsuntivazione
              ? result.statoAvanzamentoConsuntivazione + "%"
              : "";
          Swal.fire({
            title: this.translate.instant("ok"),
            text: this.translate.instant("salvataggio effettuato"),
            icon: "success",
            confirmButtonText: "Ok",
          });
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio del cruscotto: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.Sistema,
      false
    );
  }

  /**
   * confronta le date per vedete se una data è
   * maggiore rispetto all'altra
   */
  dateInverse(data1: any, data2: any) {
    if (!data1) return false;
    if (!data2) return false;
    var d1 = null;
    var d2 = null;
    if (typeof data1 == "string") {
      d1 = this.fieldsService.rectifyFormat(data1);
    }
    /*
    else{
      d1=this.fieldsService.convertObjectTimestampz(data1);
    }
    */
    if (typeof data2 == "string") {
      d2 = this.fieldsService.rectifyFormat(data2);
    }
    /*
    else{
      d2=this.fieldsService.convertObjectTimestampz(data2);
    }
    */
    //console.log("dateInverse:",data1,data2,d1,d2,d1&&d2&&(d1>=d2));
    return d1 && d2 && d1 >= d2;
  }
}

