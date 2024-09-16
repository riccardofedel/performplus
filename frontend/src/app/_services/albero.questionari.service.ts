import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../_models';
import { Subject, Observable } from "rxjs";
import { QuestionariService } from './questionari.service';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: "root" })
export class AlberoQuestionariService {
  constructor(private questionariService: QuestionariService) {
    this.initializeClosed();
  }

  private subject = new Subject<any>();

  closed: any | undefined;

  currentNodo: any | undefined;
  currentScheda: string = "/questionario/scheda";
  currentSearch: any | undefined;


  /**
   * limite ricorsione a 20
   */
  limit = 0;

  //questionario albero a partire dalla radice
  nodoAperto: any | undefined;

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

  initializeClosed() {
    this.closed = {};
  }

  caricaQuestionarioSearch(
    currentUser: User,
    search: any | undefined
  ) {
    this.questionariService
      .root(currentUser!.operator!.idEnte, currentUser!.operator!.anno,search)
      .subscribe((result) => {
        this.preparaDati(result);
      });
  }

  /**
   * mi arrivao i dati li dispongo nell'albero
   */
  preparaDati(result: any) {
    //console.log("------albero:", result);
    this.nodoAperto = result;
    this.currentNodo = result;
    this.limit = 0;
    this.preparaDatiFigli(result);
    this.subject.next({ nodo: this.currentNodo });
  }

  preparaDatiFigli(result: any) {
    //limito arbitrariamente la ricorsione nel caso ci fossero problemi a backend
    this.limit++;
    if (this.limit == 20) {
      return;
    }
    if (result.children) {
      for (let i in result.children) {
        result.children[i].puntatorePadre = result;
        //se i children hanno già i children li dò aperti
        //succede nella ricerca quando mi arrivano tutti
        //altrimenti li dò chiusi
        if (result.children[i].children) {
          this.closed[result.children[i].id] = false;
          this.preparaDatiFigli(result.children[i]);
        } else {
          this.closed[result.children[i].id] = true;
        }
      }
    }
  }

  updateNodo(nodo: number) {
    this.subject.next({ nodo: nodo });
  }

  updateSearchData(data: any) {
    //console.log("updateSearchData:",data);
    this.currentSearch = { ...data };
  }
  getSearchData(): any {
    return this.currentSearch;
  }
  updateScheda(scheda: any) {
    this.currentScheda = scheda;
  }
  getScheda(): any {
    return this.currentScheda;
  }

}

