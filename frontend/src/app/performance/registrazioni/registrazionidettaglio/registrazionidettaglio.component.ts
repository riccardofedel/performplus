import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Permission, PermissionService, RegistrazioniService, ProgrammazioneService, RisorseService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ValidatorFn, AbstractControl } from "@angular/forms";
function autocompleteObjectValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (typeof control.value === "string") {
      return { invalidAutocompleteObject: { value: control.value } };
    }
    return null; /* valid option selected */
  };
}

@Component({
  selector: "app-registrazionidettaglio",
  templateUrl: "./registrazionidettaglio.component.html",
  styleUrls: ["./registrazionidettaglio.component.scss"],
  providers: [TranslatePipe],
})
export class RegistrazionidettaglioComponent implements OnInit, OnDestroy {
  creaForm!: FormGroup;
  loading: boolean = false;

  currentUser: User | undefined;
  subs: Subscription[] = [];
  registrazioneId: number | undefined;
  valutatori: any = ([] = []);
  valutati: any = ([] = []);
  regolamenti: any = [];
  questionari: any = [];
  organizzazioni: any = [];
  Permission = Permission;

  risorse: any = [];
  strutture: any = [];

  isLoadingValutatori = false;
  isLoadingValutati = false;

  keyword = "description";

  anno: number | undefined;
  idEnte = 0;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private registrazioniService: RegistrazioniService,
    private risorseService: RisorseService,
    private programmazioneService: ProgrammazioneService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get("registrazioneId");
    //console.log("registrazioneId", id);
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.idEnte = this.currentUser.operator!.idEnte;
        this.anno = this.currentUser.operator!.anno;
        if (this.currentUser) {
          this.liste();
        }
      }
    );
    this.subs.push(sub1);
    if (id) {
      //modifica
      this.registrazioneId = Number(id);

      let sub2: Subscription = this.registrazioniService
        .getRegistrazione(this.registrazioneId)
        .subscribe((result) => {
          const description1 = result.valutatore
            ? result.valutatore.cognome + " " + result.valutatore.nome
            + " ["+result.valutatore.codiceInterno+"]"
            : null;
          const description2 = result.valutato
            ? result.valutato.cognome +
              " " +
              result.valutato.nome +
              " [" +
              result.valutato.codiceInterno +
              "]"
            : null;
          this.creaForm = this.formBuilder.group({
            idValutatore: [
              { id: result.valutatore?.id, description: description1 },
              [Validators.required, autocompleteObjectValidator()],
            ],
            idValutato: [
              { id: result.valutato?.id, description: description2 },
              [Validators.required, autocompleteObjectValidator()],
            ],
            idOrganizzazione: [result.organizzazione?.id, Validators.required],
            idRegolamento: [result.regolamento?.id, Validators.required],
            idQuestionario: [result.questionario?.id, Validators.required],
            inizioValidita: [result.inizioValidita, Validators.required],
            fineValidita: [result.fineValidita, Validators.required],
            responsabile: [result.responsabile],
            po: [result.po],
            interim: [result.interim],

            forzaSchedaSeparata: [result.forzaSchedaSeparata],
            inattiva: [result.inattiva],
            forzaValutatore: [result.forzaValutatore],
            mancataAssegnazione: [result.mancataAssegnazione],
            mancatoColloquio: [result.mancatoColloquio]
          });
          //this.checkFormChange();
        });
        this.subs.push(sub2);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        idOrganizzazione: [null, Validators.required],
        idRegolamento: [null, Validators.required],
        idQuestionario: [null, Validators.required],
        inizioValidita: [null, Validators.required],
        fineValidita: [null, Validators.required],
        responsabile: [false],
        po: [false],
        interim: [false],
        idValutatore: [
          null,
          [Validators.required, autocompleteObjectValidator()],
        ],
        idValutato: [
          null,
          [Validators.required, autocompleteObjectValidator()],
        ],
        forzaSchedaSeparata: [false],
        inattiva: [false],
        forzaValutatore: [false],
        mancataAssegnazione: [false],
        mancatoColloquio: [false],
      });
      //this.checkFormChange();
    }
  }

  liste() {
    let sub1: Subscription = this.registrazioniService
      .getRegolamentiList(
        this.currentUser!.operator?.idEnte,
        this.currentUser!.operator?.anno
      )
      .subscribe((result) => {
        this.regolamenti = result;
      });
      this.subs.push(sub1);
    let sub2: Subscription = this.registrazioniService
      .getQuestionariList(this.currentUser!.operator?.idEnte)
      .subscribe((result) => {
        this.questionari = result;
      });
      this.subs.push(sub2);
    let sub3: Subscription = this.registrazioniService
      .getOrganizzazioniList(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno
      )
      .subscribe((result) => {
        this.organizzazioni = result;
      });
      this.subs.push(sub3);
  }


  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    if (!this.currentUser) {
      return;
    }
    if (this.creaForm?.invalid) {
      this.loading = false;
      return;
    }

    //console.log("----save registrazione:", this.creaForm.value);
    const data = this.creaForm.value;
    data["idValutato"] = this.f.idValutato.value.id;
    data["idValutatore"] = this.f.idValutatore.value.id;
    data["idEnte"] = this.idEnte;
    data["anno"] = this.anno;
    if (this.registrazioneId) {
      let sub1: Subscription = this.registrazioniService
        .updateRegistrazione(
          this.currentUser.operator?.idEnte,
          this.registrazioneId,
          data
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/performance"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della registrazione: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.registrazioniService
        .createRegistrazione(this.currentUser.operator?.idEnte, data)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/performance"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio della registrazione: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }

  aggiornaFiltroValutatori(testo: string | undefined) {
    this.isLoadingValutatori = true;
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        0,
        20,
        true,
        true,
        testo,
        null
      )
      .subscribe((result) => {
        let valutatori = [];
        for (let item of result.content) {
          valutatori.push({
            id: item.id,
            description: item.cognome + " " + item.nome
            + (item.codiceInterno?" ["+item.codiceInterno+"]":""),
          });
        }
        this.valutatori = valutatori;
        this.isLoadingValutatori = false;
      });
      this.subs.push(sub);
  }
  aggiornaFiltroValutati(testo: string | undefined) {
    this.isLoadingValutati = true;
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        0,
        20,
        true,
        true,
        testo,
        null
      )
      .subscribe((result) => {
        let valutati = [];
        for (let item of result.content) {
          valutati.push({
            id: item.id,
            description: item.cognome + " " + item.nome
            + (item.codiceInterno?" ["+item.codiceInterno+"]":""),
          });
        }
        this.valutati = valutati;
        this.isLoadingValutati = false;
      });
      this.subs.push(sub);
  }
  onChangeValutatoriSearch($event: any) {
    this.aggiornaFiltroValutatori($event);
  }
  onChangeValutatiSearch($event: any) {
    this.aggiornaFiltroValutati($event);
  }
}
