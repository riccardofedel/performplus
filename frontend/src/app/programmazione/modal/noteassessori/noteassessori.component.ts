import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { User } from "src/app/_models";
import {
  AuthenticationService,
  TipoNodo,
} from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { ProgrammazioneService } from 'src/app/_services';


@Component({
  templateUrl: "noteassessori.component.html",
  styleUrls: ["noteassessori.component.scss"],
})
export class NoteassessoriComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  //parametri: per modificare
  @Input() id: number | undefined;

  //per aggiungere
  @Input() idPadre: number | undefined;

  //a che livello siamo?
  @Input() livello: number | undefined;

  //tipo nodo in fase di modifica (in fase di creazione arriva dal servizio)
  @Input() tipoNodo: string | undefined;

  //dato in ingresso
  @Input() noteAssessori: string | undefined;

  TipoNodo = TipoNodo;

  result = null;

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        this.creaForm = this.formBuilder.group({
          noteAssessori: [this.noteAssessori],
        });
      }
    );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    let value = this.creaForm?.controls?.noteAssessori?.value;
    let sub: Subscription = this.programmazioneService
      .updateNoteAssessori(this.id, value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio delle note: " + error,
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


