import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from '../../../_models';
import { UserService, AuthenticationService, RisorseService, TipoNodo} from '../../../_services';
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule, NgOption } from '@ng-select/ng-select';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { ProgrammazioneService, UtentiService } from 'src/app/_services';


@Component({
  templateUrl: "utentipassword.component.html",
  styleUrls: ["utentipassword.component.scss"],
})
export class UtentipasswordComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  fieldTextType = false;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,
    private utentiService: UtentiService,
    private authenticationService: AuthenticationService
  ) {}

  //parametri: per modificare
  @Input() userId: number | undefined;

  subs: Subscription[] = [];
  loading = false;

  passwordForm!: FormGroup;

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        this.passwordForm = this.formBuilder.group({
          password: [null, [Validators.required, Validators.minLength(5)]],
          passwordRepeat: [
            null,
            [Validators.required, Validators.minLength(5)],
          ],
        });
      }
    );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f2() {
    return this.passwordForm?.controls;
  }

  onSubmitPassword() {
    if (!this.currentUser || !this.userId) {
      return;
    }
    if (this.passwordForm?.invalid) {
      this.loading = false;
      return;
    }
    if (this.f2?.password?.value !== this.f2?.passwordRepeat?.value) {
      return;
    }

    this.loading = true;
    let sub: Subscription = this.utentiService
      .updatePassword(+this.userId, this.passwordForm.value)
      .subscribe(
        (result) => {
          this.loading = false;
          this.activeModal.close();
          Swal.fire({
            title: this.translate.instant("ok"),
            text: "Password aggiornata",
            icon: "success",
            confirmButtonText: "Ok",
          });
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della password: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
}


