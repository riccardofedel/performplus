import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CruscottoService {

  constructor(private http: HttpClient) { }

  env = environment;

  // HttpClient API get() method => Cruscotto read
  getCruscotto( anno: number | undefined, idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'cruscotto/' +((idEnte)?idEnte:0)+"/"+((anno)?anno:0));
  }

  getAnniList( idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'cruscotto/'+((idEnte)?idEnte:0)+"/anni");
  }

  // HttpClient API post() method => Cruscotto create
  setCruscotto( anno: number | undefined, idEnte: number | undefined, data: any) {
    return this.http.post<any>(this.env.API_URL + 'cruscotto/' +((idEnte)?idEnte:0)+'/' +((anno)?anno:0), data);
  }

  // HttpClient API put() method => Cruscotto create
  updateCruscotto(anno: number | undefined, idEnte: number | undefined,data: any) {
    return this.http.put<any>(this.env.API_URL + 'cruscotto/' +((idEnte)?idEnte:0)+'/' +((anno)?anno:0), data);
  }

}
