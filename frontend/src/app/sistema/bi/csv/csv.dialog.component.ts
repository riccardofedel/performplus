import { Component, OnInit, OnDestroy, Input } from "@angular/core";

import {
  AuthenticationService,
  ConsuntivazioneService,
} from "src/app/_services";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import {
  NgbActiveModal,
  NgbDateParserFormatter,
} from "@ng-bootstrap/ng-bootstrap";
import Swal from "sweetalert2";
import { TranslateService } from "@ngx-translate/core";
import { ProgrammazioneService } from "src/app/_services";
import { FieldsService } from "src/app/_services/fields.service";
import { NgbDateCustomParserFormatter } from "src/app/_helpers/dateformat";
import { User } from "src/app/_models";

@Component({
  templateUrl: "csv.dialog.component.html",
})
export class CsvDialogComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    private formBuilder: FormBuilder,
    public fieldsService: FieldsService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  loading = false;

  risultato: any;

  //parametri: per modificare
  @Input() nomeReport: string | undefined;
  @Input() pivot: Flexmonster.Pivot | undefined;

  ngOnInit() {
    this.creaForm = this.formBuilder.group({
      nomeFile: [this.nomeReport, Validators.required],
      fieldSeparator: [";", Validators.required],
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    
    let value = this.creaForm.value;

    //console.log("---esporta",value);

    if (!this.pivot) {
      Swal.fire({
        title: this.translate.instant("sorry"),
        text: "Nessun report selezionato",
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    }
    let nome: string = value.nomeFile;
    if (!nome) {
      Swal.fire({
        title: this.translate.instant("sorry"),
        text: "Manca nome file csv",
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    }
    nome=nome.replace(' ','_');
    if(nome.toLowerCase().endsWith('.csv')){
        nome=nome.substr(0,nome.length-4);
    }
    if (!value.fieldSeparator) {
      Swal.fire({
        title: this.translate.instant("sorry"),
        text: "Il separatore deve essere un carattere",
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    }
    let sep:string = value.fieldSeparator;
    if (sep === "\\t" || sep==="\t") {
      sep = '\t';
    }

 
    if (!sep || sep === "\n" || sep === "\r") {
      Swal.fire({
        title: this.translate.instant("sorry"),
        text: "Il separatore non è un carattere valido",
        icon: "error",
        confirmButtonText: "Ok",
      });
      return;
    }
    let params = {
      filename: nome,
      fieldSeparator: sep,
    };

    this.pivot!.exportTo("csv", params);

    this.activeModal.close("refresh");
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}
