import { Component, OnInit, NgZone, ViewChild, OnDestroy, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup, Validators, FormsModule, NgForm } from '@angular/forms';
import { Permission, PermissionService, RegolamentoService } from 'src/app/_services';
import { Regolamento, User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { StruttureService } from 'src/app/_services/strutture.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
//import { AggiungistrutturaComponent } from './aggiungistruttura/aggiungistruttura.component';
//import { AggiungirisorsaComponent } from './aggiungirisorsa/aggiungirisorsa.component';
import { NgSelectModule, NgOption } from '@ng-select/ng-select';
//import { AlberoComponent } from './albero/albero.component';
import { environment } from '../../../environments/environment';


@Component({
  selector: "app-regolamento",
  templateUrl: "./regolamento.component.html",
  styleUrls: ["./regolamento.component.scss"],
  providers: [TranslatePipe],
  host: {
    "(document:click)": "onClick($event)",
  },
})
export class RegolamentoComponent implements OnInit, OnDestroy {
  constructor(
    public permissionService: PermissionService,
    private router: Router,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    public fb: FormBuilder,
    private struttureService: StruttureService,
    private authenticationService: AuthenticationService,
    private regolamentoService: RegolamentoService
  ) {}

  loading = false;
  cercaForm!: FormGroup;
  paginationService: PaginationService | undefined;

  currentUser: User | undefined;
  subs: Subscription[] = [];
  regolamenti: Regolamento[] = [];
  tuttiRegolamenti: Regolamento[] = [];
  Permission = Permission;
  pageNum = 0;
  pageSize = 10;
  sorter = null;

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
    let sub: Subscription = this.regolamentoService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno
      )
      .subscribe((result) => {
        this.loading = false;
        this.tuttiRegolamenti = result;
        this.paginationService?.updateCount(this.tuttiRegolamenti.length);
        this.refreshTable();
      });
      this.subs.push(sub);
  }

  refreshTable() {
    const first = this.paginationService?.getFirst()
      ? this.paginationService?.getFirst()
      : 0;
    const last = this.paginationService?.getLast()
      ? this.paginationService?.getLast()
      : 0;
    this.regolamenti = this.tuttiRegolamenti?.slice(first - 1, last);
  }
  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  onSubmit() {
    this.loading = true;
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

  elimina(userid: number | undefined) {
    if (!userid) {
      return;
    }
    Swal.fire({
      title: "Sei sicuro?",
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina il regolamento #" + userid,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        let sub: Subscription = this.regolamentoService
          .deleteRegolamento(userid)
          .subscribe(
            (result) => {
              this.refresh();
            },
            (error) => {
              Swal.fire({
                title: this.translate.instant("sorry"),
                text: "Errore nella cancellazione del regolamento: " + error,
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

