import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../_models';
import { Subject, Observable } from "rxjs";
import { StruttureService } from './strutture.service';
import { environment } from 'src/environments/environment';
import { Schede } from '../struttura/organigramma/constants';

@Injectable({ providedIn: "root" })
export class AlberoStruttureService {
  constructor(private struttureService: StruttureService) {
    this.initializeClosed();
  }

  private subject = new Subject<any>();

  closed: any | undefined;

  currentStruttura: any | undefined;
  currentScheda: string = Schede.struttura;
  currentSearch: any | undefined;

  /**
   * limite ricorsione a 20
   */
  limit = 0;

  //struttura albero a partire dalla radice
  strutturaAperta: any | undefined;

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

  initializeClosed() {
    this.closed = {};
  }

  caricaStrutturaSearch(
    currentUser: User | undefined,
    search: any | undefined
  ) {
    this.struttureService
      .root(
        currentUser?.operator?.idEnte,
        currentUser?.operator?.anno,
        search?.intestazione,
        search?.responsabile
      )
      .subscribe((result) => {
        this.preparaDati(result);
      });
  }

  /**
   * mi arrivao i dati li dispongo nell'albero
   */
  preparaDati(result: any) {
    this.strutturaAperta = result;
    this.currentStruttura = result;
    this.limit = 0;
    this.preparaDatiFigli(result);
    this.subject.next({ struttura: this.currentStruttura });
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
          this.closed[result.children[i].id] = result.children[i].level > 1;
          this.preparaDatiFigli(result.children[i]);
        } else {
          this.closed[result.children[i].id] = true;
        }
      }
    }
  }

  updateStruttura(struttura: number) {
    this.subject.next({ struttura: struttura });
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

