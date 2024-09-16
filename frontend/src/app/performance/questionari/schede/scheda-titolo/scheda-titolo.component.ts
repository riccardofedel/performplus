import { Component, OnInit, NgZone, ViewChild, ElementRef, Input, Output, EventEmitter, OnChanges, SimpleChanges, HostListener } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { TipoNodo } from 'src/app/_services';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FieldsService } from 'src/app/_services/fields.service';
import { environment } from 'src/environments/environment';
import { TipoNodoQuestionario } from "src/app/_services/questionari.service";

@Component({
  selector: "scheda-titolo",
  templateUrl: "./scheda-titolo.component.html",
  styleUrls: ["./scheda-titolo.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaTitoloComponent implements OnInit, OnChanges {
  @ViewChild("menuprogdropdown") menuprogdropdown: ElementRef | undefined;

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

  menuprog: boolean = false;

 

  TipoNodo = TipoNodo;

  @Input() currentNodo: any | undefined;
  @Input() writer: boolean | undefined;
  @Input() reader: boolean | undefined;
  @Input() admin: boolean | undefined;
  @Input() scheda: string | undefined;
  @Output() vaiAScheda: EventEmitter<any> = new EventEmitter<any>();
  @Output() eseguiRichiesta: EventEmitter<any> = new EventEmitter<any>();

  ngOnInit() {
    this.reload();
  }

  @HostListener("document:mousedown", ["$event"])
  onGlobalClick(event: any): void {
    if (!this.menuprogdropdown?.nativeElement.contains(event.target)) {
      // clicked outside => close dropdown list
      this.menuprog = false;
    }
  }
  reload() {}

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

  aggiungiNodo() {
    this.eseguiRichiesta.emit("aggiungiNodo");
  }
  modificaNodo() {
    this.eseguiRichiesta.emit("modificaNodo");
  }

  elimina() {
    this.eseguiRichiesta.emit("elimina");
  }

  getCurrentNodoTipoId(): TipoNodoQuestionario {
    return this.toTipoNodo(this.currentNodo?.tipoNodo);
  }
  getCurrentFoglia(): boolean {
    return this.currentNodo?.foglia;
  }
  toTipoNodo(tipo: number | undefined): TipoNodoQuestionario {
    if (!tipo) return TipoNodoQuestionario.root;
    switch (tipo) {
      case 0:
        return TipoNodoQuestionario.root;
      case 1:
        return TipoNodoQuestionario.questionario;
      case 2:
        return TipoNodoQuestionario.ambito;
      case 3:
        return TipoNodoQuestionario.valore;
      default:
        return TipoNodoQuestionario.root;
    }
  }
}


