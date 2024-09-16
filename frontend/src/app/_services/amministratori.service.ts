import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AmministratoriService {

  constructor(private http: HttpClient) { }

  env = environment;

  // HttpClient API get() method => Search
  search(idEnte: number | undefined, anno: number | undefined, pageNum: number, pageSize: number, filter: any, sort: any) {
    return this.http.get<any>(this.env.API_URL + 'amministratore/search?idEnte=' +((idEnte)?idEnte:0)+ '&anno='+((anno)?anno:0)+'&page=' + pageNum + '&size=' + pageSize + (filter ? '&cognome=' + encodeURI(filter) : '' ) + ((sort !== '' && sort !== undefined) ? '&sort=' + encodeURI(sort) : '' ) );
  }

  // HttpClient API get() method => Amministratore read
  getAmministratore(idAmministratore: number) {
    return this.http.get<any>(this.env.API_URL + 'amministratore/' + idAmministratore);
  }

  getFunzioniList(idEnte: number | undefined) {
    return this.http.get<any>(this.env.API_URL + 'amministratore/funzioni?idEnte='+((idEnte)?idEnte:0));
  }

  // HttpClient API post() method => Amministratore create
  setAmministratore(idEnte: number | undefined, anno: number | undefined, data: any) {
    const dataCompleta = {idEnte, anno, ...data};
     return this.http.post<any>(this.env.API_URL + 'amministratore', dataCompleta);
  }


  // HttpClient API put() method => Amministratore update
  updateAmministratore(idEnte: number | undefined, anno: number | undefined, idAmministratore: number ,data: any) {
    const dataCompleta = {idEnte, anno, ...data};
    return this.http.put<any>(this.env.API_URL + 'amministratore/' + encodeURIComponent(idAmministratore), dataCompleta);
  }

  // HttpClient API delete() method => Amministratore delete
  deleteAmministratore(idAmministratore: number) {
    return this.http.delete<any>(this.env.API_URL + 'amministratore/' + encodeURIComponent(idAmministratore));
  }

}
