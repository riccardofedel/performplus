import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class StampeService {
  constructor(private http: HttpClient) {}

  env = environment;

  // HttpClient API get() method => Cruscotto read
  stampa(
    anno: number | undefined,
    idEnte: number | undefined,
    data: any | undefined
  ) {
    const e = idEnte ? idEnte : 0;
    const a = anno ? anno : 0;
    return this.http.post<any>(this.env.API_URL + "report/print", {
      anno: a,
      idEnte: e,
      ...data,
    });
  }
  getAree(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "report/aree?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }
  getDirezioni(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "report/direzioni?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getReport(idEnte: number, anno: number, nomeReport: string) {
    return this.http.get<any>(
      this.env.API_URL +
        "cube/getReport?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        "&nomeReport=" +
        nomeReport
    );
  }
  deleteReport(idEnte: number, anno: number, nomeReport: string) {
    return this.http.delete<any>(
      this.env.API_URL +
        "cube/deleteReport?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        "&nomeReport=" +
        nomeReport
    );
  }
  getReports(idEnte: number, anno: number, tipoReport: string) {
    return this.http.get<any>(
      this.env.API_URL +
        "cube/reports?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        "&tipoReport=" +
        tipoReport
    );
  }
  stampaScheda(
    idRegistrazione: number 
  ) {
    return this.http.get<any>(this.env.API_URL + "report/stampaScheda?idRegistrazione="+idRegistrazione, );
  }
}
