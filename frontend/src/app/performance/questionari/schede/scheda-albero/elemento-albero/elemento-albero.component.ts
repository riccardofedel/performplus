import { Component, OnInit, NgZone, OnDestroy, Input } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { FormBuilder } from '@angular/forms';
import { AlberoQuestionariService, QuestionariService} from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2'
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { environment } from 'src/environments/environment';
import { TipoNodoQuestionario } from "src/app/_services/questionari.service";


@Component({
  selector: "elemento-albero",
  templateUrl: "./elemento-albero.component.html",
  styleUrls: ["./elemento-albero.component.scss"],
  providers: [TranslatePipe],
})
export class ElementoAlberoComponent implements OnInit, OnDestroy {
  constructor(
    public alberoQuestionariService: AlberoQuestionariService,
    public translate: TranslateService,
    public fb: FormBuilder,
    private questionariService: QuestionariService,
    private authenticationService: AuthenticationService
  ) {}

  @Input() elemento: any | undefined;
  @Input() livello: number | undefined;

  subs: Subscription[] = [];

  currentUser: User | undefined;



  loading = false;

  idEnte: number | undefined;
  anno: number | undefined;

  isClosed(id: string) {
    return this.alberoQuestionariService.closed[id];
  }

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.idEnte = this.currentUser.operator?.idEnte;
          this.anno = this.currentUser.operator?.anno;
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

  toggle(nodo: any) {
    //console.log("---toggle:", nodo);
    if (!this.alberoQuestionariService.closed[nodo.id]) {
      this.alberoQuestionariService.closed[nodo.id] = true;
    } else {
      this.alberoQuestionariService.closed[nodo.id] = false;
      //se qui mancano i dati li devo caricare e inserire in albero
      if (!nodo.children) {
        let sub: Subscription = this.questionariService
          .getChildren(this.idEnte!, this.anno!,
            this.questionariService.toTipoNodo(nodo.tipoNodo),
            nodo.id
          )
          .subscribe((result) => {
            nodo.children = result;
            for (let i in result) {
              //per percorrere l'albero all'indietro
              result[i].puntatorePadre = nodo;
              this.alberoQuestionariService.closed[result[i].id] = true;
            }
          });
        this.subs.push(sub);
      }
    }
  }

  getImage(nodo: any) {
    switch (nodo?.tipoNodo) {
      case TipoNodoQuestionario.questionario:
        return "assets/img/folder1.png";
      case TipoNodoQuestionario.ambito:
        return "assets/img/folder3.png";
      case TipoNodoQuestionario.valore:
        return "assets/img/folder4.png";
    }
    return "assets/img/folder1.png";
  }

  select(nodo: any) {
    this.alberoQuestionariService.currentNodo = nodo;
    this.alberoQuestionariService.updateNodo(nodo.id);

    this.alberoQuestionariService.updateScheda("/questionari/scheda");
  }
}

