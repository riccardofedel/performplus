import { Component, OnInit, NgZone, ViewChild, OnDestroy, ElementRef, Input, Output, EventEmitter, SimpleChanges, OnChanges } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { Permission, PermissionService, ProgrammazioneService, TipoNodo } from 'src/app/_services';
import { User } from 'src/app/_models';
import { PaginationService } from 'src/app/_services/pagination.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FieldsService } from 'src/app/_services/fields.service';
import { environment } from 'src/environments/environment';
import { PesaturapopupComponent } from '../../modal/pesaturapopup/pesaturapopup.component';

@Component({
  selector: "scheda-pesatura",
  templateUrl: "./scheda-pesatura.component.html",
  styleUrls: ["./scheda-pesatura.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaPesaturaComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public permissionService: PermissionService,
    private modalService: NgbModal,
    public translate: TranslateService,
    private programmazioneService: ProgrammazioneService,
    public fieldsService: FieldsService,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService
  ) {}

  Permission = Permission;

  currentUser: User | undefined;

  loading = false;

  TipoNodo = TipoNodo;

  subs: Subscription[] = [];

  paginationService: PaginationService | undefined;

  result: any | undefined;

  //solo quelle che visualizzo
  obiettivi: any[] = [];

  //tutte
  tuttiObiettivi: any[] = [];

  closed: any | undefined;

  @Input() currentProgrammazione: any | undefined;
  @Output() cambiaScheda: EventEmitter<any> = new EventEmitter<any>();

  anno = 0;

  ngOnInit() {
    this.reload();
  }

  refreshTable() {
    const first = this.paginationService?.getFirst()
      ? this.paginationService?.getFirst()
      : 0;
    const last = this.paginationService?.getLast()
      ? this.paginationService?.getLast()
      : 0;
    this.obiettivi = this.tuttiObiettivi?.slice(first - 1, last);
  }

  refreshDataTable() {
    if (!this.currentProgrammazione || !this.currentProgrammazione.id) {
      return;
    }
    this.loading = true;
    let sub: Subscription = this.programmazioneService
      .getObiettiviPesatura(this.currentProgrammazione.id)
      .subscribe((result) => {
        this.loading = false;
        this.result = result;
        this.tuttiObiettivi = [result?.nodo, ...result?.figli];
        this.paginationService?.updateCount(
          this.tuttiObiettivi?.length ? this.tuttiObiettivi?.length : 0
        );
        this.refreshTable();
      });
      this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  modificaPesaturaPopup(id: string, tipoNodo: string) {
    const modalRef = this.modalService.open(PesaturapopupComponent, {
      size: "xl",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.id = id;
    modalRef.componentInstance.tipoNodo = tipoNodo;
    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          this.refreshDataTable();
        }
      },
      (reason) => {}
    );
  }

  getCurrentNodoTipoId() {
    if (this.currentProgrammazione?.tipoNodo) {
      return this.currentProgrammazione?.tipoNodo;
    }
    return 0;
  }

  readAllowed() {
    return this.result?.enabling?.read;
  }

  writeAllowed() {
    if (!this.result?.enabling.write) {
      return false;
    }
    return this.permissionService.getPermission(
      this.currentUser,
      Permission.DupProgrammazione,
      false
    );
  }

  vaiAScheda(scheda: any) {
    this.cambiaScheda.emit(scheda);
  }
  eseguiRichiesta(cmd: any) {}
  reload() {
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        this.anno = this.currentUser?.operator?.anno
          ? this.currentUser?.operator?.anno
          : new Date().getFullYear();
        this.refreshDataTable();
        this.paginationService?.reset(false);
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
  ngOnChanges(changes: SimpleChanges) {
    //console.log("schda-pesatura changes:", changes);
    this.reload();
  }
}

