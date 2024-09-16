import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { Permission, PermissionService, RisorseService } from 'src/app/_services';
import { Risorsa, Simple, User } from 'src/app/_models';
import { FieldsService } from 'src/app/_services/fields.service';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';

@Component({
  selector: "app-risorse",
  templateUrl: "./risorse.component.html",
  styleUrls: ["./risorse.component.scss"],
  providers: [TranslatePipe],
})
export class RisorseComponent implements OnInit, OnDestroy {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public fieldsService: FieldsService,
    private risorseService: RisorseService,
    private authenticationService: AuthenticationService,
    public permissionService: PermissionService
  ) {}

  sortType = "cognome";
  sortDirection = "ASC";

  Permission = Permission;

  isLoadingResult = false;

  subs: Subscription[] = [];

  paginationService: PaginationService | undefined;

  sorter = null;

  loading: boolean = false;

  risorse: Risorsa[] = [];

  cognomi: any[] = [];

  cercaForm!: FormGroup;

  currentUser: User | undefined;

  keyword = "description";

  onChangeSearch($event: any) {
    this.aggiornaFiltroCognomi($event);
  }

  ngOnInit() {
    const filtro: any = this.risorseService.getFiltroRisorse();
    if (filtro) {
      this.cercaForm = this.formBuilder.group({
        interno: [filtro.interno],
        soloAttiveAnno: [filtro.soloAttiveAnno],
        cerca: [filtro.cerca],
      });
    } else {
      this.cercaForm = this.formBuilder.group({
        interno: [true],
        soloAttiveAnno: [true],
        cerca: [""],
      });
    }

    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.refresh();
        }
        this.aggiornaFiltroCognomi("");
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

  aggiornaFiltroCognomi(testo: string | undefined) {
    this.isLoadingResult = true;
    const interno = this.f?.interno?.value;
    const soloAttiveAnno = this.f?.soloAttiveAnno?.value;
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        0,
        20,
        interno,
        soloAttiveAnno,
        testo,
        null
      )
      .subscribe((result) => {
        let cognomi = [];
        for (let item of result.content) {
          cognomi.push({ id: item.cognome, description: item.cognome });
        }
        this.cognomi = cognomi;
        this.isLoadingResult = false;
      });
    this.subs.push(sub);
  }

  refresh() {
    this.loading = true;
    //console.log(">>>>refresh", this.f?.interno?.value);
    const pgNum = this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    const interno = this.f?.interno?.value;
    const soloAttiveAnno = this.f?.soloAttiveAnno?.value;
    const sort = this.sortType + "," + this.sortDirection;

    let filter = this.f?.cerca?.value;
    let testo = undefined;
    if (filter && filter.description) {
      testo = filter.description;
    }
   
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        pgNum - 1,
        pgSize,
        interno,
        soloAttiveAnno,
        testo,
        sort
      )
      .subscribe((result) => {
        this.loading = false;
        this.risorse = result.content;
        this.paginationService?.updateCount(result.totalElements);
      });
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  elimina(risorsaid: number | undefined) {
    if (!risorsaid) {
      return;
    }
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina l'risorsa #" + risorsaid,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.risorseService
          .deleteRisorsa(risorsaid)
          .subscribe(
            (result) => {
              this.refresh();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: this.translate.instant("errore cancellazione risorsa"),
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub);
      }
    });
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  onSubmit() {
    this.paginationService?.reset(false);
    this.refresh();
  }

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.RisorseStruttura,
      false
    );
  }

  setFiltro() {
    const interno = this.f?.interno.value;
    const soloAttiveAnno = this.f?.soloAttiveAnno.value;
    const cerca = this.f?.cerca.value;
    if (
      this.f?.interno?.value ||
      this.f?.soloAttiveAnno?.value ||
      this.f?.cerca?.value
    ) {
      this.risorseService.setFiltroRisorse({
        interno: this.f?.interno?.value,
        soloAttiveAnno: this.f?.soloAttiveAnno?.value,
        cerca: this.f?.cerca?.value,
      });
    } else {
      this.risorseService.removeFiltroRisorse();
    }
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
    this.refresh();
  }
}

