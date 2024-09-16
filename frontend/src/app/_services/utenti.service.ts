import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: "root",
})
export class UtentiService {
  constructor(private http: HttpClient) {}

  env = environment;

  // HttpClient API get() method => Search
  search(
    idEnte: number | undefined,
    anno: number | undefined,
    pageNum: number,
    pageSize: number,
    idDirezione: string | undefined,
    ruolo: string | undefined,
    nome: string | undefined,
    sort: string
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "utente/search?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        "&page=" +
        pageNum +
        "&size=" +
        pageSize +
        (idDirezione ? "&" + ("idDirezione=" + encodeURI(idDirezione)) : "") +
        (ruolo ? "&" + ("ruolo=" + encodeURI(ruolo)) : "") +
        (nome ? "&" + ("nome=" + encodeURI(nome)) : "") +
        (sort !== "" && sort !== undefined ? "&sort=" + encodeURI(sort) : "")
    );
  }

  // HttpClient API get() method => Utente read
  getUtente(idUtente: number) {
    return this.http.get<any>(this.env.API_URL + "utente/" + idUtente);
  }

  getRoleList() {
    return this.http.get<any>(this.env.API_URL + "utente/ruoli");
  }

  getStruttureList(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "utente/strutture?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getRisorseList(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "utente/risorse?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  // HttpClient API post() method => Utente create
  setUtente(idEnte: number | undefined, anno: number | undefined, data: any) {
    let a = { ...data, idEnte: idEnte, anno: anno };
    return this.http.post<any>(this.env.API_URL + "utente", a);
  }

  // HttpClient API post() method => Utente activate
  setActivate(idUtente: number) {
    return this.http.post<any>(
      this.env.API_URL + "utente/activate/" + encodeURIComponent(idUtente),
      null
    );
  }

  // HttpClient API post() method => Utente deactivate
  setDeactivate(idUtente: number) {
    return this.http.post<any>(
      this.env.API_URL + "utente/deactivate/" + encodeURIComponent(idUtente),
      null
    );
  }

  // HttpClient API put() method => Utente update
  updateUtente(
    idUtente: number,
    idEnte: number | undefined,
    anno: number | undefined,
    data: any
  ) {
    let a = { ...data, idEnte: idEnte, anno: anno };
    return this.http.put<any>(
      this.env.API_URL + "utente/" + encodeURIComponent(idUtente),
      a
    );
  }

  updatePassword(idUtente: number, data: any) {
    return this.http.put<any>(this.env.API_URL + "auth/modificaPassword", {
      idUtente,
      ...data,
    });
  }

  cambiaPassword(userid: string, data: any) {
    return this.http.put<any>(this.env.API_URL + "auth/cambiaPassword", {
      userid,
      ...data,
    });
  }

  // HttpClient API delete() method => Utente delete
  deleteUtente(idUtente: number) {
    return this.http.delete<any>(
      this.env.API_URL + "utente/" + encodeURIComponent(idUtente)
    );
  }

  setFiltroUtenti(filtro: any) {
    localStorage.setItem("filtroUtenti", JSON.stringify(filtro));
  }
  getFiltroUtenti(): any {
    const filtro = localStorage.getItem("filtroUtenti");
    if (filtro) {
      return JSON.parse(filtro);
    }
    return null;
  }
  removeFiltroUtenti() {
    localStorage.removeItem("filtroUtenti");
  }
}
