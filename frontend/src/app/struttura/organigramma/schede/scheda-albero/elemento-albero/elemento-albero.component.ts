import { Component, OnInit, NgZone, OnDestroy, Input } from "@angular/core";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { FormBuilder } from "@angular/forms";
import { AlberoStruttureService, StruttureService } from "src/app/_services";
import { User } from "src/app/_models";
import { Subscription } from "rxjs";
import Swal from "sweetalert2";
import { AuthenticationService } from "src/app/_services/authentication.service";
import { environment } from "src/environments/environment";
import { Schede } from "src/app/struttura/organigramma/constants";

@Component({
  selector: "elemento-albero",
  templateUrl: "./elemento-albero.component.html",
  styleUrls: ["./elemento-albero.component.scss"],
  providers: [TranslatePipe],
})
export class ElementoAlberoComponent implements OnInit, OnDestroy {
  constructor(
    public alberoStrutturaService: AlberoStruttureService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private struttureService: StruttureService,
    private authenticationService: AuthenticationService
  ) {}

  @Input() elemento: any | undefined;
  @Input() livello: number | undefined;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loading = false;

  isClosed(id: string) {
    return this.alberoStrutturaService.closed[id];
  }

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
        }
      }
    );
    this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  toggle(struttura: any) {
    if (!this.alberoStrutturaService.closed[struttura.id]) {
      this.alberoStrutturaService.closed[struttura.id] = true;
    } else {
      this.alberoStrutturaService.closed[struttura.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!struttura.children) {
        let sub: Subscription = this.struttureService
          .getChildren(struttura.id)
          .subscribe((result) => {
            struttura.children = result;
            for (let i in result) {
              //per percorrere l'albero all'indietro
              result[i].puntatorePadre = struttura;
              this.alberoStrutturaService.closed[result[i].id] = true;
            }
          });
        this.subs.push(sub);
      }
    }
  }

  getImage(livello: number | undefined, id: string) {
    const closed = this.isClosed(id);
    if (closed) {
      switch (livello) {
        case 0:
          return "assets/img/folder-closed.gif";
        case 1:
          return "assets/img/blue-close.png";
        case 2:
          return "assets/img/red-close.png";
        case 3:
          return "assets/img/yellow-close.png";
        case 4:
          return "assets/img/green-close.png";
        default:
          return "assets/img/folder-closed.gif";
      }
    } else {
      switch (livello) {
        case 0:
          return "assets/img/folder-open.gif";
        case 1:
          return "assets/img/blue-open.png";
        case 2:
          return "assets/img/red-open.png";
        case 3:
          return "assets/img/yellow-open.png";
        case 4:
          return "assets/img/green-open.png";
        default:
          return "assets/img/folder-open.gif";
      }
    }
  }

  select(struttura: any) {
    //console.log("elemento-albero:",struttura);
    this.alberoStrutturaService.currentStruttura = struttura;
    this.alberoStrutturaService.updateStruttura(struttura.id);
    this.alberoStrutturaService.updateScheda(Schede.struttura);
  }
}
