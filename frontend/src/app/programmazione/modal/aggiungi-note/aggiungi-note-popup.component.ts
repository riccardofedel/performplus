import { Component, OnInit, OnDestroy, Input } from '@angular/core';


import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {  NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService, ConsuntivazioneService, ProgrammazioneService } from 'src/app/_services';
import { FieldsService } from 'src/app/_services/fields.service';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';
import { User } from 'src/app/_models';


@Component({
  templateUrl: "aggiungi-note-popup.component.html",
  styleUrls: ["aggiungi-note-popup.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class AggiungiNotePopupComponent implements OnInit, OnDestroy {
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

  periodo= 0;

  //parametri: per modificare
  @Input() idNodoPiano: number | undefined;
  @Input() azione: string | undefined;

  ngOnInit() {
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          let sub2: Subscription = this.consuntivazioneService
            .getConsuntivazione(this.idNodoPiano)
            .subscribe(
              (result) => {
                this.loading = false;
                this.risultato = result;
                this.periodo=result.periodo;
                this.creaForm = this.formBuilder.group({
                  note: [
                    this.periodo <= 2
                      ? result.statoAvanzamentoS1
                      : result.statoAvanzamentoS2,
                    Validators.required,
                  ],
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
    let sub: Subscription = this.consuntivazioneService
      .setStatoAvanzamento(this.idNodoPiano, this.periodo, this.f.note.value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio delle note di consuntivazione: " + error,
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


