import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from '../../../../_models';
import { AuthenticationService } from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { Risorsa } from 'src/app/_models';
import { QuestionariService } from 'src/app/_services/questionari.service';


@Component({
  templateUrl: "modifica-peso.component.html",
  styleUrls: ["modifica-peso.component.scss"],
})
export class ModificaPesoQuestionarioComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService,
    private questionariService: QuestionariService
  ) {}

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  //parametri: per modificare
  @Input() nodo: any | undefined;

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.creaForm = this.formBuilder.group({
            peso: [
              this.nodo.peso,
              [Validators.required, Validators.max(100), Validators.min(0)],
            ],
          });
        }
      }
    );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    this.loading = true;
    let value = { peso: this.f.peso.value };
    let sub: Subscription = this.questionariService
      .updatePesoNodo(this.nodo.tipo, this.nodo.id, value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della disponibilità: " + error,
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

}


