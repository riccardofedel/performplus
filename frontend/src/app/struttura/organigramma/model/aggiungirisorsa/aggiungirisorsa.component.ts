import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Risorsa, User } from "src/app/_models";
import {
  UserService,
  AuthenticationService,
  RisorseService,
} from "src/app/_services";
import { ActivatedRoute, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule, NgOption } from '@ng-select/ng-select';
import { FieldsService } from 'src/app/_services/fields.service';
import { StruttureService } from 'src/app/_services/strutture.service';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { PaginationService } from 'src/app/_services/pagination.service';


@Component({
  templateUrl: "aggiungirisorsa.component.html",
  styleUrls: ["aggiungirisorsa.component.scss"],
})
export class AggiungirisorsaComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    public fieldsService: FieldsService,
    private formBuilder: FormBuilder,
    private risorseService: RisorseService,
    private struttureService: StruttureService,
    public activeModal: NgbActiveModal,
    private authenticationService: AuthenticationService
  ) {}

  isLoadingResult = false;

  //parametri: id struttura
  @Input() id: number | undefined;

  result = null;

  responsabili: any[] = [];

  creaForm!: FormGroup;

  subs: Subscription[] = [];

  codiceCompleto: string | undefined;

  loading = false;

  keyword = "description";

  paginationService: PaginationService | undefined;

  sorter = null;

  risorse: Risorsa[] = [];

  cognomi: any[] = [];

  cercaForm!: FormGroup;

  tutteRisorse: Risorsa[] = [];

  selezionatoTutti = false;
  selezionato: number[] = [];
  onChangeSearch($event: any) {
    if (!$event) {
      return;
    }
    const cognome2 = $event.toLowerCase();
    const cognomi = this.tutteRisorse.filter((val) => {
      const elemento = val.cognome?.substr(0, $event?.length);
      if (!elemento) {
        return false;
      }
      const cognome1 = elemento.toLowerCase();
      return cognome1 === cognome2;
    });
    this.cognomi = cognomi.map((val) => {
      return { id: val.id, description: val.nome + " " + val.cognome };
    });
  }

  ngOnInit() {
    this.cercaForm = this.formBuilder.group({
      cerca: [""],
      interno: ["tutti"],
    });

    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.refresh();
        }
      }
    );
    this.subs.push(sub1);
    this.paginationService = new PaginationService();

    let sub2: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refreshTable();
      });
    this.subs.push(sub2);
  }

  refreshTable() {
    const first = this.paginationService?.getFirst()
      ? this.paginationService?.getFirst()
      : 0;
    const last = this.paginationService?.getLast()
      ? this.paginationService?.getLast()
      : 0;
    this.risorse = this.tutteRisorse?.slice(first - 1, last);
  }

  refresh() {
    this.loading = true;
    const pgNum = this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    let filter = this.f?.cerca?.value;
    if (this.f?.cerca?.value?.description) {
      filter = this.f?.cerca?.value?.description;
    }
    const sort = "";
    let sub: Subscription = this.struttureService
      .getRisorseAssociabili(this.id)
      .subscribe((result) => {
        this.loading = false;
        this.tutteRisorse = result;
        this.paginationService?.updateCount(result.length ? result.length : 0);
        this.refreshTable();
      });
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  onSubmit() {
    if (this?.f?.cerca?.value?.id) {
      //se ho scelto proprio il personaggio
      this.risorse = this.tutteRisorse.filter(
        (val) => val.id === this?.f?.cerca?.value?.id
      );
    } else {
      //se invece filtro per stringa
      let $event = this?.f?.cerca?.value;
      if (!$event) {
        $event = "";
      }
      const cognome2 = $event.toLowerCase();
      this.risorse = this.tutteRisorse.filter((val) => {
        const cognome1 = val.cognome?.substr(0, $event?.length).toLowerCase();
        return cognome1 === cognome2;
      });
    }
  }

  
  aggiungiRisorsa(id: number | undefined) {
    let sub: Subscription = this.struttureService
      .associaRisorse({
        idOrganizzazione: this.id,
        selezionati: [id],
      })
      .subscribe(
        (result:any) => {
          this.activeModal.close("refresh");
        },
        (error:any) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("errore associa risorsa"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }
  aggiungiRisorse() {
    let sub: Subscription = this.struttureService
      .associaRisorse({
        id: this.id,
        selezionati: this.selezionato,
      })
      .subscribe(
        (result:any) => {
          this.activeModal.close("refresh");
        },
        (error:any) => {
          this.loading = false;
          Swal.fire({
            title: this.translate.instant("sorry"),
            text: this.translate.instant("errore associa risorse"),
            icon: "error",
            confirmButtonText: "Ok",
          });
        }
      );
    this.subs.push(sub);
  }
  toggleSelezionaTutti() {
    this.selezionatoTutti = !this.selezionatoTutti;
  }
  toggleSeleziona(id: number) {
    const index = this.selezionato.findIndex((r) => id === r);
    if (index < 0) {
      this.selezionato.push(id);
    } else {
      this.selezionato.splice(index, 1);
    }
  }
  isSelezionato(id: number): boolean {
    const index = this.selezionato.findIndex((r) => id === r);
    return index >= 0;
  }
}


