import { Component, OnInit, NgZone, ViewChild, ElementRef, Input, Output, EventEmitter, OnChanges, SimpleChanges, HostListener, OnDestroy } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { TipoNodo } from 'src/app/_services';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FieldsService } from 'src/app/_services/fields.service';
import { environment } from 'src/environments/environment';
import { Azioni, Schede } from "src/app/programmazione/constants";
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';

@Component({
  selector: "scheda-titolo",
  templateUrl: "./scheda-titolo.component.html",
  styleUrls: ["./scheda-titolo.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaTitoloComponent implements OnInit, OnChanges, OnDestroy {
  constructor(
    private modalService: NgbModal,
    public translate: TranslateService,
    public fieldsService: FieldsService,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    this.reload();
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  @ViewChild("menuprogdropdown") menuprogdropdown: ElementRef | undefined;

  menuprog: boolean = false;

  TipoNodo = TipoNodo;

  @Input() currentProgrammazione: any | undefined;
  @Input() nodo: any | undefined;
  @Input() writer: boolean | undefined;
  @Input() reader: boolean | undefined;
  @Input() add: boolean | undefined;
  @Input() admin: boolean | undefined;
  @Input() scheda: string | undefined;
  @Output() vaiAScheda: EventEmitter<any> = new EventEmitter<any>();
  @Output() eseguiRichiesta: EventEmitter<any> = new EventEmitter<any>();

  Azioni = Azioni;
  Schede = Schede;
  currentUser: User | undefined;

  anno: number | undefined;
  idEnte: number | undefined;

  subs: Subscription[] = [];

  ngOnInit() {
    this.reload();
  }

  reload() {
    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
        //console.log(">>>>>>>>>>>>>", this.currentUser?.operator?.ruolo);
        if (this.currentUser && this.currentUser.operator) {
          this.anno = this.currentUser.operator.anno;
          this.idEnte = this.currentUser.operator.idEnte;
        }
      })
    );
  }

  getCurrentNodoTipoId() {
    if (this.currentProgrammazione?.tipoNodo) {
      return this.currentProgrammazione?.tipoNodo;
    }
    return 0;
  }

  onClick(event: any) {
    if (
      this.menuprogdropdown &&
      !this.menuprogdropdown.nativeElement.contains(event.target)
    ) {
      this.menuprog = false;
    }
  }

  readAllowed() {
    return this.reader;
  }

  writeAllowed() {
    return this.writer;
  }

  addAllowed() {
    return this.add;
  }
  vaiASchedaIndicatori() {
    this.vaiAScheda.emit(Schede.indicatori);
  }
  vaiASchedaPesatura() {
    this.vaiAScheda.emit(Schede.pesatura);
  }
  aggiungiNodo() {
    this.eseguiRichiesta.emit(Azioni.aggiungiNodo);
  }
  spostaNodo() {
    this.eseguiRichiesta.emit(Azioni.spostaNodo);
  }
  modificaNodo() {
    this.eseguiRichiesta.emit(Azioni.modificaNodo);
  }
  elimina() {
    this.eseguiRichiesta.emit(Azioni.elimina);
  }
  vaiAProgrammazione() {
    this.vaiAScheda.emit(Schede.programmazione);
  }
  aggiungiIndicatore() {
    this.eseguiRichiesta.emit(Azioni.aggiungiIndicatore);
  }
  note() {
    this.eseguiRichiesta.emit(Azioni.note);
  }
  forzature() {
    this.eseguiRichiesta.emit(Azioni.forzature);
  }

  vaiASchedaRisorse() {
    this.vaiAScheda.emit(Schede.risorse);
  }

  aggiungiRisorsa() {
    this.eseguiRichiesta.emit(Azioni.aggiungiRisorsa);
  }
  @HostListener("document:mousedown", ["$event"])
  onGlobalClick(event: any): void {
    if (!this.menuprogdropdown?.nativeElement.contains(event.target)) {
      // clicked outside => close dropdown list
      this.menuprog = false;
    }
  }
}

