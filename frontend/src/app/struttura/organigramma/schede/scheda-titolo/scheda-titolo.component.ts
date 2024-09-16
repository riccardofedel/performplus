import { Component, OnInit, NgZone, ViewChild, ElementRef, Input, Output, EventEmitter, OnChanges, SimpleChanges, HostListener } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { TipoNodo } from 'src/app/_services';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FieldsService } from 'src/app/_services/fields.service';
import { environment } from 'src/environments/environment';
import { Azioni, Schede } from '../../constants';

@Component({
  selector: "scheda-titolo",
  templateUrl: "./scheda-titolo.component.html",
  styleUrls: ["./scheda-titolo.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaTitoloComponent implements OnInit, OnChanges {
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

  @ViewChild("menuprogdropdown") menuprogdropdown: ElementRef | undefined;

  menuprog: boolean = false;

  TipoNodo = TipoNodo;

  @Input() currentStruttura: any | undefined;
  @Input() writer: boolean | undefined;
  @Input() reader: boolean | undefined;
  @Input() scheda: string | undefined;
  @Output() vaiAScheda: EventEmitter<any> = new EventEmitter<any>();
  @Output() eseguiRichiesta: EventEmitter<any> = new EventEmitter<any>();

  ngOnInit() {
    this.reload();
  }

  reload() {}

  getCurrentLevel() {
    if (this.currentStruttura?.level) {
      return this.currentStruttura?.level - 1;
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

  vaiASchedaRisorse() {
    this.vaiAScheda.emit(Schede.risorse);
  }
  aggiungiStruttura() {
    this.eseguiRichiesta.emit(Azioni.aggiungiStruttura);
  }
  modificaStruttura() {
    this.eseguiRichiesta.emit(Azioni.modificaStruttura);
  }

  elimina() {
    this.eseguiRichiesta.emit(Azioni.elimina);
  }

  vaiAStruttura() {
    this.vaiAScheda.emit(Schede.struttura);
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

