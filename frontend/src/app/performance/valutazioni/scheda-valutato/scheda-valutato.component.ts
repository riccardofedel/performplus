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
  templateUrl: "scheda-valutato.component.html",
  styleUrls: ["scheda-valutato.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class SchedaValutatoComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private valutazioneService: ValutazioneService,
    public fieldsService: FieldsService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  subs: Subscription[] = [];

  loading = false;

  scheda: any;

  //parametri: per modificare
  @Input() idValutato: number | undefined;
 

  ngOnInit() {
    this.loading = true;
    
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          if (this.idValutato) {
            let sub2: Subscription = this.valutazioneService
              .schedaValutato(this.idValutato)
              .subscribe(
                (result) => {
                  this.loading = false;
                  this.scheda = result;
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

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  
}
