import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Risorsa } from '../_models';


@Injectable({
  providedIn: 'root'
})

export class AzioneService {

  constructor(private http: HttpClient
  ) { }

  env = environment;



  search(idEnte: number | undefined, anno: number | undefined, data: any | undefined) {

    return this.http.post<any>(this.env.API_URL + 'programmazione/azione/search', {
      idEnte, anno, ...data
    });

  }
  proponi(data: any | undefined) {
    //console.log("proponi service", data);
    return this.http.put<any>(this.env.API_URL + 'programmazione/azione/proponi', {
      ...data
    });

  }
  valida(data: any | undefined) {

    return this.http.put<any>(this.env.API_URL + 'programmazione/azione/valida', {
      ...data
    });

  }
  invalida(data: any | undefined) {

    return this.http.put<any>(this.env.API_URL + 'programmazione/azione/invalida', {
      ...data
    });

  }
  cambiaResponsabile(data: any | undefined) {

    return this.http.put<any>(this.env.API_URL + 'programmazione/azione/cambiaResponsabile', {
      ...data
    });

  }
  getResponsabili(idEnte: number | undefined, anno: number | undefined, cognome: string | undefined) {
    return this.http.get<any>(this.env.API_URL + 'programmazione/azione/responsabiliAzione?idEnte=' + ((idEnte) ? idEnte : 0) +
      '&anno=' + ((anno) ? anno : 0) + '&cognome=' + cognome);
  }

}


