import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { AnagraficheService, Permission, PermissionService, UtentiService } from 'src/app/_services';
import { Anagrafica, User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { FieldsService } from 'src/app/_services/fields.service';
import { filter } from 'rxjs/operators';
import { ExcelService } from 'src/app/_services/excel.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: "app-anagrafica",
  templateUrl: "./anagrafica.component.html",
  styleUrls: ["./anagrafica.component.scss"],
  providers: [TranslatePipe],
})
export class AnagraficaComponent implements OnInit, OnDestroy {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private readonly zone: NgZone,
    public fieldService: FieldsService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public permissionService: PermissionService,
    private anagraficheService: AnagraficheService,
    private authenticationService: AuthenticationService,
    private excelService: ExcelService
  ) {}

  sortType = "codiceCompleto";
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

  items: Anagrafica[] = [];

  cercaForm!: FormGroup;

  page: string | undefined;

  ngOnInit() {
    let sub1: Subscription = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((val) => {
        this.refresh(true);
      });
      this.subs.push(sub1);
    let sub2: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.refresh(true);
        }
      }
    );
      this.subs.push(sub2);
    this.paginationService = new PaginationService();

    let sub3: Subscription = this.paginationService
      .getMessage()
      .subscribe((message) => {
        this.refresh(false);
      });
      this.subs.push(sub3);
    this.cercaForm = this.formBuilder.group({
      cerca: [""],
    });
  }

  refresh(first: Boolean) {
    let page = this.route.snapshot.paramMap.get("page");
    if (page) {
      this.page = page;
    } else {
      this.page = "tag";
    }

    this.loading = true;
    const pgNum = first ? 1 : this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    const filter = this.f?.cerca.value;
    const sort = this.sortType + "," + this.sortDirection;
    let sub: Subscription = this.anagraficheService
      .search(
        this.page,
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        pgNum - 1,
        pgSize,
        filter,
        sort
      )
      .subscribe(
        (result) => {
          this.loading = false;
          this.length = result.totalElements;
          this.items = result.content;
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

  elimina(id: number | undefined) {
    if (!id) {
      return;
    }
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina anagrafica #" + id,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.anagraficheService
          .delete(this.page, id)
          .subscribe(
            (result) => {
              this.refresh(true);
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella cancellazione dell'anagrafica: " + error,
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
    const filter = this.f?.cerca.value;
    const sort = this.sortType + "," + this.sortDirection;

    let sub: Subscription = this.anagraficheService
      .search(
        this.page,
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        0,
        10000,
        filter,
        sort
      )
      .subscribe((result) => {
        list = result.content;

        this.excelService.exportAsExcelFile(list, this.page!);
      });
      this.subs.push(sub);
  }

  savePdf(): void {
    var list: any[] = [];
    var rows = [[]];
    const filter = this.f?.cerca.value;
    const sort = this.sortType + "," + this.sortDirection;

    let sub: Subscription = this.anagraficheService
      .search(
        this.page,
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        0,
        10000,
        filter,
        sort
      )
      .subscribe((result) => {
        list = result.content;

        list.forEach((t) => {
          var r: any = [
            t.codice,
            t.descrizione,
            t.inizioValidita,
            t.fineValidita,
            t.codiceCompletoPadre,
          ];
          rows.push(r);
        });

        var doc = new jsPDF();
        autoTable(doc, {
          head: [["codice", "descrizione", "inizio", "fine", "padre"]],
          body: rows,
        });
        doc.save(this.page + ".pdf");
      });
      this.subs.push(sub);
  }
}