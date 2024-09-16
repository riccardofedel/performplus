import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { User } from "../../../_models";
import {
  UserService,
  AuthenticationService,
  RisorseService,
  ConsuntivazioneService,
} from "../../../_services";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {
  NgbModal,
  NgbActiveModal,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";
import { FieldsService } from "src/app/_services/fields.service";
import { NgbDateCustomParserFormatter } from "src/app/_helpers/dateformat";
import { ValutazioneService } from "src/app/_services/valutazione.service";

@Component({
  templateUrl: "pubblicazione-popup.component.html",
  styleUrls: ["pubblicazione-popup.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class PubblicapopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private valutazioneService: ValutazioneService,
    public fieldsService: FieldsService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  statoScheda: any;

  azione: string = "Registrazione non valida";

  //parametri: per modificare
  @Input() idRegistrazione: number | undefined;
  @Input() fase: number | undefined;

  ngOnInit() {
    this.loading = true;
    if (this.idRegistrazione && this.fase) {
      if (this.fase == 1) {
        this.azione = "assegnazione obiettivi";
      } else if (this.fase == 2) {
        this.azione = "invio valutazione";
      }
    }
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          if (this.idRegistrazione) {
            let sub2: Subscription = this.valutazioneService
              .statoScheda(this.idRegistrazione!)
              .subscribe(
                (result) => {
                  this.loading = false;
                  this.statoScheda = result;
                  this.creaForm = this.formBuilder.group({
                    note: [null],
                  });
                },
                (error) => {
                  this.loading = false;
                }
              );
            this.subs.push(sub2);
          }
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

    let data = {
      idRegistrazione: this.idRegistrazione,
      note: this.f.note.value,
    };
    if (this.fase == 1) {
      this.subs.push(
        this.valutazioneService.pubblicazioneScheda(data).subscribe(
          (result) => {
            this.success();
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nell'invio: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        )
      );
      return;
    }
    if (this.fase == 2) {
      this.subs.push(
        this.valutazioneService.pubblicazioneValutazione(data).subscribe(
          (result) => {
            this.success();
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nell'invio': " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        )
      );
      return;
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  success() {
    Swal.fire({
      text: "Invio avvenuto con successo",
      icon: "success",
      showConfirmButton: false,
      timer: 1000,
    });
  }
}
