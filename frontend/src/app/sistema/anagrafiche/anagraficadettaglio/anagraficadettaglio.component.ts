import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { AnagraficheService, Permission, PermissionService, UtentiService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';
import { NgbDateParserFormatter, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';
import { FieldsService } from 'src/app/_services/fields.service';

@Component({
  selector: "app-anagraficadettaglio",
  templateUrl: "./anagraficadettaglio.component.html",
  styleUrls: ["./anagraficadettaglio.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class AnagraficadettaglioComponent implements OnInit, OnDestroy {
  creaForm!: FormGroup;

  id: number | undefined;

  page: string | undefined;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  loading: boolean = false;

  ruoli: any = [];

  Permission = Permission;

  risorse: any = [];

  strutture: any = [];

  idPadre: number | undefined;
  padre: any | undefined;

  constructor(
    private modalService: NgbModal,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    private anagraficheService: AnagraficheService,
    private route: ActivatedRoute,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private fieldsService: FieldsService
  ) {}

  ngOnInit() {
    const page = this.route.snapshot.paramMap.get("page");
    if (page) {
      this.page = page;
    } else {
      this.page = "tag";
    }
    const id = this.route.snapshot.paramMap.get("id");
    const idPadre = this.route.snapshot.paramMap.get("idPadre");
    if (idPadre) {
      this.idPadre = +idPadre;
      let sub1: Subscription = this.anagraficheService
        .get(this.page, this.idPadre)
        .subscribe((result) => {
          this.padre = result;
        });
        this.subs.push(sub1);
    } else {
      this.idPadre = undefined;
    }
    if (id) {
      //modifica
      this.id = +id;
      let sub2: Subscription = this.anagraficheService
        .get(this.page, this.id)
        .subscribe((result) => {
          if (result.idPadre) {
            this.idPadre = result.idPadre;
            let sub3: Subscription = this.anagraficheService
              .get(this.page, result.idPadre)
              .subscribe((result) => {
                this.padre = result;
              });
              this.subs.push(sub3);
          }
          this.creaForm = this.formBuilder.group({
            codice: [result.codice, Validators.required],
            descrizione: [result.descrizione, Validators.required],
            inizioValidita: [
              this.fieldsService.convertTimestampzToObject(
                result.inizioValidita
              ),
              Validators.required,
            ],
            fineValidita: [
              this.fieldsService.convertTimestampzToObject(result.fineValidita),
              Validators.required,
            ],
          });
        });
        this.subs.push(sub2);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        codice: ["", Validators.required],
        descrizione: ["", Validators.required],
        inizioValidita: [
          this.fieldsService.convertCurrentaDate(),
          Validators.required,
        ],
        fineValidita: [
          this.fieldsService.convertTimestampzToObject("9999-12-31"),
          Validators.required,
        ],
      });
    }
    let sub4: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub4)
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

    let value = this.creaForm.value;

    value.inizioValidita = this.fieldsService.convertObjectTimestampz(
      this.creaForm?.value?.inizioValidita
    );
    value.fineValidita = this.fieldsService.convertObjectTimestampz(
      this.creaForm?.value?.fineValidita
    );

    if (this.idPadre) {
      value["idPadre"] = this.idPadre;
    }
    this.loading = true;
    if (this.id) {
      let sub1: Subscription = this.anagraficheService
        .update(this.page, this.id, value)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/anagrafiche/" + this.page]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'anagrafica: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.anagraficheService
        .create(this.page, value)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/anagrafiche/" + this.page]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'anagrafica: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }
}
