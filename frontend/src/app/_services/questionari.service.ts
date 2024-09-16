import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";
import { environment } from "../../environments/environment";
import { Risorsa } from "../_models";

export enum TipoNodoQuestionario {
  root = "root",
  questionario = "questionario",
  ambito = "ambito",
  valore = "valore",
}

@Injectable({
  providedIn: "root",
})
export class QuestionariService {
  constructor(private http: HttpClient) {}

  env = environment;

  getNodo(idEnte: number, tipo: TipoNodoQuestionario, idNodo: number) {
    return this.http.get<any>(
      this.env.API_URL + `questionario/nodo/${tipo}/${idNodo}?idEnte=${idEnte}`
    );
  }

  root(idEnte: number, anno: number, filter: any | undefined) {
    //console.log("----root anno:",anno,", filter:", filter);
    return this.http.get<any>(
      this.env.API_URL +
        `questionario/root?idEnte=${idEnte}&anno=${anno}` +
        (filter && filter.cerca ? "&filter=" + filter.cerca : "")
    );
  }

  preparaNodo(
    idEnte: number,
    tipoPadre: TipoNodoQuestionario,
    idPadre: number | undefined
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        `questionario/nodo/prepara?idEnte=${idEnte}&tipoPadre=${tipoPadre}&idNodoPadre=${idPadre}`
    );
  }

  getChildren(
    idEnte: number,
    anno: number,
    tipo: TipoNodoQuestionario,
    idNodo: number
  ) {
    return this.http.get<any>(
      this.env.API_URL +
        `questionario/children/${tipo}/${idNodo}?idEnte=${idEnte}&anno=${anno}`
    );
  }

  createNodo(
    idEnte: number,
    anno: number,
    tipo: TipoNodoQuestionario,
    tipoPadre: TipoNodoQuestionario | undefined,
    idPadre: number | undefined,
    data: any
  ) {
    const dataCompleta = {
      idEnte: idEnte,
      tipo: tipo,
      tipoPadre: tipoPadre,
      idPadre: idPadre,
      ...data,
    };

    return this.http.post<any>(
      this.env.API_URL + "questionario/nodo",
      dataCompleta
    );
  }

  updateNodo(tipo: TipoNodoQuestionario, idNodo: number, data: any) {
    return this.http.put<any>(
      this.env.API_URL + `questionario/nodo/${tipo}/${idNodo}`,
      data
    );
  }

  deleteNodo(tipo: TipoNodoQuestionario, idNodo: number) {
    return this.http.delete<any>(
      this.env.API_URL + `questionario/nodo/${tipo}/${idNodo}`
    );
  }
  updatePesoNodo(tipo: TipoNodoQuestionario, idNodo: number, data: any) {
    return this.http.put<any>(
      this.env.API_URL + `questionario/nodo/${tipo}/${idNodo}/peso`,
      data
    );
  }
  getCategorie(idEnte: number) {
    return this.http.get<any>(
      this.env.API_URL + `questionario/categorie?idEnte=${idEnte}`
    );
  }

  getIncarichi(idEnte: number) {
    return this.http.get<any>(
      this.env.API_URL + `questionario/incarichi?idEnte=${idEnte}`
    );
  }
  getValutazione(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        `scheda-valutato/questionario?idRegistrazione=${idRegistrazione}`
    );
  }
  getRisultatoValutazione(idRegistrazione: number) {
    return this.http.get<any>(
      this.env.API_URL +
        `scheda-valutato/risultatoQuestionario?idRegistrazione=${idRegistrazione}`
    );
  }
  toTipoNodo(tipo: number | undefined): TipoNodoQuestionario {
    if (!tipo) return TipoNodoQuestionario.root;
    switch (tipo) {
      case 0:
        return TipoNodoQuestionario.root;
      case 1:
        return TipoNodoQuestionario.questionario;
      case 2:
        return TipoNodoQuestionario.ambito;
      case 3:
        return TipoNodoQuestionario.valore;
      default:
        return TipoNodoQuestionario.root;
    }
  }
}
