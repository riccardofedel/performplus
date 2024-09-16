import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";
import { environment } from "../../environments/environment";
import { Risorsa } from "../_models";

@Injectable({
  providedIn: "root",
})
export class RisorseService {
  constructor(private http: HttpClient) {}

  env = environment;

  // HttpClient API get() method => Search
  search(
    idEnte: number,
    anno: number,
    pageNum: number,
    pageSize: number,
    interno: boolean | undefined,
    soloAttiveAnno: boolean | true,
    testo: any | undefined,
    sort: any | null
  ) {
    //console.log(">>>>interno", interno);
    return this.http.get<any>(
      this.env.API_URL +
        "risorsa_umana/search?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        "&soloAttiveAnno=" +
        soloAttiveAnno +
        "&page=" +
        pageNum +
        "&size=" +
        pageSize +
        (interno == undefined ? "" : "&interno=" + interno) +
        (testo ? "&cognome=" + encodeURI(testo) : "") +
        (sort && sort!== "" ? "&sort=" + encodeURI(sort) : "")
    );
  }

  // HttpClient API get() method => Risorsa read
  getRisorsa(idRisorsa: number) {
    return this.http.get<any>(this.env.API_URL + "risorsa_umana/" + idRisorsa);
  }

  getCategorieList(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "risorsa_umana/categorie?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }

  getProfiliList(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "risorsa_umana/profili?idEnte=" + (idEnte ? idEnte : 0)
    );
  }
  getIncarichiList(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "risorsa_umana/incarichi?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }
  getContrattiList(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "risorsa_umana/contratti?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }

  // HttpClient API post() method => Risorsa create
  setRisorsa(idEnte: number | undefined, anno: number | undefined, data: any) {
    const dataCompleta = { idEnte, anno, ...data };
    return this.http.post<any>(
      this.env.API_URL + "risorsa_umana",
      dataCompleta
    );
  }

  // HttpClient API put() method => Risorsa update
  updateRisorsa(
    idEnte: number | undefined,
    anno: number | undefined,
    idRisorsa: number,
    data: any
  ) {
    const dataCompleta = { idEnte, anno, ...data };
    return this.http.put<any>(
      this.env.API_URL + "risorsa_umana/" + encodeURIComponent(idRisorsa),
      dataCompleta
    );
  }
  // HttpClient API delete() method => Risorsa delete
  deleteRisorsa(idRisorsa: number) {
    return this.http.delete<any>(
      this.env.API_URL + "risorsa_umana/" + encodeURIComponent(idRisorsa)
    );
  }

  setFiltroRisorse(filtro: any) {
    localStorage.setItem("filtroRisorse", JSON.stringify(filtro));
  }
  getFiltroRisorse(): any {
    const filtro = localStorage.getItem("filtroRisorse");
    if (filtro) {
      return JSON.parse(filtro);
    }
    return null;
  }
  removeFiltroRisorse() {
    localStorage.removeItem("filtroRisorse");
  }
}
