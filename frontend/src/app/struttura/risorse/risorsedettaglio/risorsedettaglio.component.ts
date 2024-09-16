import { OnInit, NgZone, Component, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  NgForm,
} from "@angular/forms";
import {
  Permission,
  PermissionService,
  RisorseService,
} from "src/app/_services";
import { Simple, User } from "src/app/_models";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/_services/authentication.service";
import Swal from "sweetalert2";
import { first } from "rxjs/operators";
import { FieldsService } from "src/app/_services/fields.service";
import { NgbDateParserFormatter } from "@ng-bootstrap/ng-bootstrap";
import { NgbDateCustomParserFormatter } from "src/app/_helpers/dateformat";
import { Categoria } from "../../../_models/regolamento.categorie";

@Component({
  selector: "app-risorsedettaglio",
  templateUrl: "./risorsedettaglio.component.html",
  styleUrls: ["./risorsedettaglio.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class RisorsedettaglioComponent implements OnInit {
  creaForm!: FormGroup;

  risorsaId: string | undefined;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  loading: boolean = false;

  Permission = Permission;

  categorie = [];
  contratti = [];
  incarichi = [];
  profili = [];

  interno=true;

  constructor(
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    public permissionService: PermissionService,
    private risorseService: RisorseService,
    private route: ActivatedRoute,
    private fieldsService: FieldsService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder
  ) {}

  ngOnInit() {
    const risorsaId = this.route.snapshot.paramMap.get("risorsaId");

    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
      })
    );
    this.subs.push(
      this.risorseService
        .getCategorieList(this.currentUser?.operator?.idEnte)
        .subscribe((result) => {
          this.categorie = result;
        })
    );
    this.subs.push(
      this.risorseService
        .getContrattiList(this.currentUser?.operator?.idEnte)
        .subscribe((result) => {
          this.contratti = result;
        })
    );
    this.subs.push(
      this.risorseService
        .getProfiliList(this.currentUser?.operator?.idEnte)
        .subscribe((result) => {
          this.profili = result;
        })
    );
    this.subs.push(
      this.risorseService
        .getIncarichiList(this.currentUser?.operator?.idEnte)
        .subscribe((result) => {
          this.incarichi = result;
        })
    );
    if (risorsaId) {
      //modifica
      this.risorsaId = risorsaId;

      let sub1: Subscription = this.risorseService
        .getRisorsa(+this.risorsaId)
        .subscribe((result) => {
          this.interno = result.interno;
          this.creaForm = this.formBuilder.group({
            nome: [result.nome, Validators.required],
            cognome: [result.cognome, Validators.required],
            codiceFiscale: [result.codiceFiscale],
            email: [result.email, Validators.email],
            inizioValidita: [result.inizioValidita],
            fineValidita: [result.fineValidita],
            categoria: [result.idCategoria],
            contratto: [result.idContratto],
            profilo: [result.idProfilo],
            incarico: [result.idIncarico],
            interno: [result.interno, Validators.required],
            codiceInterno: [result.codiceInterno],
          });
        });
      this.subs.push(sub1);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        nome: ["", Validators.required],
        cognome: ["", Validators.required],
        codiceFiscale: [""],
        email: ["", Validators.email],
        inizioValidita: [null, Validators.required],
        fineValidita: [null, Validators.required],
        categoria: [null],
        contratto: [null],
        profilo: [null],
        incarico: [null],
        interno: [true, Validators.required],
        codiceInterno: [null],
      });
    }
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

    if (this.risorsaId) {
      let sub1: Subscription = this.risorseService
        .updateRisorsa(
          this.currentUser?.operator?.idEnte,
          this.currentUser?.operator?.anno,
          +this.risorsaId,
          value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/struttura/risorse"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("errore salvataggio risorsa"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.risorseService
        .setRisorsa(
          this.currentUser?.operator?.idEnte,
          this.currentUser?.operator?.anno,
          value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/struttura/risorse"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: this.translate.instant("errore salvataggio risorsa"),
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub2);
    }
  }
  onChangeInterno($event: any) {
    this.interno=!this.interno;
    if(this.interno){
      this.f.contratto.setValue(null);
    }else{
      this.f.profilo.setValue(null);
      this.f.categoria.setValue(null);
      this.f.incarico.setValue(null);
    }
  }
}
