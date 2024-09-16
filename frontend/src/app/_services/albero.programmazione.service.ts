import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "../_models";
import { Subject, Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { ProgrammazioneService } from ".";
import { Schede } from "../programmazione/constants";

@Injectable({ providedIn: "root" })
export class AlberoProgrammazioneService {
  constructor(private programmazioneService: ProgrammazioneService) {
    this.initializeClosed();
  }

  private subject = new Subject<any>();

  closed: any | undefined;

  currentProgrammazione: any | undefined;
  currentScheda: string = Schede.programmazione;
  currentSearch: any | undefined;
  isConsuntivazione: boolean = false;

  /**
   * limite ricorsione a 20
   */
  limit = 0;

  /**
   * currentProgrammazione.tipoNodo
   *  0 piao
   *  1 area
   *  2 obiettivo
   *  3 azione
   *  4 fase
   */

  //programmazione albero a partire dalla radice
  programmazioneAperta: any | undefined;

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

  initializeClosed() {
    this.closed = {};
    this.programmazioneAperta = undefined;
  }

  /**
   * mi arrivano i dati li dispongo nell'albero
   */
  preparaDati(result: any) {
    //console.log('-------->prepara dati');
    this.programmazioneAperta = result;
    if (
      !this.currentProgrammazione ||
      this.currentProgrammazione.id !== result.id
    ) {
      this.currentProgrammazione = result;
    }
    this.limit = 0;
    this.preparaDatiFigli(result);
    this.subject.next({ programmazione: this.currentProgrammazione });
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

  /**
   * @param currentUser come quella sopra ma con ricerca
   */
  caricaProgrammazioneSearch(
    currentUser: User | undefined,
    value: any,
    isConsuntivazione = false
  ) {
    if (!currentUser) return;
    this.programmazioneService
      .search(
        currentUser?.operator?.idEnte,
        currentUser?.operator?.anno,
        value,
        isConsuntivazione
      )
      .subscribe((result) => {
        this.preparaDati(result);
      });
  }

  updateProgrammazione(programmazione: number) {
    this.subject.next({ programmazione: programmazione });
  }

  updateSearchData(data: any) {
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
  consuntivazione(b: boolean) {
    this.isConsuntivazione = b;
  }
}
