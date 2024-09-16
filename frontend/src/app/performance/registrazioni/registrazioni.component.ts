import {
  Component,
  OnInit,
  NgZone,
  ViewChild,
  OnDestroy,
  ElementRef,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";

import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  NgForm,
} from "@angular/forms";
import {
  AlberoStruttureService,
  Permission,
  PermissionService,
  RegistrazioniService,
} from "src/app/_services";
import { Registrazione, User } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
//import { AggiungiValutatoComponent } from './aggiungiValutato/aggiungiValutato.component';
import { NgSelectModule, NgOption } from "@ng-select/ng-select";
//import { AlberoComponent } from './albero/albero.component';
import { environment } from "../../../environments/environment";
import { FieldsService } from "src/app/_services/fields.service";

@Component({
  selector: "app-registrazioni",
  templateUrl: "./registrazioni.component.html",
  styleUrls: ["./registrazioni.component.scss"],
  providers: [TranslatePipe],
})
export class RegistrazioniComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    public alberoService: AlberoStruttureService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private registrazioniService: RegistrazioniService,
    public fieldsService: FieldsService
  ) {}

  organizzazioni: any[] = [];
  valutatori: any[] = [];
  valutati: any[] = [];
  regolamenti: any = [];
  questionari: any = [];

  isLoadingOrganizzazioni = false;
  isLoadingValutatori = false;
  isLoadingValutati = false;
  loading = false;

  cercaForm!: FormGroup;
  paginationService: PaginationService | undefined;

  currentUser: User | undefined;
  subs: Subscription[] = [];
  length = null;
  registrazioni: Registrazione[] = [];

  Permission = Permission;
  pageNum = 0;
  pageSize = 10;
  sorter = null;

  keyword = "description";

  ngOnInit() {
    //console.log("REGISTRAZIONI ngOnInit");
    const filtro: any = this.registrazioniService.getFiltroRegistrazioni();
    if (filtro) {
      this.cercaForm = this.formBuilder.group({
        idValutatore: [filtro.idValutatore],
        idValutato: [filtro.idValutato],
        idStruttura: [filtro.idStruttura],
        idQuestionario: [filtro.idQuestionario],
        idRegolamento: [filtro.idRegolamento],
      });
    } else {
      this.cercaForm = this.formBuilder.group({
        idValutatore: [null],
        idValutato: [null],
        idStruttura: [null],
        idQuestionario: [null],
        idRegolamento: [null],
      });
    }
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.liste();
          this.refresh();
        }
      }
    );
    this.subs.push(sub1);
    this.paginationService = new PaginationService();
    let sub2: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refresh();
      });
    this.subs.push(sub2);
  }

  refresh() {
    this.loading = true;
    const pgNum = this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    let filter: any = {};
    let controls = this.cercaForm.controls;

    //console.log("---CERCA", this.cercaForm.controls);

    filter.idValutatore = controls.idValutatore?.value?.id;
    filter.idValutato = controls.idValutato?.value?.id;
    filter.idStruttura = controls.idStruttura?.value;
    filter.idQuestionario = controls.idQuestionario?.value;
    filter.idRegolamento = controls.idRegolamento?.value;

    //console.log("---CERCA", filter);

    this.setFiltro();

    const sort = "";
    let sub: Subscription = this.registrazioniService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        pgNum - 1,
        pgSize,
        filter,
        sort
      )
      .subscribe((result) => {
        this.setFiltro();

        this.loading = false;
        this.length = result.totalElements;
        this.registrazioni = result.content;
        this.paginationService?.updateCount(result.totalElements);
      });
    this.subs.push(sub);
  }

  elimina(idReg: number | undefined) {
    if (!idReg) {
      return;
    }
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina la registrazione #" + idReg,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.registrazioniService
          .deleteRegistrazione(idReg)
          .subscribe(
            (result) => {
              this.refresh();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text:
                  "Errore nella cancellazione della registrazione: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm!.controls;
  }

  onSubmit() {
    this.refresh();
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.Sistema,
      false
    );
  }

  onChangeSearchValutatore($event: any) {
    if (!$event) {
      return;
    }
    this.isLoadingValutatori = true;
    let sub: Subscription = this.registrazioniService
      .getValutatori(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingValutatori = false;
        this.valutatori = result.map((val: any) => {
          return {
            id: val.id,
            description: val.descrizione + " [" + val.codice + "]",
          };
        });
      });
    this.subs.push(sub);
  }

  onChangeSearchValutato($event: any) {
    //console.log("valutato:", $event);
    if (!$event) {
      return;
    }
    this.isLoadingValutati = true;
    let sub: Subscription = this.registrazioniService
      .getValutati(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingValutati = false;
        this.valutati = result.map((val: any) => {
          return {
            id: val.id,
            description: val.descrizione + " [" + val.codice + "]",
          };
        });
      });
    this.subs.push(sub);
  }

  liste() {
    let sub1: Subscription = this.registrazioniService
      .getRegolamentiList(
        this.currentUser!.operator?.idEnte,
        this.currentUser!.operator?.anno
      )
      .subscribe((result) => {
        this.regolamenti = result;
      });
    this.subs.push(sub1);
    let sub2: Subscription = this.registrazioniService
      .getQuestionariList(this.currentUser!.operator?.idEnte)
      .subscribe((result) => {
        this.questionari = result;
      });
    this.subs.push(sub2);
    let sub3: Subscription = this.registrazioniService
      .getOrganizzazioniList(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno
      )
      .subscribe((result) => {
        this.organizzazioni = result;
      });
    this.subs.push(sub3);
  }
  vaiARegistrazione(idRegistrazione: number | undefined) {
    if (idRegistrazione === null || idRegistrazione === undefined) return;
    this.router.navigateByUrl("/performance/valutazioni", {
      state: { idRegistrazione: idRegistrazione },
      replaceUrl: true,
    });
  }
  registrazioniValutatore(idValutatore: number | undefined) {
    if (idValutatore === null || idValutatore === undefined) return;
    this.router.navigateByUrl("/performance/valutazioni", {
      state: { idValutatore: idValutatore },
      replaceUrl: true,
    });
  }
  registrazioniValutato(idValutato: number | undefined) {
    if (idValutato === null || idValutato === undefined) return;
    this.router.navigateByUrl("/performance/valutazioni", {
      state: { idValutato: idValutato },
      replaceUrl: true,
    });
  }
  setFiltro() {
    if (
      this.f?.idValutatore?.value ||
      this.f?.idValutato?.value ||
      this.f?.idStruttura?.value ||
      this.f?.idQuestionario?.value ||
      this.f?.idRegolamento?.value
    ) {
      this.registrazioniService.setFiltroRegistrazioni({
        idValutatore: this.f?.idValutatore?.value,
        idValutato: this.f?.idValutato?.value,
        idStruttura: this.f?.idStruttura?.value,
        idQuestionario: this.f?.idQuestionario?.value,
        idRegolamento: this.f?.idRegolamento?.value,
      });
    } else {
      this.registrazioniService.removeFiltroRegistrazioni();
    }
  }
  undo(idReg: number | undefined) {
    if (!idReg) {
      return;
    }
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Ripristinare lo stato precedente della registrazione #" + idReg,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.registrazioniService
          .undo(idReg)
          .subscribe(
            (result) => {
              this.refresh();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text:
                  "Errore nel ripristino della registrazione: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }
}
