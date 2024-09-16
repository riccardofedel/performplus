import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class ValutazioneService {
  env = environment;

  constructor(private http: HttpClient) {}

  getValutatori(
    idEnte: number,
    anno: number,
    cognome: string | undefined,
    idValutato: number | undefined
  ) {
    //console.log("-----GET VALUTATORI ----");
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/valutatori?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "") +
        (this.vuoto(idValutato) ? "" : "&idValutato=" + idValutato)
    );
  }
  getValutatoriAll(idValutato: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/valutatoriAll?idValutato=" +
        idValutato
    );
  }
  getValutati(
    idEnte: number,
    anno: number,
    cognome: string | undefined,
    idValutatore: number | undefined
  ) {
    //console.log("----GET VALutati----");
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/valutati?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (cognome ? "&cognome=" + cognome : "") +
        (this.vuoto(idValutatore) ? "" : "&idValutatore=" + idValutatore)
    );
  }
  getRisorsa(idRisorsa: number) {
    return this.http.get<any>(
      this.env.API_URL + "scheda-valutato/risorsa?idRisorsa=" + idRisorsa
    );
  }
  registrazioni(idValutatore: number, idValutato: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/elenco?idValutatore=" +
        idValutatore +
        "&idValutato=" +
        idValutato
    );
  }

  registrazioniAll(idValutatore: number | undefined, idValutato: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/elencoAll?idValutato=" +
        idValutato +
        (this.vuoto(idValutatore) ? "" : "&idValutatore=" + idValutatore)
    );
  }
  salvaComportamenti(data: any) {
    //console.log("-----salvaComportamenti:", data);
    return this.http.post<any>(
      this.env.API_URL + "scheda-valutato/aggiornaQuestionario",
      data
    );
  }
  getObiettivi(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/obiettivi?idRegistrazione=" +
        idRegistrazione
    );
  }
  totali(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/totali?idRegistrazione=" +
        idRegistrazione
    );
  }
  statoScheda(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/statoScheda?idRegistrazione=" +
        idRegistrazione
    );
  }
  leggiScheda(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/leggi?idRegistrazione=" +
        idRegistrazione
    );
  }
  pubblicazioneScheda(data: any) {
    //console.log("-----pubblicaScheda:", data);
    return this.http.put<any>(
      this.env.API_URL + "scheda-valutato/pubblicazioneScheda",
      data
    );
  }
  pubblicazioneValutazione(data: any) {
    //console.log("-----pubblicaValutazione:", data);
    return this.http.put<any>(
      this.env.API_URL + "scheda-valutato/pubblicazioneValutazione",
      data
    );
  }
  accettazioneScheda(data: any) {
    //console.log("-----accettaScheda:", data);
    return this.http.put<any>(
      this.env.API_URL + "scheda-valutato/accettaScheda",
      data
    );
  }
  accettazioneValutazione(data: any) {
    //console.log("-----accettaValutazione:", data);
    return this.http.put<any>(
      this.env.API_URL + "scheda-valutato/accettaValutazione",
      data
    );
  }
  schedaValutato(idValutato: number) {
    return this.http.get<any>(
      this.env.API_URL +
        "scheda-valutato/schedaValutato?idValutato=" +
        idValutato
    );
  }
  vuoto(d: number | undefined): boolean {
    return d === null || d === undefined;
  }
  performanceOrganizzativa(
    idScheda: number | undefined,
    idRegistrazione: number,
    performance: boolean
  ) {
    const data = {
      idScheda: idScheda,
      idRegistrazione: idRegistrazione,
      attiva: performance,
    };
    return this.http.put<any>(
      this.env.API_URL + "scheda-valutato/performanceOrganizzativa",
      data
    );
  }
}
