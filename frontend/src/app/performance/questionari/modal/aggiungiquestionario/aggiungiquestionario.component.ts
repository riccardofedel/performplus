import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { User } from "src/app/_models";
import { AuthenticationService, RisorseService } from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { QuestionariService } from "src/app/_services/questionari.service";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";

import { ValidatorFn, AbstractControl } from "@angular/forms";
import { TipoNodoQuestionario } from "src/app/_services/questionari.service";
function autocompleteObjectValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (typeof control.value === "string") {
      return { invalidAutocompleteObject: { value: control.value } };
    }
    return null; /* valid option selected */
  };
}

@Component({
  templateUrl: "aggiungiquestionario.component.html",
  styleUrls: ["aggiungiquestionario.component.scss"],
})
export class AggiungiquestionarioComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;
  idEnte = 0;
  anno: number | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private questionariService: QuestionariService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  isLoadingResult = false;

  //parametri: per modificare
  @Input() id: number | undefined;
  @Input() tipo: TipoNodoQuestionario | undefined;
  //per aggiungere
  @Input() tipoPadre: TipoNodoQuestionario | undefined;
  @Input() idPadre: number | undefined;

  result = null;

  categorie: any[] = [];
  incarichi: any[] = [];

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  tipoNodo: TipoNodoQuestionario | undefined;

  codiceCompleto: string | undefined;

  loading = false;

  keyword = "description";

  ngOnInit() {
    this.loading = true;
    this.codiceCompleto = "";
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.idEnte = this.currentUser.operator!.idEnte;
          this.anno = this.currentUser.operator!.anno;

          let sub2: Subscription = this.questionariService
            .getCategorie(this.currentUser!.operator!.idEnte)
            .subscribe((result) => {
              this.categorie = result;
            });
          this.subs.push(sub2);
          let sub3: Subscription = this.questionariService
            .getIncarichi(this.currentUser!.operator!.idEnte)
            .subscribe((result) => {
              this.incarichi = result;
            });
          this.subs.push(sub3);
          if (this.id !== undefined) {
            let sub4: Subscription = this.questionariService
              .getNodo(this.idEnte, this.tipo!, this.id)
              .subscribe((result) => {
                //console.log("----result:", result);
                this.loading = false;
                this.tipoNodo = result.tipo;
                this.codiceCompleto = result.codiceCompleto;
                const listCategorie = this.list(result.categorie);
                const listIncarichi = this.list(result.incarichi);
                this.creaForm = this.formBuilder.group({
                  codice: [
                    result.codice,
                    this.tipoNodo != "root" && this.tipoNodo != "questionario"
                      ? Validators.required
                      : null,
                  ],
                  categorie: [listCategorie],
                  incarichi: [listIncarichi],
                  intestazione: [result.intestazione, Validators.required],
                  descrizione: [result.descrizione],
                  foglia: [result.foglia],
                  peso: [
                    result.peso,
                    this.tipoNodo != "root" && this.tipoNodo != "questionario"
                      ? Validators.required
                      : null,
                  ],
                  pesoMancataAssegnazione: [result.pesoMancataAssegnazione],
                  pesoMancatoColloquio: [result.pesoMancatoColloquio],
                  flagSoloAdmin: [result.flagSoloAdmin],
                  idPadre: result.idPadre,
                });
              });
            this.subs.push(sub4);
          } else {
            let sub5: Subscription = this.questionariService
              .preparaNodo(this.idEnte, this.tipoPadre!, this.idPadre)
              .subscribe((result) => {
                this.loading = false;
                this.tipoNodo = result.tipo;
                this.codiceCompleto = result.codiceCompleto;
                this.creaForm = this.formBuilder.group({
                  codice: [
                    result.codice,
                    this.tipoNodo != "root" && this.tipoNodo != "questionario"
                      ? Validators.required
                      : null,
                  ],
                  intestazione: [null, Validators.required],
                  peso: [
                    null,
                    this.tipoNodo != "root" && this.tipoNodo != "questionario"
                      ? Validators.required
                      : null,
                  ],
                  categorie: [null],
                  incarichi: [null],
                  descrizione: [null],
                  foglia: [null],
                  pesoMancataAssegnazione: [null],
                  pesoMancatoColloquio: [null],
                  flagSoloAdmin:[null],
                  idPadre: this.idPadre,
                });
              });
            this.subs.push(sub5);
          }
        }
      }
    );
    this.subs.push(sub1);
  }
  list(items: any[]): number[] {
    let out: number[] = [];
    if (!items) return out;
    items.forEach((t) => {
      out.push(t.id);
    });
    return out;
  }
  
  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    let value = this.creaForm.value;

    if (this.tipoNodo == TipoNodoQuestionario.questionario) {
      value.codice = "00";
    }
    if (this.id !== undefined) {
      delete value.idPadre;
      let sub1: Subscription = this.questionariService
        .updateNodo(this.tipoNodo!, this.id, value)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della questionario: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.questionariService
        .createNodo(
          this.idEnte,
          this.anno!,
          this.tipoNodo!,
          this.tipoPadre,
          this.idPadre,
          value
        )
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della questionario: " + error,
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
