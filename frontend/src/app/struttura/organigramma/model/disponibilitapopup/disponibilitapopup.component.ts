import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from '../../../../_models';
import { AuthenticationService} from '../../../../_services';
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { Risorsa } from 'src/app/_models';
import { StruttureService } from 'src/app/_services/strutture.service';


@Component({
  templateUrl: "disponibilitapopup.component.html",
  styleUrls: ["disponibilitapopup.component.scss"],
})
export class DisponibilitapopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService,
    private struttureService: StruttureService
  ) {}

  creaForm!: FormGroup;

  subs: Subscription[] = [];
  loading = false;

  //parametri: per modificare
  @Input() risorsa: Risorsa | undefined;

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.creaForm = this.formBuilder.group({
            disponibilita: [
              this.risorsa?.disponibilita,
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
    let value = this.f["disponibilita"].value;
    let sub: Subscription = this.struttureService
      .updateDisponibilita(this.risorsa?.id, value)
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


