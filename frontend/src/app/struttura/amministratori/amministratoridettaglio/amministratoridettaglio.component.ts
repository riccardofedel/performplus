import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { AmministratoriService, Permission, PermissionService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';
import { FieldsService } from 'src/app/_services/fields.service';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';

@Component({
  selector: "app-amministratoridettaglio",
  templateUrl: "./amministratoridettaglio.component.html",
  styleUrls: ["./amministratoridettaglio.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class AmministratoridettaglioComponent implements OnInit {
  creaForm!: FormGroup;

  amministratoreId: string | undefined;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  loading: boolean = false;

  funzioni: any[] = [];

  formSubscription: Subscription | undefined;

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    private amministratoriService: AmministratoriService,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    private fieldsService: FieldsService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder
  ) {}

  Permission = Permission;

  ngOnInit() {
    const amministratoreId =
      this.route.snapshot.paramMap.get("amministratoreId");

    if (amministratoreId) {
      //modifica
      this.amministratoreId = amministratoreId;

      let sub1: Subscription = this.amministratoriService
        .getAmministratore(+this.amministratoreId)
        .subscribe((result) => {
          this.creaForm = this.formBuilder.group({
            nome: [result.nome, Validators.required],
            cognome: [result.cognome, Validators.required],
            codiceFiscale: [result.codiceFiscale],
            funzione: [result.funzione],
            delega: [result.cognome],
          });
          this.checkFormChange();
        });
        this.subs.push(sub1);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        nome: ["", Validators.required],
        cognome: ["", Validators.required],
        codiceFiscale: [""],
        funzione: ["", Validators.required],
        delega: [""],
      });
      this.checkFormChange();
    }

    let sub2: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.amministratoriService
            .getFunzioniList(this.currentUser?.operator?.idEnte)
            .subscribe((result) => {
              this.funzioni = result;
            });
        }
      }
    );
    this.subs.push(sub2);
  }

  checkFormChange() {
    let sub: Subscription = (this.formSubscription =
      this.creaForm.valueChanges.subscribe((val) => {}));
      this.subs.push(sub);
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

    this.loading = true;

    /*let payload={
      nome:this.f.nome.value,
      risorsa:0,
      idEnte: this.currentUser.operator?.idEnte,
      struttura: 0,
      anno: this.currentUser.operator?.anno,
      userid:this.f.userid.value,
      ruolo:this.f.ruolo
    };*/

    const value = this.creaForm.value;

    if (this.amministratoreId) {
      let sub1: Subscription = this.amministratoriService
        .updateAmministratore(
          this.currentUser.operator?.idEnte,
          this.currentUser.operator?.anno,
          +this.amministratoreId,
          value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/struttura"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("errore salvataggio admin"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.amministratoriService
        .setAmministratore(
          this.currentUser.operator?.idEnte,
          this.currentUser.operator?.anno,
          value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/struttura/amministratori"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("errore salvataggio admin"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }
}
