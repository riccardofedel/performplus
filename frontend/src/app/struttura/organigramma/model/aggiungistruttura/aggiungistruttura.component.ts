import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { User } from "../../../../_models";
import {
  UserService,
  AuthenticationService,
  RisorseService,
} from "../../../../_services";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbModal, NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
import { StruttureService } from "src/app/_services/strutture.service";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: "aggiungistruttura.component.html",
  styleUrls: ["aggiungistruttura.component.scss"],
})
export class AggiungistrutturaComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private risorseService: RisorseService,
    private struttureService: StruttureService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  isLoadingResult = false;

  //parametri: per modificare
  @Input() id: number | undefined;

  //per aggiungere
  @Input() idPadre: number | undefined;

  result = null;

  responsabili: any[] = [];

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  codiceCompleto: string | undefined;

  loading = false;

  keyword = "description";

  tipi: any[] = [];

  tipologie: any[] = [];

  livello: any | undefined;

  ngOnInit() {
    this.loading = true;
    this.codiceCompleto = "";
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.aggiornaFiltroResponsabili("");
          this.getTipi();
          this.getTipologie();
          if (this.id !== undefined) {
            let sub2: Subscription = this.struttureService
              .getStruttura(this.id)
              .subscribe((result) => {
                this.loading = false;
                this.codiceCompleto = result.codiceCompleto;
                this.livello = result.livello?.codice;
                const description = result.responsabile
                  ? result.responsabile.cognome + " " + result.responsabile.nome
                  : null;
                this.creaForm = this.formBuilder.group({
                  codice: [result.codice, Validators.required],
                  intestazione: [result.intestazione, Validators.required],
                  idResponsabile: [
                    { id: result.responsabile?.id, description },
                    Validators.required,
                  ],
                  descrizione: [result.descrizione],
                  codiceInterno: [result.codiceInterno],
                  idPadre: result.idPadre,
                  inizioValidita: result.inizioValidita,
                  fineValidita: result.fineValidita,
                  tipoStruttura: [
                    result.tipoStruttura
                  ],
                  tipologiaStruttura: [
                    result.tipologiaStruttura
                  ],
                });
              });
            this.subs.push(sub2);
          } else {
            let sub3: Subscription = this.struttureService
              .preparaStruttura(this.idPadre)
              .subscribe((result) => {
                this.loading = false;
                this.codiceCompleto = result.codiceCompleto;
                this.livello = result.livello;
                if (result.idResponsabile) {
                  let sub4: Subscription = this.risorseService
                    .getRisorsa(result.idResponsabile)
                    .subscribe((risorsa) => {
                      this.creaForm = this.formBuilder.group({
                        codice: [result.codice, Validators.required],
                        intestazione: [null, Validators.required],
                        idResponsabile: [
                          {
                            id: result.idResponsabile,
                            description: risorsa
                              ? risorsa.cognome + " " + risorsa.nome
                              : null,
                          },
                          Validators.required,
                        ],
                        descrizione: [null],
                        codiceInterno: [null],
                        idPadre: this.idPadre,
                        inizioValidita: [null],
                        fineValidita: [null],
                        tipoStruttura: [
                          null
                        ],
                        tipologiaStruttura: [
                          null
                        ],
                      });
                    });
                  this.subs.push(sub4);
                } else {
                  this.creaForm = this.formBuilder.group({
                    codice: [result.codice, Validators.required],
                    intestazione: [null, Validators.required],
                    idResponsabile: [null, Validators.required],
                    descrizione: [null],
                    codiceInterno: [null],
                    idPadre: this.idPadre,
                    inizioValidita: [null],
                    fineValidita: [null],
                    tipoStruttura: [
                      null
                    ],
                    tipologiaStruttura: [
                      null
                    ],
                  });
                }
              });
            this.subs.push(sub3);
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

  getTipi() {
    this.subs.push(
      this.struttureService.tipi().subscribe((result) => {
        this.tipi = result;
      })
    );
  }
  getTipologie() {
    this.subs.push(
      this.struttureService.tipologie().subscribe((result) => {
        this.tipologie = result;
      })
    );
  }
  onSubmit() {
    let value = this.creaForm.value;
    if (this.creaForm?.value?.idResponsabile?.id) {
      value.idResponsabile = this.creaForm?.value?.idResponsabile?.id;
    }
    if (this.id !== undefined) {
      delete value.idPadre;
      let sub1: Subscription = this.struttureService
        .updateStruttura(this.id, value)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della struttura: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.struttureService
        .setStruttura(value)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della struttura: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub2);
    }
  }

  aggiornaFiltroResponsabili(testo: string | undefined) {
    this.isLoadingResult = true;
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        0,
        20,
        true,
        true,
        testo,
        undefined
      )
      .subscribe((result) => {
        let responsabili = [];
        for (let item of result.content) {
          responsabili.push({
            id: item.id,
            description:
              item.cognome + " " + item.nome + " [" + item.codiceInterno + "]",
          });
        }
        this.responsabili = responsabili;
        this.isLoadingResult = false;
      });
    this.subs.push(sub);
  }

  onChangeSearch($event: any) {
    this.aggiornaFiltroResponsabili($event);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}
