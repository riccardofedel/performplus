import { Component, OnInit, NgZone, ViewChild, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { NgbDateCustomParserFormatter } from "../_helpers/dateformat";
import { NgbDateParserFormatter } from "@ng-bootstrap/ng-bootstrap";

import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  NgForm,
} from "@angular/forms";
import {
  CruscottoService,
  Permission,
  PermissionService,
  StampeService,
  UtentiService,
} from "src/app/_services";
import { User, Utente } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { FieldsService } from "src/app/_services/fields.service";
import { saveAs as importedSaveAs } from "file-saver";

@Component({
  selector: "app-stampe",
  templateUrl: "./stampe.component.html",
  styleUrls: ["./stampe.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class StampeComponent implements OnInit, OnDestroy {
  constructor(
    private stampeService: StampeService,
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

  items = [
    {
      denominazione: "Piano integrato di attività e organizzazione",
      id: "PIAO",
    },
    { denominazione: "PIAO Sezione per Area", id: "ARE" },
    { denominazione: "PIAO Sezione per Direzione", id: "DIR" },
    { denominazione: "Rendicontazione", id: "REN" },
    { denominazione: "Elenco responsabilità da riassegnare", id: "RES" },
  ];

  types = [
    { denominazione: "pdf", id: "PDF" },
    { denominazione: "docx", id: "DOCX" },
  ];

  stampeForm!: FormGroup;

  loading = false;

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser?: User;

  direzioni = [];

  aree = [];

  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.stampeForm = this.formBuilder.group({
            tipoReport: ["PIAO"],
            outputType: ["PDF"],
            premessa: [false],
            strutturaOrganizzativa: [false],
            elencoRisultatiAttesi: [false],
            schedeRisultatiAttesi: [false],
            elencoAzioni: [false],
            schedeAzioni: [false],

            area: [null],
            elencoRisultatiAttesiArea: [false],
            schedeRisultatiAttesiArea: [false],
            elencoAzioniArea: [false],
            schedeAzioniArea: [false],

            direzione: [null],
            elencoRisultatiAttesiDirezione: [false],
            schedeRisultatiAttesiDirezione: [false],
            elencoAzioniDirezione: [false],
            schedeAzioniDirezione: [false],

            rendicontazioneRa: [false],
            rendicontazioneAz: [true],
          });

          let sub2: Subscription = this.stampeService
            .getDirezioni(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              //console.log("direzioni:", result);
              this.direzioni = result;
            });
          this.subs.push(sub2);
          let sub3: Subscription = this.stampeService
            .getAree(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              //console.log("aree:", result);
              this.aree = result;
            });
          this.subs.push(sub3);

          let sub4: Subscription = this.stampeForm!.get(
            "tipoReport"
          )!.valueChanges.subscribe((val) => {
            this.checkChange({ id: val });
          });
          if (sub4) this.subs.push(sub4);

          this.checkChange({ id: "PIAO" });
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

  checkChange(e: any) {
    //PIAO
    //console.log("----checkChange----");
    this.stampeForm.controls.premessa.setValue(false, { emitEvent: false });
    this.stampeForm.controls.strutturaOrganizzativa.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.elencoRisultatiAttesi.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesi.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioni.setValue(false, { emitEvent: false });
    this.stampeForm.controls.schedeAzioni.setValue(false, { emitEvent: false });
    //ARE
    this.stampeForm.controls.area.setValue(null);
    this.stampeForm.controls.elencoRisultatiAttesiArea.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesiArea.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioniArea.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.schedeAzioniArea.setValue(false, {
      emitEvent: false,
    });
    //DIR
    this.stampeForm.controls.direzione.setValue(null);
    this.stampeForm.controls.elencoRisultatiAttesiDirezione.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesiDirezione.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioniDirezione.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.schedeAzioniDirezione.setValue(false, {
      emitEvent: false,
    });
    //REN
    this.stampeForm.controls.rendicontazioneRa.setValue(false, {
      emitEvent: false,
    });
    this.stampeForm.controls.rendicontazioneAz.setValue(false, {
      emitEvent: false,
    });

    this.stampeForm.controls.premessa.disable({ emitEvent: false });
    this.stampeForm.controls.strutturaOrganizzativa.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.elencoRisultatiAttesi.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesi.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioni.disable({ emitEvent: false });
    this.stampeForm.controls.schedeAzioni.disable({ emitEvent: false });

    this.stampeForm.controls.area.disable({ emitEvent: false });
    this.stampeForm.controls.elencoRisultatiAttesiArea.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesiArea.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioniArea.disable({ emitEvent: false });
    this.stampeForm.controls.schedeAzioniArea.disable({ emitEvent: false });

    this.stampeForm.controls.direzione.disable({ emitEvent: false });
    this.stampeForm.controls.elencoRisultatiAttesiDirezione.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.schedeRisultatiAttesiDirezione.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.elencoAzioniDirezione.disable({
      emitEvent: false,
    });
    this.stampeForm.controls.schedeAzioniDirezione.disable({
      emitEvent: false,
    });

    this.stampeForm.controls.rendicontazioneRa.disable({ emitEvent: false });
    this.stampeForm.controls.rendicontazioneAz.disable({ emitEvent: false });

    switch (e.id) {
      case "PIAO":
        this.stampeForm.controls.strutturaOrganizzativa.setValue(true);
        // this.stampeForm.controls.elencoRisultatiAttesi.setValue(true);
        // this.stampeForm.controls.elencoAzioni.setValue(true);
        this.stampeForm.controls.premessa.enable();
        this.stampeForm.controls.strutturaOrganizzativa.enable();
        this.stampeForm.controls.elencoRisultatiAttesi.enable();
        this.stampeForm.controls.schedeRisultatiAttesi.enable();
        this.stampeForm.controls.elencoAzioni.enable();
        this.stampeForm.controls.schedeAzioni.enable();
        break;
      case "ARE":
        this.stampeForm.controls.elencoRisultatiAttesiArea.setValue(true);
        this.stampeForm.controls.elencoAzioniArea.setValue(true);
        this.stampeForm.controls.area.enable();
        this.stampeForm.controls.elencoRisultatiAttesiArea.enable();
        this.stampeForm.controls.schedeRisultatiAttesiArea.enable();
        this.stampeForm.controls.elencoAzioniArea.enable();
        this.stampeForm.controls.schedeAzioniArea.enable();
        break;
      case "DIR":
        this.stampeForm.controls.elencoRisultatiAttesiDirezione.setValue(true);
        this.stampeForm.controls.elencoAzioniDirezione.setValue(true);
        this.stampeForm.controls.direzione.enable();
        this.stampeForm.controls.elencoRisultatiAttesiDirezione.enable();
        this.stampeForm.controls.schedeRisultatiAttesiDirezione.enable();
        this.stampeForm.controls.elencoAzioniDirezione.enable();
        this.stampeForm.controls.schedeAzioniDirezione.enable();
        break;
      case "REN":
        this.stampeForm.controls.rendicontazioneAz.setValue(true);
        this.stampeForm.controls.rendicontazioneRa.enable();
        this.stampeForm.controls.rendicontazioneAz.enable();
        break;
    }
  }

  onSubmit() {
    this.loading = true;

    const value = this.stampeForm.value;

    let sub: Subscription = this.stampeService
      .stampa(
        this.currentUser?.operator?.anno,
        this.currentUser?.operator?.idEnte,
        value
      )
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

  get f() {
    return this.stampeForm?.controls;
  }
}
