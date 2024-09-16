import { Component, OnInit, NgZone, ViewChild, OnDestroy } from "@angular/core";
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
  Permission,
  PermissionService,
  UtentiService,
} from "src/app/_services";
import { User, Utente } from "src/app/_models";
import { PaginationService } from "src/app/_services/pagination.service";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { ExcelService } from "../../_services/excel.service";

@Component({
  selector: "app-utenti",
  templateUrl: "./utenti.component.html",
  styleUrls: ["./utenti.component.scss"],
  providers: [TranslatePipe],
})
export class UtentiComponent implements OnInit, OnDestroy {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public permissionService: PermissionService,
    private utentiService: UtentiService,
    private authenticationService: AuthenticationService,
    private excelService: ExcelService
  ) {}

  sortType = "id";
  sortDirection = "ASC";

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  paginationService: PaginationService | undefined;

  pageNum = 0;
  pageSize = 10;
  sorter = null;

  length = null;

  loading: boolean = false;

  utenti: Utente[] = [];

  direzioni: any[] = [];

  isLoadingDirezioni = false;

  ruoli: any[] = [];

  cercaForm!: FormGroup;

  ngOnInit() {
    const filtro: any = this.utentiService.getFiltroUtenti();
    if (filtro) {
      this.cercaForm = this.formBuilder.group({
        nome: [filtro.nome],
        idDirezione: [filtro.idDirezione],
        ruolo: [filtro.ruolo],
      });
    } else {
      this.cercaForm = this.formBuilder.group({
        nome: [""],
        idDirezione: [null],
        ruolo: [null],
      });
    }
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        //console.log("sistema utenti", x);
        this.currentUser = x;
        if (this.currentUser) {
          let sub2: Subscription = this.utentiService
            .getStruttureList(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno
            )
            .subscribe((result) => {
              this.isLoadingDirezioni = false;
              this.direzioni = result;
            });
          this.subs.push(sub2);
          let sub3: Subscription = this.utentiService
            .getRoleList()
            .subscribe((result) => {
              this.ruoli = result;
            });
          this.subs.push(sub3);
          this.refresh(true);
        }
      }
    );
    this.subs.push(sub1);
    this.paginationService = new PaginationService();

    let sub4: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refresh(false);
      });
    this.subs.push(sub4);
  }

  refresh(first: Boolean) {
    this.loading = true;
    const pgNum = first ? 1 : this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    const nome = this.f?.nome.value;
    const idDirezione = this.f?.idDirezione.value;
    const ruolo = this.f?.ruolo.value;
    const sort = this.sortType + "," + this.sortDirection;

    this.setFiltro();
    let sub: Subscription = this.utentiService
      .search(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        pgNum - 1,
        pgSize,
        idDirezione,
        ruolo,
        nome,
        sort
      )
      .subscribe(
        (result) => {
          this.loading = false;
          this.length = result.totalElements;
          this.utenti = result.content;
          this.paginationService?.updateCount(result.totalElements);
        },
        (error) => {
          this.loading = false;
        }
      );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  elimina(userid: number | undefined) {
    if (!userid) {
      return;
    }
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina l'utente #" + userid,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.utentiService
          .deleteUtente(userid)
          .subscribe(
            (result) => {
              this.refresh(true);
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella cancellazione dell'utente: " + error,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }

  toggleSort(type: string) {
    if (type == this.sortType) {
      if (this.sortDirection == "DESC") {
        this.sortDirection = "ASC";
      } else {
        this.sortDirection = "DESC";
      }
    } else {
      this.sortType = type;
      this.sortDirection = "ASC";
    }
    this.refresh(false);
  }

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.Sistema,
      false
    );
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  onSubmit() {
    this.refresh(true);
  }

  exportAsXLSX(): void {
    var list: any[] = [];
    var rows = [[]];
    const nome = this.f?.nome.value;
    const idDirezione = this.f?.idDirezione.value;
    const ruolo = this.f?.ruolo.value;
    const sort = this.sortType + "," + this.sortDirection;
    //console.log("excel utenti");
    let sub: Subscription = this.utentiService
      .search(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        0,
        10000,
        idDirezione,
        ruolo,
        nome,
        sort
      )
      .subscribe((result) => {
        list = result.content;
        //console.log("excel utenti:", list);
        this.excelService.exportAsExcelFile(list, "Utenti");
      });
    this.subs.push(sub);
  }

  setFiltro() {
    const nome = this.f?.nome.value;
    const idDirezione = this.f?.idDirezione.value;
    const ruolo = this.f?.ruolo.value;
    const sort = this.sortType + "," + this.sortDirection;
    if (
      this.f?.nome?.value ||
      this.f?.idDirezione?.value ||
      this.f?.ruolo?.value
    ) {
      this.utentiService.setFiltroUtenti({
        nome: this.f?.nome?.value,
        idDirezione: this.f?.idDirezione?.value,
        ruolo: this.f?.ruolo?.value,
        sort: this.sortType + "," + this.sortDirection,
      });
    } else {
      this.utentiService.removeFiltroUtenti();
    }
  }
}
