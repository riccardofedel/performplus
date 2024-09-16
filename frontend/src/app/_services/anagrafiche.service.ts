import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnagraficheService {

  constructor(private http: HttpClient) { }

  env = environment;

  // HttpClient API get() method => Search
  search(page: string | undefined, idEnte: number | undefined, anno: number | undefined, pageNum: number, pageSize: number, filter: string, sort: string) {
    return this.http.get<any>(this.env.API_URL + 'sistema/'+page+'/search?idEnte=' +((idEnte)?idEnte:0)+ '&anno='+((anno)?anno:0)+'&page=' + pageNum + '&size=' + pageSize + (filter ? '&' + ('testo='+encodeURI(filter)) : '' ) + ((sort !== '' && sort !== undefined) ? '&sort=' + encodeURI(sort) : '' ) );
  }

  delete(page: string | undefined, id: number) {
    return this.http.delete<any>(this.env.API_URL + 'sistema/'+page+'/' + encodeURIComponent(id));
  }

  get(page: string | undefined, id: number) {
    return this.http.get<any>(this.env.API_URL + 'sistema/'+page+'/' + encodeURIComponent(id));
  }

  getAnagrafica(page: string | undefined, idEnte: number | undefined) {
    //console.log("getAnagrafica:",page);
      if(page==='processi' || page==='bilancio')
      return new Observable<any>();
      
    return this.http.get<any>(this.env.API_URL + 'programmazione/'+page);
  }

  update(page: string | undefined, id: number, data: any | undefined) {
    return this.http.put<any>(this.env.API_URL + 'sistema/'+page+'/' + encodeURIComponent(id), data);
  }

  create(page: string | undefined, data: any | undefined) {
    return this.http.post<any>(this.env.API_URL + 'sistema/'+page, data);
  }

}
