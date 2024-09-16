import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Regolamento } from '../_models';

@Injectable({
  providedIn: "root",
})
export class RegolamentoService {
  env = environment;

  constructor(private http: HttpClient) {}

  // HttpClient API get() method => Search
  /*search(idEnte: number | undefined, anno: number | undefined, data: any | undefined) {
    return this.http.post<any>(this.env.API_URL + 'registrazione/search',{
      idEnte, anno, ...data
    });
  }*/
  search(idEnte: number, anno: number) {
    return this.http.get<any>(
      this.env.API_URL + "regolamento/list?idEnte=" + idEnte + "&anno=" + anno
    );
  }

  getCategorieList(idEnte: number) {
    return this.http.get<any>(
      this.env.API_URL + "regolamento/categorie?idEnte=" + idEnte
    );
  }

  getIncarichiList(idEnte: number) {
    return this.http.get<any>(
      this.env.API_URL + "regolamento/incarichi?idEnte=" + idEnte
    );
  }

  getRegolamento(idRegolamento: number) {
    return this.http.get<any>(
      this.env.API_URL + "regolamento/" + idRegolamento
    );
  }

  // HttpClient API delete() method => Risorsa delete
  deleteRegolamento(idRegolamento: number) {
    return this.http.delete<any>(
      this.env.API_URL + "regolamento/" + encodeURIComponent(idRegolamento)
    );
  }

  updateRegolamento(idRegolamento: number, data: any) {
    const dataCompleta = { idRegolamento, ...data };
    return this.http.put<any>(
      this.env.API_URL + "regolamento/" + encodeURIComponent(idRegolamento),
      dataCompleta
    );
  }

  createRegolamento(idEnte: number, anno: number, data: any) {
    const dataCompleta = { idEnte: idEnte, anno: anno, ...data };
    return this.http.post<any>(this.env.API_URL + "regolamento", dataCompleta);
  }

 
}
