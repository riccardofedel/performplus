import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Risorsa } from '../_models';

@Injectable({
  providedIn: "root",
})
export class StruttureService {
  constructor(private http: HttpClient) {}

  env = environment;

  albero(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "struttura/albero?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getStruttura(idStruttura: number | undefined) {
    return this.http.get<any>(this.env.API_URL + "struttura/" + idStruttura);
  }

  root(
    idEnte: number | undefined,
    anno: number | undefined,
    intestazione: string | undefined,
    responsabile: string | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "struttura/root?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        (intestazione ? "&filter=" + intestazione : "") +
        (responsabile ? "&responsabile=" + responsabile : "")
    );
  }

  getRisorseAssociate(idStruttura: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "struttura/getRisorseAssociate?idOrganizzazione=" +
        idStruttura
    );
  }

  getRisorseAssociabili(idStruttura: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "struttura/getRisorseAssociabili?idOrganizzazione=" +
        idStruttura
    );
  }

  preparaStruttura(idPadre: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "struttura/prepareNuovaStruttura?idPadre=" + idPadre
    );
  }

  getChildren(idStruttura: number) {
    return this.http.get<any>(
      this.env.API_URL + "struttura/children/" + idStruttura
    );
  }

  getLivelloList(idEnte: number) {
    return this.http.get<any>(
      this.env.API_URL + "struttura/livello?idEnte=" + (idEnte ? idEnte : 0)
    );
  }

  setStruttura(data: any) {
    return this.http.post<any>(this.env.API_URL + "struttura", data);
  }

  updateStruttura(idStruttura: number, data: any) {
    return this.http.put<any>(this.env.API_URL + "struttura", {
      id: idStruttura,
      ...data,
    });
  }

  associaRisorse(data: any) {
    return this.http.post<any>(
      this.env.API_URL + "struttura/associaRisorse",
      data
    );
  }

  deleteStruttura(idStruttura: number | undefined) {
    if (!idStruttura) {
      idStruttura = 0;
    }
    return this.http.delete<any>(
      this.env.API_URL + "struttura/" + encodeURIComponent(idStruttura)
    );
  }

  rimuoviRisorsa(id: number | undefined) {
    return this.http.delete<any>(
      this.env.API_URL + "struttura/rimuoviRisorsa/" + id
    );
  }

  tipi() {
    return this.http.get<any>(this.env.API_URL + "struttura/tipi");
  }
  tipologie() {
    return this.http.get<any>(this.env.API_URL + "struttura/tipologie");
  }
  updateDisponibilita(
    idStruttura: number | undefined,
    value: number | undefined
  ) {
    return this.http.post<any>(
      this.env.API_URL + "struttura/modificaDisponibilitaRisorsa/",
      { id: idStruttura, disponibilita: value }
    );
  }
  getDipendenti(
    idStruttura: number,
    anno: number,
    pageNum: number,
    pageSize: number,
    testo: string | undefined,
    sort: any
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "struttura/" +
        idStruttura +
        "/risorse" +
        "?anno=" +
        anno +
        "&page=" +
        pageNum +
        "&size=" +
        pageSize +
        (testo ? "&cognome=" + encodeURI(testo) : "") +
        (sort !== "" && sort !== undefined ? "&sort=" + encodeURI(sort) : "")
    );
  }
}


