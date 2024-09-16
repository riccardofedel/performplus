import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: "root",
})
export class ConsuntivazioneService {
  constructor(private http: HttpClient) {}

  env = environment;

  getValutazione(
    idIndicatore: number | undefined,
    idValutazione: number | undefined
  ) {
    const valutazionePar = idValutazione
      ? "&idValutazione=" + idValutazione
      : "";
    return this.http.get<any>(
      this.env.API_URL +
        "consuntivazione/valutazione/" +
        idIndicatore +
        "?idIndicatore=" +
        idIndicatore +
        valutazionePar
    );
  }

  root(
    idEnte: number | undefined,
    anno: number | undefined,
    filter: string | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "consuntivazione/root?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        (filter ? "&filter=" + filter : "")
    );
  }

  getChildren(idProgrammazione: number) {
    return this.http.get<any>(
      this.env.API_URL + "consuntivazione/children/" + idProgrammazione
    );
  }

  getForzatura(idIndicatore: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "consuntivazione/forzatura/" + idIndicatore
    );
  }

  getAllegati(idIndicatore: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "consuntivazione/allegati/" + idIndicatore
    );
  }

  setForzatura(idIndicatore: number | undefined, data: any | undefined) {
    return this.http.put<any>(
      this.env.API_URL + "consuntivazione/forzatura/" + idIndicatore,
      data
    );
  }

  salvaAllegato(idIndicatore: number | undefined, data: any | undefined) {
    return this.http.post<any>(
      this.env.API_URL + "consuntivazione/allegato/" + idIndicatore,
      data
    );
  }

  rimuoviAllegato(idAllegato: number | undefined) {
    return this.http.delete<any>(
      this.env.API_URL + "consuntivazione/allegato/" + idAllegato
    );
  }

  getAllegato(idAllegato: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "consuntivazione/allegato/" + idAllegato
    );
  }

  setRichiestaForzatura(
    idIndicatore: number | undefined,
    data: any | undefined
  ) {
    return this.http.put<any>(
      this.env.API_URL + "consuntivazione/richiestaForzatura/" + idIndicatore,
      data
    );
  }

  getConsuntivazione(id: number | undefined) {
    return this.http.get<any>(this.env.API_URL + "consuntivazione/" + id);
  }

  setConsuntivazione(id: number | undefined, data: any | undefined) {
    return this.http.put<any>(this.env.API_URL + "consuntivazione/" + id, data);
  }

  // HttpClient API put() method => Risorsa update
  setConsuntiva(idIndicatore: number | undefined, data: any | undefined) {
    return this.http.put<any>(
      this.env.API_URL + "consuntivazione/consuntiva/" + idIndicatore,
      data
    );
  }

  search(
    idEnte: number | undefined,
    anno: number | undefined,
    data: any | undefined
  ) {
    return this.http.post<any>(this.env.API_URL + "consuntivazione/search", {
      idEnte,
      anno,
      ...data,
    });
  }

  setStatoAvanzamento(
    idNodoPiano: number | undefined,
    periodo: number | undefined,
    statoAvanzamento: string | undefined
  ) {
    return this.http.put<any>(
      this.env.API_URL + "consuntivazione/statoAvanzamento",
      {
        periodo: periodo,
        idNodoPiano: idNodoPiano,
        statoAvanzamento: statoAvanzamento,
      }
    );
  }
  getForzatureNodo(idNodoPiano: number) : any{
    return this.http.get<any>(
      this.env.API_URL + "consuntivazione/forzatureNodo/" + idNodoPiano
    );
  }
  richiestaForzatureNodo(idNodoPiano: number | undefined,forzaturaRisorse: number | undefined, forzaturaResponsabili: number | undefined) {
    const payload={idNodoPiano: idNodoPiano , forzaturaRisorse: forzaturaRisorse, forzaturaResponsabili: forzaturaResponsabili};
    return this.http.put<any>(
      this.env.API_URL + "consuntivazione/forzatureNodo",
      payload
    );
    
  }
}


