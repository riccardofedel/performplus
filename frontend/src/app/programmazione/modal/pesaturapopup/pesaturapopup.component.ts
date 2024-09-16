import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from "src/app/_models";
import {
  AuthenticationService,
  TipoNodo,
} from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { ProgrammazioneService } from 'src/app/_services';


@Component({
  templateUrl: "pesaturapopup.component.html",
  styleUrls: ["pesaturapopup.component.scss"],
})
export class PesaturapopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  TipoNodo = TipoNodo;

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  pesatura: any = null;

  tipologie = [];

  livelliStrategicita = [];

  livelliComplessita = [];

  //parametri: per modificare
  @Input() id: number | undefined;

  @Input() tipoNodo: string | undefined;

  ngOnInit() {
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          let sub2: Subscription = this.programmazioneService
            .getTipologiePesatura(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.tipologie = result;
            });
          this.subs.push(sub2);
          let sub3: Subscription = this.programmazioneService
            .getLivelliStrategicita(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.livelliStrategicita = result;
            });
          this.subs.push(sub3);
          let sub4: Subscription = this.programmazioneService
            .getLivelliComplessita(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.livelliComplessita = result;
            });
          this.subs.push(sub4);
          if (this.id !== undefined) {
            let sub5: Subscription = this.programmazioneService
              .getPesatura(this.id)
              .subscribe((result) => {
                this.loading = false;
                this.pesatura = result;
                this.creaForm = this.formBuilder.group({
                  livelloComplessita: [result.livelloComplessita],
                  livelloStrategicita: [result.livelloStrategicita],
                  tipologia: [result.tipologia],
                });
                this.checkChange();
              });
            this.subs.push(sub5);
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

  checkChange() {
    let sub: Subscription = this.creaForm.valueChanges.subscribe((val) => {
      this.ricaricaPesi();
    });
    this.subs.push(sub);
  }

  ricaricaPesi() {
    let value = this.creaForm.value;
    let sub: Subscription = this.programmazioneService
      .calcolaPesatura(this.id, value)
      .subscribe(
        (result) => {
          this.pesatura = result;
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel calcolo della pesatura: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  onSubmit() {
    this.loading = true;
    let value = this.creaForm.value;
    let sub: Subscription = this.programmazioneService
      .setPesatura(this.id, value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della pesatura: " + error,
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


