import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { NgbDateCustomParserFormatter } from '../../_helpers/dateformat';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { CruscottoService, Permission, PermissionService, ProgrammazioneService, UtentiService } from 'src/app/_services';
import { Introduzione, IntroduzioneElemento, User, Utente } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { FieldsService } from 'src/app/_services/fields.service';

@Component({
  selector: "app-introduzione",
  templateUrl: "./introduzione.component.html",
  styleUrls: ["./introduzione.component.scss"],
  providers: [
    TranslatePipe,
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class IntroduzioneComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private fieldsService: FieldsService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private authenticationService: AuthenticationService,
    public fb: FormBuilder,
    private programmazioneService: ProgrammazioneService
  ) {}

  Permission = Permission;
  loading = false;

  subs: Subscription[] = [];

  currentUser?: User;

  elemento: IntroduzioneElemento | undefined;

  introduzioneForm!: FormGroup | undefined;

  //map: any | undefined;

  field: string | undefined;

  ngOnInit() {
    this.loading = true;

    const field = this.route.snapshot.paramMap.get("field");
    if (field) {
      this.field = field;
    } else {
      this.field = "*@*";
    }
    //console.log("FIELD:", field);
    const a = this.field.split("@");
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          let sub2: Subscription = this.programmazioneService
            .getIntroduzioneContenuto(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno,
              a[0],
              a[1]
            )
            .subscribe((result) => {
              this.elemento = result;

              this.loading = false;
              if (!this.elemento) {
                this.elemento = {
                  id: undefined,
                  gruppo: a[0],
                  nome: a[1],
                  contenuto: "",
                  idPiano: undefined,
                };
              }
              this.introduzioneForm = this.formBuilder.group({
                contenuto: this.elemento?.contenuto,
              });
            });
          this.subs.push(sub2);
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

  // convenience getter for easy access to form fields
  get f() {
    return this.introduzioneForm?.controls;
  }

  onSubmit() {
    if (!this.canWrite()) {
      return;
    }
    if (!this.currentUser || !this.introduzioneForm) {
      return;
    }
    if (this.introduzioneForm?.invalid) {
      this.loading = false;
      return;
    }

    this.loading = true;

    let sub: Subscription = this.programmazioneService
      .updateIntroduzione(this.elemento?.idPiano, {
        elemento: {
          id: this.elemento?.id,
          gruppo: this.elemento?.gruppo,
          nome: this.elemento?.nome,
          contenuto: this.introduzioneForm?.controls["contenuto"].value,
        },
      })
      .subscribe(
        (result) => {
          this.loading = false;
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio dell'introduzione: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
      this.subs.push(sub);
  }

  canWrite() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.DupIntroduzione,
      false
    );
  }

  canRead() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.DupIntroduzione,
      true
    );
  }

  getGruppo() {
    if (this.elemento) {
      return this.elemento.gruppo;
    }
    return "";
  }
  getNome() {
    if (this.elemento) {
      return this.elemento.nome;
    }
    return "";
  }
}

