import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from "src/app/_models";
import {
  AuthenticationService,
  ConsuntivazioneService,
} from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { ProgrammazioneService } from 'src/app/_services';
import { FieldsService } from 'src/app/_services/fields.service';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';


@Component({
  templateUrl: "risultatopopup.component.html",
  styleUrls: ["risultatopopup.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class RisultatopopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private consuntivazioneService: ConsuntivazioneService,
    public fieldsService: FieldsService,
    private programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  risultato: any;

  //parametri: per modificare
  @Input() idIndicatore: number | undefined;

  @Input() consuntivazione: any | undefined;

  ngOnInit() {
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          let sub2: Subscription = this.consuntivazioneService
            .getValutazione(this.idIndicatore, this.consuntivazione?.id)
            .subscribe(
              (result) => {
                this.loading = false;
                this.risultato = result;
                this.creaForm = this.formBuilder.group({
                  valoreBooleano: [result.valoreBooleano],
                  valoreNumerico: [result.valoreNumerico],
                  valoreNumericoA: [result.valoreNumericoA],
                  valoreNumericoB: [result.valoreNumericoB],
                  valoreTemporale: [result.valoreTemporale],
                  statoAvanzamento: [result.statoAvanzamento],
                });
              },
              (error) => {
                Swal.fire({
                  title: this.translate.instant("sorry"),
                  text: "Errore: " + error,
                  icon: "error",
                  confirmButtonText: "Ok",
                });
              }
            );
          this.subs.push(sub2);
        }
      }
    );
    this.subs.push(sub1);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    this.loading = true;

    let value = this.creaForm.value;

    value.dataRilevazione = this.fieldsService.convertObjectTimestampz(
      this.creaForm.value.dataRilevazione
    );
    value.valoreTemporale = this.fieldsService.convertObjectTimestampz(
      this.creaForm.value.valoreTemporale
    );
    value.periodo = this.risultato.period;

    let sub: Subscription = this.consuntivazioneService
      .setConsuntiva(this.idIndicatore, value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della consuntiva: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}


