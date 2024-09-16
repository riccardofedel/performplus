import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Risorsa } from '../_models';
import { ConsuntivazioneService } from '.';

export enum TipoNodo
{
    Piano = "PIANO",
    Area = "AREA",
    Obiettivo = "OBIETTIVO",
    Azione = "AZIONE",
    Fase = "FASE"
}

@Injectable({
  providedIn: "root",
})
export class ProgrammazioneService {
  constructor(
    private http: HttpClient,
    private consuntivazioneService: ConsuntivazioneService
  ) {}

  env = environment;

  getIntroduzioneElementi() {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/introduzione/elementi"
    );
  }

  getIntroduzione(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/introduzione/" +
        (idEnte ? idEnte : 0) +
        "/" +
        (anno ? anno : 0)
    );
  }
  getIntroduzioneContenuto(
    idEnte: number | undefined,
    anno: number | undefined,
    gruppo: string,
    nome: string
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/introduzione/" +
        (idEnte ? idEnte : 0) +
        "/" +
        (anno ? anno : 0) +
        "/contenuto?gruppo=" +
        gruppo +
        "&nome=" +
        nome
    );
  }

  getProgrammazione(id: number | undefined) {
    return this.http.get<any>(this.env.API_URL + "programmazione/" + id);
  }

  // HttpClient API put() method => Risorsa update
  updateIntroduzione(id: number | undefined, data: any | undefined) {
    return this.http.put<any>(
      this.env.API_URL + "programmazione/introduzione/" + id,
      data
    );
  }

  root(
    idEnte: number | undefined,
    anno: number | undefined,
    filter: string | undefined,
    isConsuntivazione = false
  ) {
    if (isConsuntivazione) {
      return this.consuntivazioneService.root(idEnte, anno, filter);
    } else {
      return this.http.get<any>(
        this.env.API_URL +
          "programmazione/root?idEnte=" +
          (idEnte ? idEnte : 0) +
          "&anno=" +
          (anno ? anno : 0) +
          (filter ? "&filter=" + filter : "")
      );
    }
  }

  search(
    idEnte: number | undefined,
    anno: number | undefined,
    data: any | undefined,
    isConsuntivazione = false
  ) {
    //console.log("-------SEARCH:", data);
    if (isConsuntivazione) {
      return this.consuntivazioneService.search(idEnte, anno, data);
    } else {
      return this.http.post<any>(this.env.API_URL + "programmazione/search", {
        idEnte,
        anno,
        ...data,
      });
    }
  }

  preparaProgrammazione(idPadre: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/preparaNuovoNodo?idPadre=" + idPadre
    );
  }
  preparaModificaProgrammazione(idNodo: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/preparaModificaNodo?idNodo=" + idNodo
    );
  }
  setProgrammazione(data: any) {
    return this.http.post<any>(this.env.API_URL + "programmazione", data);
  }

  updateProgrammazione(idProgrammazione: number, data: any) {
    return this.http.put<any>(
      this.env.API_URL + "programmazione/" + idProgrammazione,
      { id: idProgrammazione, ...data }
    );
  }

  rimuoviProgrammazione(idProgrammazione: number | undefined) {
    return this.http.delete<any>(
      this.env.API_URL + "programmazione/" + idProgrammazione
    );
  }

  getDirezioni(
    idEnte: number | undefined,
    anno: number | undefined,
    testo: string | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/direzioni?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        (testo ? "&testo=" + testo : "")
    );
  }

  getOrganizzazioni(idEnte: number, anno: number, testo: string | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/organizzazioni?idEnte=" +
        idEnte +
        "&anno=" +
        anno +
        (testo ? "&testo=" + testo : "")
    );
  }

  getModalitaAttuative() {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/modalitaAttuative"
    );
  }

  getTipiTiming(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/timing?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getTipi() {
    return this.http.get<any>(this.env.API_URL + "programmazione/tipi");
  }

  getTipologe() {
    return this.http.get<any>(this.env.API_URL + "programmazione/tipologie");
  }
  getTipiObiettivoGestionale(
    idEnte: number | undefined,
    anno: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/tipiObiettivoGestionale?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getTipologieObiettivoGestionale(
    idEnte: number | undefined,
    anno: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/tipologiaObiettivoGestionale?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getTipologieObiettiviOperativi(
    idEnte: number | undefined,
    anno: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/tipologieObiettiviOperativi?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getTipologiePesatura(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/pesatura/tipologia?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getLivelliStrategicita(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/pesatura/livelliStrategicita?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getLivelliComplessita(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/pesatura/livelliComplessita?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getAmministratori(idEnte: number | undefined, anno: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/amministratori?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0)
    );
  }

  getChildren(idProgrammazione: number, isConsuntivazione = false) {
    if (isConsuntivazione) {
      return this.consuntivazioneService.getChildren(idProgrammazione);
    } else {
      return this.http.get<any>(
        this.env.API_URL + "programmazione/children/" + idProgrammazione
      );
    }
  }

  getIndicatoriAssociati(idNodoPiano: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/indicatore"
    );
  }

  getObiettiviPesatura(idNodoPiano: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/pesatura/ramo"
    );
  }

  getPesatura(idNodoPiano: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/pesatura"
    );
  }

  setPesatura(idNodoPiano: number | undefined, data: any | undefined) {
    return this.http.post<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/pesatura",
      data
    );
  }

  calcolaPesatura(idNodoPiano: number | undefined, data: any | undefined) {
    return this.http.post<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/pesatura/calcolo",
      data
    );
  }

  updateNoteAssessori(idNodoPiano: number | undefined, noteAssessori: string) {
    return this.http.put<any>(
      this.env.API_URL + "programmazione/noteAssessori",
      { idNodoPiano: idNodoPiano, noteAssessori: noteAssessori }
    );
  }

  setIndicatorePiano(idNodoPiano: number | undefined, data: any | undefined) {
    return this.http.post<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/indicatore",
      data
    );
  }

  updateIndicatorePiano(
    idNodoPiano: number | undefined,
    id: number | undefined,
    data: any | undefined
  ) {
    return this.http.put<any>(
      this.env.API_URL + "programmazione/" + idNodoPiano + "/indicatore/" + id,
      data
    );
  }

  updateIndicatoreTarget(
    idNodoPiano: number | undefined,
    id: number | undefined,
    data: any | undefined
  ) {
    return this.http.post<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        id +
        "/target",
      data
    );
  }

  updateIndicatoreRange(
    idNodoPiano: number | undefined,
    id: number | undefined,
    data: any | undefined
  ) {
    return this.http.post<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        id +
        "/range",
      { ranges: data?.range }
    );
  }

  getIndicatorePiano(
    idNodoPiano: number | undefined,
    idIndicatore: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        idIndicatore
    );
  }

  getIndicatoreTarget(
    idNodoPiano: number | undefined,
    idIndicatore: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        idIndicatore +
        "/target"
    );
  }

  getIndicatoreRange(
    idNodoPiano: number | undefined,
    idIndicatore: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        idIndicatore +
        "/range"
    );
  }

  rimuoviIndicatorePiano(
    idNodoPiano: number | undefined,
    idIndicatore: number | undefined
  ) {
    return this.http.delete<any>(
      this.env.API_URL +
        "programmazione/" +
        idNodoPiano +
        "/indicatore/" +
        idIndicatore
    );
  }

  getTemplates(idEnte: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/indicatore/templates?idEnte=" +
        (idEnte ? idEnte : 0)
    );
  }

  getRisorse(
    idEnte: number | undefined,
    anno: number | undefined,
    cognome: string | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/risorse?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        "&cognome=" +
        cognome
    );
  }

  getFigli(idNodo: number) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/figli?idNodo=" + idNodo
    );
  }
  getStatiNodo() {
    return this.http.get<any>(this.env.API_URL + "programmazione/statiNodo");
  }
  getStatiProposta() {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/statiProposta"
    );
  }
  getResponsabili(
    idEnte: number | undefined,
    anno: number | undefined,
    cognome: string | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/responsabili?idEnte=" +
        (idEnte ? idEnte : 0) +
        "&anno=" +
        (anno ? anno : 0) +
        "&cognome=" +
        cognome
    );
  }
  getRisorseAssociate(idNodoPiano: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/getRisorseAssociate?idNodoPiano=" +
        idNodoPiano
    );
  }

  getRisorseAssociabili(idNodoPiano: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/getRisorseAssociabili?idNodoPiano=" +
        idNodoPiano +
        "&soloStruttura=true"
    );
  }
  associaRisorse(data: any) {
    return this.http.post<any>(
      this.env.API_URL + "programmazione/associaRisorse",
      data
    );
  }
  rimuoviRisorsa(id: number | undefined) {
    return this.http.delete<any>(
      this.env.API_URL + "programmazione/rimuoviRisorsa/" + id
    );
  }
  getRisorseProgrammazione(idNodo: number | undefined) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/" + idNodo + "/risorse"
    );
  }
  getResponsabiliAzione(idOrganizzazione: any | undefined) {
    return this.http.get<any>(
      this.env.API_URL +
        "programmazione/getResponsabiliAzione?idOrganizzazione=" +
        idOrganizzazione
    );
  }

  spostaNodo(data:any) {
    return this.http.put<any>(
      this.env.API_URL + "programmazione/spostaNodo",
      data
    );
  }

  getObiettivi(id: number) {
    return this.http.get<any>(
      this.env.API_URL + "programmazione/listSposta?id=" + id
    );
  }
}


