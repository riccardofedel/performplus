import { OnInit, NgZone, Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { Permission, PermissionService, UtentiService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import Swal from 'sweetalert2'
import { first } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UtentipasswordComponent } from '../utentipassword/utentipassword.component';

@Component({
  selector: "app-utentidettaglio",
  templateUrl: "./utentidettaglio.component.html",
  styleUrls: ["./utentidettaglio.component.scss"],
  providers: [TranslatePipe],
})
export class UtentidettaglioComponent implements OnInit, OnDestroy {
  creaForm!: FormGroup;

  idUtente: number | undefined;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  loading: boolean = false;

  ruoli: any = [];

  ruoliAgg : any = [];
  Permission = Permission;

  risorse: any = [];

  strutture: any = [];

  constructor(
    private modalService: NgbModal,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private router: Router,
    private utentiService: UtentiService,
    private route: ActivatedRoute,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get("id");
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          let sub2: Subscription = this.utentiService
            .getRoleList()
            .subscribe((result) => {
              this.ruoli = result;
              //console.log('---ruolio:',this.ruoli);
              this.ruoliAgg= this.ruoli.filter((t:any)=>{ return t.codice === "REFERENTE" || t.codice === "RISORSA" || t.codice === "POSIZIONE_ORGANIZZATIVA";})
            });
             this.subs.push(sub2);
          let sub3: Subscription = this.utentiService
            .getRisorseList(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.risorse = result;
            });
             this.subs.push(sub3);
          let sub4: Subscription = this.utentiService
            .getStruttureList(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.strutture = result;
            });
             this.subs.push(sub4);
          if (id) {
            //modifica
            this.idUtente = +id;

            let sub5: Subscription = this.utentiService
              .getUtente(this.idUtente)
              .subscribe((result) => {
                //console.log(", ruolo:", result.ruolo);

                this.creaForm = this.formBuilder.group({
                  //risorsa: [result.risorsa],
                  struttura: [result.struttura],
                  nome: [result.nome, Validators.required],
                  codiceFiscale: [result.codiceFiscale],
                  username: [result.username, [Validators.required]],
                  ruolo: [result.ruolo, Validators.required],
                  ruoloAggiunto: [result.ruoloAggiunto],
                  strutturaAggiunta: [result.strutturaAggiunta],
                });
                this.checkFormChange();
              });
               this.subs.push(sub5);
          } else {
            //crea
            this.creaForm = this.formBuilder.group({
              // risorsa: [''],
              struttura: [""],
              nome: ["", Validators.required],
              codiceFiscale: [""],
              username: ["", [Validators.required]],
              ruolo: ["", Validators.required],
              ruoloAggiunto: [null],
              strutturaAggiunta: [null],
            });
            this.checkFormChange();
          }
        }
      }
    );
    this.subs.push(sub1);
  }

  checkFormChange() {
    if (this.f.ruolo.value === "AMMINISTRATORE") {
      //this.creaForm.controls['risorsa'].setValue(null, {emitEvent:false});
      this.creaForm.controls["struttura"].setValue(null, { emitEvent: false });
    }
    let sub: Subscription = this.creaForm.valueChanges.subscribe((val) => {
      if (val.ruolo === "AMMINISTRATORE") {
        //this.creaForm.controls['risorsa'].setValue(null, {emitEvent:false});
        this.creaForm.controls["struttura"].setValue(null, {
          emitEvent: false,
        });
      }
    });
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
    if (this.idUtente) {
      let sub1: Subscription = this.utentiService
        .updateUtente(
          this.idUtente,
          this.currentUser?.operator?.idEnte,
          this.currentUser?.operator?.anno,
          this.creaForm.value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/utenti"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'utente: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub1);
    } else {
      let sub2: Subscription = this.utentiService
        .setUtente(
          this.currentUser?.operator?.idEnte,
          this.currentUser?.operator?.anno,
          this.creaForm.value
        )
        .subscribe(
          (result) => {
            this.loading = false;
            this.router.navigate(["/sistema/utenti"]);
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'utente: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
        this.subs.push(sub2);
    }
  }

  apriPassword() {
    const modalRef = this.modalService.open(UtentipasswordComponent, {
      size: "lg",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.userId = this.idUtente;

    modalRef.result.then(
      (result) => {},
      (reason) => {}
    );
  }
}
