
import { DomSanitizer } from '@angular/platform-browser';
import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { FieldsService } from 'src/app/_services/fields.service';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';
import { User } from 'src/app/_models';
import { AuthenticationService, ConsuntivazioneService } from 'src/app/_services';


@Component({
  templateUrl: "forzatura-nodo-popup.component.html",
  styleUrls: ["forzatura-nodo-popup.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class ForzaturaNodoPopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    private sanitizer: DomSanitizer,
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    public fieldsService: FieldsService,
    private consuntivazioneService: ConsuntivazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}



  richiestaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  risultato: any;

  //parametri: per modificare
  @Input() idNodoPiano: number | undefined;

  @Input() writeAllowed: boolean | undefined;

  
  ngOnInit() {
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          let sub2: Subscription = this.consuntivazioneService
            .getForzatureNodo(this.idNodoPiano!)
            .subscribe(
              (result:any) => {
                this.loading = false;
                this.risultato = result;
                this.richiestaForm = this.formBuilder.group({
                  forzaturaRisorse: [
                    result.forzaturaRisorse,
                    [Validators.min(0), Validators.max(100)],
                  ],
                  forzaturaResponsabili: [
                    result.forzaturaResponsabili,
                    [Validators.min(0), Validators.max(100)],
                  ],
                });
              },
              (error:any) => {
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

  get f() {
    return this.richiestaForm?.controls;
  }

  onSubmit() {
    this.loading = true;
    let value = this.richiestaForm.value;
    let sub: Subscription = this.consuntivazioneService
      .richiestaForzatureNodo(this.idNodoPiano,this.f.forzaturaRisorse.value, this.f.forzaturaResponsabili.value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio delle forzature: " + error,
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
