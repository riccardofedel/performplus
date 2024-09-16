import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Registrazione } from '../_models';

@Injectable({
  providedIn: "root",
})
export class PrioritaPiService {
  env = environment;

  constructor(private http: HttpClient) {}

  // HttpClient API get() method => Registrazione Search
  elenco(idValutatore: number, idValutato: number, idRegistrazione: number ) {
    let nro=0;
    let uri=this.env.API_URL +
        "peso-valutato/elenco?idValutato=" + idValutato + "&idValutatore=" + idValutatore+ "&idRegistrazione=" + idRegistrazione;
    return this.http.get<any>(uri);
  }

  aggiornaPesoNodo(data: any) {
    return this.http.post<any>(
      this.env.API_URL + "peso-valutato/aggiornaPesoNodo",
      data
    );
  }
  aggiornaPesoIndicatore(data: any) {
    return this.http.post<any>(
      this.env.API_URL + "peso-valutato/aggiornaPesoIndicatore",
      data
    );
  }
  getPrioritaNodo(
    id: number | undefined,
    idRegistrazione: number,
    idNodo: number
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "peso-valutato/prioritaNodo" +
        "?idRegistrazione=" +
        idRegistrazione +
        "&idNodo=" +
        idNodo +
        (id ? "&id=" + id : "")
    );
  }
  getPrioritaIndicatore(
    id: number | undefined,
    idRegistrazione: number,
    idIndicatorePiano: number
  ) {
return this.http.get<any>(
  this.env.API_URL +
    "peso-valutato/prioritaIndicatore" +
    "?idRegistrazione=" +
    idRegistrazione +
    "&idIndicatorePiano=" +
    idIndicatorePiano +
    (id ? "&id=" + id : "")
);
  }

  aggiornaPesi(data: any) {
    return this.http.post<any>(
      this.env.API_URL + "peso-valutato/aggiornaPesi",
      data
    );
  }

  getValutatori(
    idEnte: number,
    anno: number,
    cognome: string | undefined,
    idValutato: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "peso-valutato/valutatori?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "") +
        (idValutato === null || idValutato === undefined
          ? ""
          : "&idValutato=" + idValutato)
    );
  }

  getValutati(
    idEnte: number,
    anno: number,
    cognome: string | undefined,
    idValutatore: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "peso-valutato/valutati?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "") +
        (idValutatore === null || idValutatore=== undefined
          ? ""
          : "&idValutatore=" + idValutatore)
    );
  }
}
