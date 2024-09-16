import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Registrazione } from '../_models';

@Injectable({
  providedIn: "root",
})
export class RegistrazioniService {
  env = environment;

  constructor(private http: HttpClient) {}

  // HttpClient API get() method => Registrazione Search
  search(
    idEnte: number,
    anno: number,
    pageNum: number,
    pageSize: number,
    filter: any | undefined,
    sort: string
  ) {
    return this.http.post<any>(
      this.env.API_URL +
        "registrazione/search" +
        "?page=" +
        pageNum +
        "&size=" +
        pageSize +
        "&sort=" +
        sort,
      {
        idEnte,
        anno,
        ...filter,
      }
    );
  }

  // HttpClient API post() method => Registrazione create
  setRegistrazione(data: any) {
    return this.http.post<any>(this.env.API_URL + "registrazione", data);
  }

  // HttpClient API delete() method => Registrazione delete
  deleteRegistrazione(idRegistrazione: number) {
    return this.http.delete<any>(
      this.env.API_URL + "registrazione/" + encodeURIComponent(idRegistrazione)
    );
  }

  // HttpClient API get() method => Indicatore read
  getRegistrazione(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL + "registrazione/" + idRegistrazione
    );
  }

  getValutatoriList(idEnte: number, anno: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "registrazione/valutatori?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }

  getRegolamentiList(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "registrazione/regolamenti?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getQuestionariList(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "registrazione/questionari?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }

  getOrganizzazioniList(idEnte: number, anno: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "registrazione/strutture?idEnte=" +
        idEnte +
        "&anno=" +
        anno
    );
  }

  updateRegistrazione(
    idEnte: number | undefined,
    idRegistrazione: number,
    data: any
  ) {
    const dataCompleta = { idEnte: idEnte, ...data };
    return this.http.put<any>(
      this.env.API_URL + "registrazione/" + encodeURIComponent(idRegistrazione),
      dataCompleta
    );
  }

  createRegistrazione(idEnte: number | undefined, data: any) {
    const dataCompleta = { idEnte: idEnte, ...data };
    //console.log("createRegistrazione", dataCompleta);
    return this.http.post<any>(
      this.env.API_URL + "registrazione",
      dataCompleta
    );
  }

  getValutatori(idEnte: number, anno: number, cognome: string | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/risorse?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "")
    );
  }

  getValutati(idEnte: number, anno: number, cognome: string | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/risorse?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "")
    );
  }
  verificaValutatore(idRisorsa: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "registrazione/verificaValutatore?idRisorsa=" +
        idRisorsa
    );
  }
  getRisorse(idEnte: number, anno: number, cognome: string | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/risorse?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "")
    );
  }
  setFiltroRegistrazioni(filtro: any) {
    localStorage.setItem("filtroRegistrazioni", JSON.stringify(filtro));
  }
  getFiltroRegistrazioni(): any {
    const filtro = localStorage.getItem("filtroRegistrazioni");
    if (filtro) {
      return JSON.parse(filtro);
    }
    return null;
  }
  removeFiltroRegistrazioni() {
    localStorage.removeItem("filtroRegistrazioni");
  }
  aggiornaScheda(idRegistrazione: number) {
    return this.http.put<any>(
      this.env.API_URL +
        "registrazione/" +
        encodeURIComponent(idRegistrazione) +
        "/aggiornaScheda",
      null
    );
  }
  undo(idRegistrazione: number) {
     return this.http.put<any>(
      this.env.API_URL +
        "registrazione/" +
        encodeURIComponent(idRegistrazione) +
        "/undo",
      null);
  }
}
