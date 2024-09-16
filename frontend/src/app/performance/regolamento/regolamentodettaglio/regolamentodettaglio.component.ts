import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { Permission, PermissionService, RegolamentoService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
//import { UtentipasswordComponent } from '../utentipassword/utentipassword.component';

@Component({
  selector: "app-regolamentodettaglio",
  templateUrl: "./regolamentodettaglio.component.html",
  styleUrls: ["./regolamentodettaglio.component.scss"],
  providers: [TranslatePipe],
})
export class RegolamentodettaglioComponent implements OnInit, OnDestroy {
  creaForm!: FormGroup;
  loading: boolean = false;
  regolamentoId: number | undefined;

  currentUser: User | undefined;
  subs: Subscription[] = [];

  ruoli: any = [];
  Permission = Permission;

  categorie: any = [];
  incarichi: any = [];
  idEnte = 0;
  anno: number | undefined;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public regolamentoService: RegolamentoService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get("regolamentoId");
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.idEnte = this.currentUser.operator!.idEnte;
        this.anno = this.currentUser.operator!.anno;
      }
    );
      this.subs.push(sub1);
    let sub2: Subscription = this.regolamentoService
      .getCategorieList(this.currentUser!.operator!.idEnte)
      .subscribe((result) => {
        this.categorie = result;
      });
      this.subs.push(sub2);
   let sub3: Subscription = this.regolamentoService
     .getIncarichiList(this.currentUser!.operator!.idEnte)
     .subscribe((result) => {
       this.incarichi = result;
     });
     this.subs.push(sub3);
    if (id) {
      //modifica
      this.regolamentoId = Number(id);

      let sub4: Subscription = this.regolamentoService
        .getRegolamento(this.regolamentoId)
        .subscribe((result) => {
          this.creaForm = this.formBuilder.group({
            id: [result.id],
            pesoObiettiviIndividuali: [
              result.pesoObiettiviIndividuali,
              Validators.required,
            ],
            pesoObiettiviDiStruttura: [
              result.pesoObiettiviDiStruttura,
              Validators.required,
            ],
            pesoObiettiviDiPerformance: [
              result.pesoObiettiviDiPerformance,
              Validators.required,
            ],
            pesoComportamentiOrganizzativi: [
              result.pesoComportamentiOrganizzativi,
              Validators.required,
            ],
            po: [result.po],
            categorie: [result.categorie],
            incarichi: [result.incarichi],
            intestazione: [result.intestazione, Validators.required],
          });
          //this.checkFormChange();
        });
        this.subs.push(sub4);
    } else {
      //crea
      this.creaForm = this.formBuilder.group({
        id: [""],
        pesoObiettiviIndividuali: [0],
        pesoObiettiviDiStruttura: [0],
        pesoObiettiviDiPerformance: [0],
        pesoComportamentiOrganizzativi: [0],
        po: ["false"],
        categorie: [null],
        incarichi: [null],
        intestazione: [""],
      });
      //this.checkFormChange();
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
    //console.log("regolamentoId:", this.regolamentoId);
    if (this.regolamentoId) {
      let sub1: Subscription = this.regolamentoService
        .updateRegolamento(this.regolamentoId, this.creaForm.value)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/performance/regolamento"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'regolamento: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.regolamentoService
        .createRegolamento(this.idEnte, this.anno!, this.creaForm.value)
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/performance/regolamento"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'regolamento: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }
}
