
import { DomSanitizer } from '@angular/platform-browser';
import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { FieldsService } from 'src/app/_services/fields.service';
import { NgbDateCustomParserFormatter } from 'src/app/_helpers/dateformat';
import { User } from 'src/app/_models';
import { AuthenticationService, ConsuntivazioneService } from 'src/app/_services';


@Component({
  templateUrl: "forzaturapopup.component.html",
  styleUrls: ["forzaturapopup.component.scss"],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class ForzaturapopupComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    private sanitizer: DomSanitizer,
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    public fieldsService: FieldsService,
    private consuntivazioneService: ConsuntivazioneService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  fileToUpload: File | null = null;

  creaForm!: FormGroup;

  addForm!: FormGroup;

  richiestaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  risultato: any;

  allegati: any[] | undefined;

  pdfObj: any | undefined;

  //parametri: per modificare
  @Input() idIndicatore: number | undefined;

  @Input() writeAllowed: boolean | undefined;

  cardImageBase64: string | ArrayBuffer | undefined;

  ngOnInit() {
    this.loading = true;
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.addForm = this.formBuilder.group({
            file: [null, Validators.required],
            descrizione: [""],
          });

          this.refreshDataTable();

          let sub2: Subscription = this.consuntivazioneService
            .getForzatura(this.idIndicatore)
            .subscribe(
              (result) => {
                this.loading = false;
                this.risultato = result;
                this.creaForm = this.formBuilder.group({
                  forzatura: [
                    result.forzatura,
                    [Validators.min(0), Validators.max(100)],
                  ],
                  note: [result.note],
                  nonValutabile: [result.nonValutabile],
                });
                this.richiestaForm = this.formBuilder.group({
                  forzatura: [
                    result.richiestaForzatura,
                    [Validators.min(0), Validators.max(100)],
                  ],
                  note: [result.richiestaNote],
                });
              },
              (error) => {
                Swal.fire({
                  title: this.translate.instant("sorry"),
                  text: "Errore: " + error,
                  icon: "error",
                  confirmButtonText: "Ok",
                });
              }
            );
          this.subs.push(sub2);
        }
      }
    );
    this.subs.push(sub1);
  }

  refreshDataTable() {
    let sub: Subscription = this.consuntivazioneService
      .getAllegati(this.idIndicatore)
      .subscribe(
        (result) => {
          this.allegati = result;
        },
        (error) => {
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  get f2() {
    return this.richiestaForm?.controls;
  }

  onSubmit() {
    this.loading = true;
    let value = this.creaForm.value;
    let sub: Subscription = this.consuntivazioneService
      .setForzatura(this.idIndicatore, { id: this.risultato?.id, ...value })
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della forzatura: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  fileChangeEvent(fileInput: any) {
    this.cardImageBase64 = "";
    this.fileToUpload = null;
    if (fileInput.target.files && fileInput.target.files[0]) {
      this.fileToUpload = fileInput.target.files[0];
      const reader: FileReader = new FileReader();
      if (this.fileToUpload) {
        let that = this;
        reader.readAsDataURL(this.fileToUpload);
        reader.onloadend = function () {
          if (reader.result) {
            that.cardImageBase64 = (<string>reader.result).split(",")[1];
          }
        };
      }
    }
  }

  onSubmitAllegato() {
    if (!this.fileToUpload || !this.cardImageBase64) {
      return;
    }
    this.loading = true;

    let value = {
      descrizione: this.addForm.value.descrizione,
      nome: this.fileToUpload.name,
      fileName: this.fileToUpload.name,
      contentType: this.fileToUpload.type,
      base64: this.cardImageBase64,
    };

    let sub: Subscription = this.consuntivazioneService
      .salvaAllegato(this.idIndicatore, value)
      .subscribe(
        (result) => {
          this.loading = false;
          this.setPage("allegati");
          this.refreshDataTable();
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio dell'allegato: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }

  onSubmitRichiesta() {
    this.loading = true;
    let value = this.richiestaForm.value;
    let sub: Subscription = this.consuntivazioneService
      .setRichiestaForzatura(this.idIndicatore, value)
      .subscribe(
        (result) => {
          this.activeModal.close("refresh");
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nel salvataggio della richiesta: " + error,
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

  page: string = "";

  setPage(page: string) {
    this.page = page;
  }

  viewAllegato(idAllegato: number) {
    let sub: Subscription = this.consuntivazioneService
      .getAllegato(idAllegato)
      .subscribe(
        (result) => {
          this.pdfObj = this.sanitizer.bypassSecurityTrustResourceUrl(
            "data:" + result.contentType + ";base64," + result.base64
          );
        },
        (error) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: "Errore nella visualizzazione dell'allegato: " + error,
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
      this.subs.push(sub);
  }

  deleteAllegato(idAllegato: number) {
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Rimuovi l'allegato #" + idAllegato,
    }).then((result) => {
      this.loading = true;
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.consuntivazioneService
          .rimuoviAllegato(idAllegato)
          .subscribe(
            (result) => {
              this.loading = false;
              this.refreshDataTable();
            },
            (error) => {
              this.loading = false;
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella rimozione dell'allegato: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
          this.subs.push(sub);
      }
    });
  }

  aggiungiAllegato() {
    this.setPage("addallegato");
  }
}
