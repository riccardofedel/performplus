import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { User } from "src/app/_models";
import {
  UserService,
  AuthenticationService,
  RisorseService,
  TipoNodo,
} from "src/app/_services";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbModal, NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";
import { ProgrammazioneService, PrioritaPiService } from "src/app/_services";

@Component({
  templateUrl: "pesaturapopup.component.html",
  styleUrls: ["pesaturapopup.component.scss"],
})
export class ModificaPrioritaComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    private prioritaService: PrioritaPiService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  descrizioneNodo: string | undefined;
  descrizioneIndicatore: string | undefined;
  peso: number | undefined;

  pesoForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  //parametri: per modificare
  @Input() id: number | undefined;
  @Input() idRegistrazione: number | undefined;
  @Input() idNodo: number | undefined;
  @Input() idIndicatorePiano: number | undefined;

  ngOnInit() {
    //console.log("----ModificaPrioritaComponent");
    this.loading = true;
    this.peso = 0.0;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          if (!this.idIndicatorePiano) {
            if (this.id) {
              let sub2: Subscription = this.prioritaService
                .getPrioritaNodo(this.id, this.idRegistrazione!, this.idNodo!)
                .subscribe((result: any) => {
                  this.peso = result.peso;
                  this.initForm();
                  this.loading = false;
                });
                this.subs.push(sub2);
            } else {
              this.initForm();
              this.loading = false;
            }
          } else {
            if (this.id) {
              let sub3: Subscription = this.prioritaService
                .getPrioritaIndicatore(
                  this.id,
                  this.idRegistrazione!,
                  this.idIndicatorePiano!
                )
                .subscribe((result: any) => {
                  this.peso = result.peso;
                  this.initForm();
                  this.loading = false;
                });
                this.subs.push(sub3);
            } else {
              this.initForm();
              this.loading = false;
            }
          }
        }
      }
    );
    this.subs.push(sub1);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.pesoForm?.controls;
  }

  initForm() {
    this.pesoForm = this.formBuilder.group({
      peso: [
        this.peso,
        [Validators.required, Validators.min(0.0), Validators.max(100.0)],
      ],
    });
  }

  onSubmit() {
    //console.log("--onsummit priorita");
    this.loading = true;
    var data: any = {
      id: this.id,
      idRegistrazione: this.idRegistrazione,
      peso: this.f.peso.value,
    };

    if (this.idIndicatorePiano) {
      data.idIndicatorePiano = this.idIndicatorePiano;
      let sub1: Subscription = this.prioritaService
        .aggiornaPesoIndicatore(data)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      data.idNodo = this.idNodo;
      let sub2: Subscription = this.prioritaService
        .aggiornaPesoNodo(data)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}
