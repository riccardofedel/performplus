import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { AnagraficheService, AuthenticationService, ProgrammazioneService, RisorseService, TipoNodo } from 'src/app/_services';
import { FieldsService } from 'src/app/_services/fields.service';
import { User } from 'src/app/_models';
import { PermissionService } from '../../../_services/permission.service';



@Component({
  templateUrl: "sposta-nodo.component.html",
  styleUrls: ["sposta-nodo.component.scss"],
})
export class SpostaNodoComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    public fieldsService: FieldsService,
    private formBuilder: FormBuilder,
    public programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,

    private authenticationService: AuthenticationService,
    private risorseService: RisorseService,
    private permissionService: PermissionService
  ) {}

  //parametri: per modificare
  @Input() id: number | undefined;

  result = null;

  isLoadingResult = false;

  spostaForm!: FormGroup;

  codiceInterno: string | undefined;

  denominazione: string | undefined;

  loading = false;

  organizzazioni = [];

  obiettivi = [];

  subs: Subscription[] = [];
  ngOnInit() {
    this.loading = true;

    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.subs.push(
            this.programmazioneService
              .getProgrammazione(this.id)
              .subscribe((result) => {
                this.loading = false;

                this.codiceInterno = result.codiceInterno;
                this.denominazione = result.denominazione;
                this.spostaForm = this.formBuilder.group({
                  organizzazione: [result.organizzazione?.id, Validators.required],
                  nuovoPadre: [null, Validators.required],
                });
              })
          );

          this.subs.push(
            this.programmazioneService
              .getOrganizzazioni(
                this.currentUser!.operator!.idEnte,
                this.currentUser!.operator!.anno,
                undefined
              )
              .subscribe((result) => {
                this.organizzazioni = result;
              })
          );

          this.subs.push(
            this.programmazioneService
              .getObiettivi(this.id!)
              .subscribe((result: any) => {
                this.obiettivi = result;
              })
          );
        }
      }
    );
    this.subs.push(sub1);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.spostaForm?.controls;
  }

  onSubmit() {
    const nuovoPadre = this.spostaForm?.value?.nuovoPadre;
    const organizzazione = this.spostaForm?.value?.organizzazione;
    const data = {
      id: this.id,
      nuovoPadre: nuovoPadre,
      organizzazione: organizzazione,
    };

    this.subs.push(
      this.programmazioneService.spostaNodo(data).subscribe(
        (result: any) => {
          this.activeModal.close("refresh");
        },
        (error: any) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nello spostamento dell'obiettivo: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      )
    );
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}


