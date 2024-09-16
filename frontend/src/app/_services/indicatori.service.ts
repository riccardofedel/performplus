import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IndicatoriService {

  env = environment;

  constructor(private http: HttpClient) { }


  // HttpClient API get() method => Search
  search(idEnte: number | undefined, pageNum: number, pageSize: number, filter: any, sort: any) {
    return this.http.get<any>(this.env.API_URL + 'indicatore/search?idEnte='+((idEnte)?idEnte:0)+'&page=' + pageNum + '&size=' + pageSize + (filter ? '&' + ('denominazione='+encodeURI(filter)) : '') + ((sort !== '' && sort !== undefined) ? '&sort=' + encodeURI(sort) : ''));
  }

  // HttpClient API get() method => Indicatore read
  getIndicatore(idIndicatore: number) {
    return this.http.get<any>(this.env.API_URL + 'indicatore/' + idIndicatore);
  }

  getCalcoliConsuntivazione(idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'indicatore/calcoliConsuntivazione?idEnte=' +((idEnte)?idEnte:0));
  }

  getRaggruppamentiList(idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'indicatore/raggruppamenti?idEnte=' +((idEnte)?idEnte:0));
  }

  getFormuleList(idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'indicatore/formule?idEnte=' +((idEnte)?idEnte:0));
  }

  // HttpClient API post() method => Indicatore create
  setIndicatore(idEnte: number | undefined, data: any) {
    const dataCompleta = {idEnte, ...data};
    return this.http.post<any>(this.env.API_URL + 'indicatore', data);
  }


  // HttpClient API put() method => Indicatore update
  updateIndicatore(idEnte: number | undefined, idIndicatore: number, data: any) {
    const dataCompleta = {idEnte, ...data};

    return this.http.put<any>(this.env.API_URL + 'indicatore/' + encodeURIComponent(idIndicatore), data);
  }
  // HttpClient API delete() method => Indicatore delete
  deleteIndicatore(idIndicatore: number) {
    return this.http.delete<any>(this.env.API_URL + 'indicatore/' + encodeURIComponent(idIndicatore));
  }
}
