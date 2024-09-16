import { Component, OnInit, NgZone, OnDestroy, Input } from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { FormBuilder } from "@angular/forms";
import {
  AlberoProgrammazioneService,
  ProgrammazioneService,
} from "src/app/_services";
import { User } from "src/app/_models";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { environment } from "src/environments/environment";
import { Schede } from "src/app/programmazione/constants";

@Component({
  selector: "elemento-albero",
  templateUrl: "./elemento-albero.component.html",
  styleUrls: ["./elemento-albero.component.scss"],
  providers: [TranslatePipe],
})
export class ElementoAlberoComponent implements OnInit, OnDestroy {
  constructor(
    public alberoProgrammazioneService: AlberoProgrammazioneService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private programmazioneService: ProgrammazioneService,
    private authenticationService: AuthenticationService
  ) {}

  @Input() elementoProgrammazione: any | undefined;
  @Input() livello: number | undefined;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  @Input() isConsuntivazione = false;

  isClosed(id: string) {
    return this.alberoProgrammazioneService.closed[id];
  }

  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
        }
      }
    );
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  toggle(programmazione: any) {
    if (!this.alberoProgrammazioneService.closed[programmazione.id]) {
      this.alberoProgrammazioneService.closed[programmazione.id] = true;
    } else {
      this.alberoProgrammazioneService.closed[programmazione.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!programmazione.children) {
        let sub: Subscription = this.programmazioneService
          .getChildren(programmazione.id, this.isConsuntivazione)
          .subscribe((result) => {
            programmazione.children = result;
            for (let i in result) {
              //per percorrere l'albero all'indietro
              result[i].puntatorePadre = programmazione;
              this.alberoProgrammazioneService.closed[result[i].id] = true;
            }
          });
        this.subs.push(sub);
      }
    }
  }

  getImage(programmazione: any) {
    const closed = this.isClosed(programmazione.id);
    if (closed) {
      switch (programmazione?.tipoNodo) {
        case 0:
          return "./assets/img/folder-closed.gif";
        case 1:
          return "./assets/img/green-close.png";
        case 2:
          return "./assets/img/blue-close.png";
        case 3:
          return "./assets/img/red-close.png";
        case 4:
          return "./assets/img/yellow-close.png";
        default:
          return "./assets/img/folder-closed.gif";
      }
    } else {
      switch (programmazione?.tipoNodo) {
        case 0:
          return "./assets/img/folder-open.gif";
        case 1:
          return "./assets/img/green-open.png";
        case 2:
          return "./assets/img/blue-open.png";
        case 3:
          return "./assets/img/red-open.png";
        case 4:
          return "./assets/img/yellow-open.png";
        default:
          return "./assets/img/folder-open.gif";
      }
    }
  }

  select(programmazione: any) {
    this.alberoProgrammazioneService.currentProgrammazione = programmazione;
    this.alberoProgrammazioneService.updateProgrammazione(programmazione.id);
    this.alberoProgrammazioneService.consuntivazione(this.isConsuntivazione);
    if (this.isConsuntivazione) {
      this.alberoProgrammazioneService.updateScheda(Schede.consuntivazione);
    } else {
      this.alberoProgrammazioneService.updateScheda(Schede.programmazione);
    }
  }
}
