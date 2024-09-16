import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { AmministratoriService, Permission, PermissionService } from 'src/app/_services';
import { Amministratore, User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';

@Component({
  selector: "app-amministratori",
  templateUrl: "./amministratori.component.html",
  styleUrls: ["./amministratori.component.scss"],
  providers: [TranslatePipe],
})
export class AmministratoriComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private amministratoriService: AmministratoriService
  ) {}

  Permission = Permission;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  paginationService: PaginationService | undefined;

  pageNum = 0;
  pageSize = 10;
  sorter = null;

  length = null;

  loading: boolean = false;

  amministratori: Amministratore[] = [];

  cercaForm!: FormGroup;

  ngOnInit() {
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
        this.refresh();
      });
      this.subs.push(sub2);
    this.cercaForm = this.formBuilder.group({
      cerca: [""],
    });
  }

  refresh() {
    this.loading = true;
    const pgNum = this.paginationService?.getPage() || 1;
    const pgSize = this.paginationService?.getNumber() || 10;
    const filter = this.f?.cerca.value;
    const sort = "";
    let sub: Subscription = this.amministratoriService
      .search(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        pgNum - 1,
        pgSize,
        filter,
        sort
      )
      .subscribe((result) => {
        this.loading = false;
        this.length = result.totalElements;
        this.amministratori = result.content;
        this.paginationService?.updateCount(result.totalElements);
      });
      this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  elimina(amministratoreid: number | undefined) {
    if (!amministratoreid) {
      return;
    }
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina l'amministratore #" + amministratoreid,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.amministratoriService
          .deleteAmministratore(amministratoreid)
          .subscribe(
            (result) => {
              this.refresh();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text:
                  "Errore nella cancellazione dell'amministratore: " + error,
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

  writeAllowed() {
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.AmministratoriStruttura,
      false
    );
  }

  onSubmit() {
    this.refresh();
  }
}

