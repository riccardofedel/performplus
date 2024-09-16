import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { IndicatoriService, Permission, PermissionService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';

@Component({
  selector: "app-indicatoridettaglio",
  templateUrl: "./indicatoridettaglio.component.html",
  styleUrls: ["./indicatoridettaglio.component.scss"],
  providers: [TranslatePipe],
})
export class IndicatoridettaglioComponent implements OnInit {
  creaForm!: FormGroup;

  Permission = Permission;

  indicatoreId: number | undefined;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  loading: boolean = false;

  formule: any = [];

  raggruppamenti: any = [];

  calcoliConsuntivazione: any = [];

  constructor(
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    private indicatoriService: IndicatoriService,
    private route: ActivatedRoute,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder
  ) {}

  ngOnInit() {
    const indicatoreId = this.route.snapshot.paramMap.get("indicatoreId");
    if (indicatoreId) {
      //modifica
      this.indicatoreId = +indicatoreId;

      let sub1: Subscription = this.indicatoriService
        .getIndicatore(this.indicatoreId)
        .subscribe((result) => {
          this.creaForm = this.formBuilder.group({
            decimali: [result.decimali],
            decimaliA: [result.decimaliA],
            decimaliB: [result.decimaliB],
            denominazione: [result.denominazione, Validators.required],
            descrizione: [result.descrizione],
            formula: [result.formula],
            nomeValoreA: [result.nomeValoreA],
            calcoloConsuntivazione: [result.calcoloConsuntivazione],
            raggruppamento: [result.raggruppamento],
            nomeValoreB: [result.nomeValoreB],
            percentuale: [result.percentuale],
          });
          this.checkFormChange();
        });
        this.subs.push(sub1);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        decimali: [null],
        decimaliA: [null],
        decimaliB: [null],
        denominazione: [""],
        descrizione: [""],
        calcoloConsuntivazione: [""],
        formula: [""],
        nomeValoreA: [""],
        nomeValoreB: [""],
        percentuale: [false],
        raggruppamento: [""],
      });
      this.checkFormChange();
    }
    let sub2: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.indicatoriService
            .getFormuleList(this.currentUser.operator?.idEnte)
            .subscribe((result) => {
              this.formule = result;
            });
            this.subs.push(sub2);
          let sub3: Subscription = this.indicatoriService
            .getCalcoliConsuntivazione(this.currentUser.operator?.idEnte)
            .subscribe((result) => {
              this.calcoliConsuntivazione = result;
            });
            this.subs.push(sub3);
          let sub4: Subscription = this.indicatoriService
            .getRaggruppamentiList(this.currentUser.operator?.idEnte)
            .subscribe((result) => {
              this.raggruppamenti = result;
            });
            this.subs.push(sub4);
        }
      }
    );
  }

  checkFormChange() {
    this.resetFields();
    let sub1: Subscription =
      this.creaForm.controls.formula.valueChanges.subscribe((val) => {
        if (val === "TIPO_FORMULA_RAPPORTO") {
          this.creaForm.controls["percentuale"].setValue(false, {
            emitEvent: false,
          });
        }
      });
      this.subs.push(sub1);
    let sub2: Subscription = this.creaForm.valueChanges.subscribe((val) => {
      this.resetFields();
    });
    this.subs.push(sub2);
  }

  resetFields() {
    if (
      this.f.formula.value !== "TIPO_FORMULA_NUMERO" &&
      this.f.formula.value !== "TIPO_FORMULA_RAPPORTO"
    ) {
      this.creaForm.controls["decimali"].setValue(null, { emitEvent: false });
      this.creaForm.controls["decimali"].setValue(null, { emitEvent: false });
    }
    if (this.f.formula.value !== "TIPO_FORMULA_RAPPORTO") {
      this.creaForm.controls["decimaliA"].setValue(null, { emitEvent: false });
      this.creaForm.controls["decimaliB"].setValue(null, { emitEvent: false });
      this.creaForm.controls["nomeValoreA"].setValue(null, {
        emitEvent: false,
      });
      this.creaForm.controls["nomeValoreB"].setValue(null, {
        emitEvent: false,
      });
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    if (!this.currentUser) {
      return;
    }
    if (this.creaForm?.invalid) {
      this.loading = false;
      return;
    }

    this.loading = true;

    /*let payload={
      nome:this.f.nome.value,
      risorsa:0,
      idEnte: this.currentUser.operator?.idEnte,
      struttura: 0,
      anno: this.currentUser.operator?.anno,
      userid:this.f.userid.value,
      ruolo:this.f.ruolo
    };*/

    if (this.indicatoreId) {
      let sub1: Subscription = this.indicatoriService
        .updateIndicatore(
          this.currentUser.operator?.idEnte,
          this.indicatoreId,
          this.creaForm.value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/indicatori"]);
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
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.indicatoriService
        .setIndicatore(this.currentUser.operator?.idEnte, this.creaForm.value)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/indicatori"]);
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
        this.subs.push(sub2);
    }
  }
}
