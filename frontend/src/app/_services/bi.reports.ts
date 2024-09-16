import { Injectable } from "@angular/core";

@Injectable({ providedIn: "any" })
export class TemplateReports {
  REPORT1(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/piao?anno=" + anno,
        mapping: {
          ANNO: { type: "string", caption: "Anno", visible: false },
          TIPO_NODO: { type: "string", caption: "Tipo", visible: true },
          CODICE: { type: "string", caption: "Codice", visible: true },
          DENOMINAZIONE: {
            type: "string",
            caption: "Denominazione",
            filters: false,
          },
          INIZIO: {
            type: "date string",
            caption: "Inizio",
            pattern: "dd/MM/yyyy",
          },
          SCADENZA: {
            type: "date string",
            caption: "Scadenza",
            pattern: "dd/MM/yyyy",
          },
          DESCRIZIONE: {
            type: "string",
            caption: "Descrizione",
            visible: true,
            filters: false,
          },
          STRUTTURA_CODICE: {
            type: "string",
            caption: "Struttura codice",
            visible: true,
          },
          STRUTTURA_CODICE_INTERNO: {
            type: "string",
            caption: "Struttura codice interno",
            visible: true,
          },
          STRUTTURA_NOME: {
            type: "string",
            caption: "Struttura nome",
            visible: true,
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          TIPOLOGIE: {
            type: "string",
            caption: "Tipologia",
            visible: true,
          },
          TIPO_OBIETTIVO: {
            type: "string",
            caption: "Tipo obiettivo",
            visible: true,
          },
          MODALITA_ATTUATIVE: {
            type: "string",
            caption: "Modalità attuative",
            visible: true,
          },
          NOTE: {
            type: "string",
            caption: "Note",
            visible: true,
          },
          INNOVAZIONE: {
            type: "string",
            caption: "Innovazione",
            filters: true,
          },
          POLITICA: {
            type: "string",
            caption: "Politica",
            filters: true,
          },
          ANNUALITA: {
            type: "string",
            caption: "Annualità",
            filters: true,
          },
          CONTRIBUTORS: {
            type: "string",
            caption: "Contributors",
            filters: true,
          },
          DIMENSIONE: {
            type: "string",
            caption: "Dimensione",
            filters: true,
          },
          STAKEHOLDERS: {
            type: "string",
            caption: "Stakeholders",
            filters: true,
          },
          PROSPETTIVA: {
            type: "string",
            caption: "Prospettiva",
            filters: true,
          },
          BLOCCATO: {
            type: "string",
            caption: "Bloccato",
            filters: true,
          },
          FLAG_PNRR: {
            type: "string",
            caption: "PNRR",
            filters: true,
          },
          FOCUS_ACCESSIBILITA: {
            type: "string",
            caption: "Focus accessibilità",
            filters: true,
          },
          FOCUS_DIGITALIZZAZIONE: {
            type: "string",
            caption: "Focus digitalizzazione",
            filters: true,
          },
          FOCUS_PARI_OPPORTUNITA: {
            type: "string",
            caption: "Focus pari opportunità",
            filters: true,
          },
          FOCUS_RISPARMIO_ENERGETICO: {
            type: "string",
            caption: "Focus risparmio energetico",
            filters: true,
          },
          FOCUS_SEMPLIFICAZIONE: {
            type: "string",
            caption: "Focus semplificazione",
            filters: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }

  REPORT2(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/indicatori?anno=" + anno,
        mapping: {
          ANNO: { type: "string", caption: "Anno", visible: false },
          CODICE_INDICATORE: {
            type: "string",
            caption: "Codice indicatore",
            visible: false,
          },
          ANNO_TARGET: {
            type: "string",
            caption: "Anno target",
            visible: false,
          },
          DENOMINAZIONE_INDICATORE: {
            type: "string",
            caption: "Indicatore",
            visible: true,
          },
          TIPO_INDICATORE: {
            type: "string",
            caption: "Tipo indicatore",
            visible: true,
          },
          SCADENZA_INDICATORE: {
            type: "date string",
            caption: "Scadenza indicatore",
            pattern: "dd/MM/yyyy",
          },
          DESCRIZIONE: {
            type: "string",
            caption: "Descrizione",
            filters: false,
          },
          TIPO_FORMULA: {
            type: "string",
            caption: "Tipo formula",
            visible: true,
          },
          TARGET: {
            type: "string",
            caption: "Target",
            filters: false,
          },
          TARGET_NUMERO: {
            type: "number",
            caption: "Target numerico",
            filters: false,
          },
          STORICO: {
            type: "number",
            caption: "Storico",
            filters: false,
          },
          CODICE_INTERVENTO: {
            type: "string",
            caption: "Obiettivo codice",
            visible: true,
          },
          NOME_INTERVENTO: {
            type: "string",
            caption: "Obiettivo nome",
            visible: true,
          },
          CODICE_STRUTTURA: {
            type: "string",
            caption: "Struttura codice",
            visible: true,
          },
          NOME_STRUTTURA: {
            type: "string",
            caption: "Struttura nome",
            visible: true,
          },
          RESPONSABILE_INTERVENTO: {
            type: "string",
            caption: "Struttura responsabile ",
            visible: true,
          },
          DECRESCENTE: {
            type: "string",
            caption: "Decrescente",
            visible: true,
          },
          PERCENTUALE: {
            type: "string",
            caption: "Percentuale",
            visible: true,
          },
          NUMERATORE: {
            type: "string",
            caption: "Numeratore nome",
            filters: true,
          },
          DENOMINATORE: {
            type: "string",
            caption: "Denominatore nome",
            filters: true,
          },
          TARGET_NUMERATORE: {
            type: "number",
            caption: "Target numeratore",
            filters: false,
          },
          STORICO_NUMERATORE: {
            type: "number",
            caption: "Storico numeratore",
            filters: false,
          },
          TARGET_DENOMINATORE: {
            type: "number",
            caption: "Target denominatore",
            filters: true,
          },
          STORICO_DENOMINATORE: {
            type: "number",
            caption: "Storico denominatore",
            filters: true,
          },
          DECIMALI: {
            type: "number",
            caption: "Decimali",
            filters: true,
          },
          DECIMALI_NUMERATORE: {
            type: "number",
            caption: "Decimali numeratore",
            filters: false,
          },
          DECIMALI_DENOMINATORE: {
            type: "number",
            caption: "Decimali denominatore",
            filters: false,
          },
          NOTE: {
            type: "string",
            caption: "Note",
            filters: false,
          },
          UNITA_MISURA: {
            type: "string",
            caption: "Unità di misura",
            filters: true,
          },
          DIMENSIONE: {
            type: "string",
            caption: "Dimensione",
            filters: true,
          },
          FONTE: {
            type: "string",
            caption: "Fonte",
            filters: true,
          },
          PROSPETTIVA: {
            type: "string",
            caption: "Prospettiva",
            filters: true,
          },
          BASE_LINE: {
            type: "string",
            caption: "Baseline",
            filters: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT3(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/organigramma?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          CODICE: {
            type: "string",
            caption: "Codice",
            visible: true,
          },
          CODICE_INTERNO: {
            type: "string",
            caption: "Codice interno",
            visible: true,
          },
          INTESTAZIONE: {
            type: "string",
            caption: "Intestazione",
            visible: true,
          },
          INIZIO_VALIDITA: {
            type: "date string",
            caption: "Inizio validità",
            pattern: "dd/MM/yyyy",
          },
          FINE_VALIDITA: {
            type: "date string",
            caption: "Fine validità",
            pattern: "dd/MM/yyyy",
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          INTERIM: {
            type: "number",
            caption: "Interim",
            visible: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT4(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/registrazioni?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          VALUTATORE: {
            type: "string",
            caption: "Valutatore",
            visible: true,
          },
          VALUTATORE_MATRICOLA: {
            type: "string",
            caption: "Matricola Valutatore",
            visible: true,
          },
          VALUTATO: {
            type: "string",
            caption: "Valutato",
            visible: true,
          },
          VALUTATO_MATRICOLA: {
            type: "string",
            caption: "Matricola Valutato",
            visible: true,
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
          CODICE_STRUTTURA: {
            type: "string",
            caption: "Struttura codice",
            visible: true,
          },
          NOME_STRUTTURA: {
            type: "string",
            caption: "Struttura nome",
            visible: true,
          },
          QUESTIONARIO: {
            type: "string",
            caption: "Questionario",
            visible: true,
          },
          REGOLAMENTO: {
            type: "string",
            caption: "Regolamento",
            visible: true,
          },
          INIZIO_VALIDITA: {
            type: "date string",
            caption: "Inizio validità",
            pattern: "dd/MM/yyyy",
          },
          FINE_VALIDITA: {
            type: "date string",
            caption: "Fine validità",
            pattern: "dd/MM/yyyy",
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          PO: {
            type: "string",
            caption: "PO",
            visible: true,
          },
          INATTIVA: {
            type: "string",
            caption: "Non disponibile",
            visible: true,
          },
          INTERIM: {
            type: "string",
            caption: "Interim",
            visible: true,
          },
          MANCATA_ASSEGNAZIONE: {
            type: "string",
            caption: "Mancata assegnazione",
            visible: true,
          },
          MANCATO_COLLOQUIO: {
            type: "string",
            caption: "Mancato colloquio",
            visible: true,
          },
          FORZA_SCHEDA_SEPARATA: {
            type: "string",
            caption: "Scheda separata",
            visible: true,
          },
          FORZA_VALUTATORE: {
            type: "string",
            caption: "Forza valutatore",
            visible: true,
          },
          DATA_ACC_SCHEDA: {
            type: "date string",
            caption: "Data presa visione scheda",
            pattern: "dd/MM/yyyy",
          },
          NOTE_ACC_SCHEDA: {
            type: "string",
            caption: "Note presa visione scheda",
            visible: true,
          },
          DATA_ACC_VALUTAZIONE: {
            type: "date string",
            caption: "Data presa visione valutazione",
            pattern: "dd/MM/yyyy",
          },
          NOTE_ACC_VALUTAZIONE: {
            type: "string",
            caption: "Note presa visione valutazione",
            visible: true,
          },
          DATA_PUBBL_SCHEDA: {
            type: "date string",
            caption: "Data pubblicazione scheda",
            pattern: "dd/MM/yyyy",
          },
          NOTE_PUBBL_SCHEDA: {
            type: "string",
            caption: "Note pubblicazione scheda",
            visible: true,
          },
          DATA_PUBBL_VALUTAZIONE: {
            type: "date string",
            caption: "Data pubblicazione valutazione",
            pattern: "dd/MM/yyyy",
          },
          NOTE_PUBBL_VALUTAZIONE: {
            type: "string",
            caption: "Note pubblicazione valutazione",
            visible: true,
          },
          DATA_APP_SCHEDA_OIV: {
            type: "date string",
            caption: "Data approvazione scheda OIV",
            pattern: "dd/MM/yyyy",
          },
          DATA_APP_VALUTAZIONE_OIV: {
            type: "date string",
            caption: "Data approvazione valutazione OIV",
            pattern: "dd/MM/yyyy",
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT5(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/pesature?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          VALUTATO: {
            type: "string",
            caption: "Valutato",
            visible: true,
          },
          MATRICOLA_VALUTATO: {
            type: "string",
            caption: "Matricola Valutato",
            visible: true,
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
          VALUTATORE: {
            type: "string",
            caption: "Valutatore",
            visible: true,
          },
          MATRICOLA_VALUTATORE: {
            type: "string",
            caption: "Matricola Valutatore",
            visible: true,
          },
          STRUTTURA: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          QUESTIONARIO: {
            type: "string",
            caption: "Questionario",
            visible: true,
          },
          REGOLAMENTO: {
            type: "string",
            caption: "Regolamento",
            visible: true,
          },
          INIZIO_VALIDITA: {
            type: "date string",
            caption: "Inizio validità",
            pattern: "dd/MM/yyyy",
          },
          FINE_VALIDITA: {
            type: "date string",
            caption: "Fine validità",
            pattern: "dd/MM/yyyy",
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          PO: {
            type: "string",
            caption: "PO",
            visible: true,
          },
          OBIETTIVO: {
            type: "string",
            caption: "Obiettivo",
            visible: true,
          },
          TIPO: {
            type: "string",
            caption: "Tipo",
            visible: false,
          },
          PESO_OBIETTIVO: {
            type: "number",
            caption: "Peso Obiettivo",
            visible: true,
          },
          INDICATORE: {
            type: "string",
            caption: "Indicatore",
            visible: true,
          },
          SCADENZA_INDICATORE: {
            type: "date string",
            caption: "Scadenza Indicatore",
            pattern: "dd/MM/yyyy",
          },
          PESO_INDICATORE: {
            type: "number",
            caption: "Peso Indicatore",
            visible: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT6(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/schede?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          VALUTATO: {
            type: "string",
            caption: "Valutato",
            visible: true,
          },
          MATRICOLA: {
            type: "string",
            caption: "Matricola Valutato",
            visible: true,
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
          PESO_OBIETTIVI_IND: {
            type: "number",
            caption: "Peso obiettivi individuali",
            visible: true,
          },
          PESO_OBIETTIVI_STRUT: {
            type: "number",
            caption: "Peso obiettivi struttura",
            visible: true,
          },
          PESO_QUEST: {
            type: "number",
            caption: "Peso questionario",
            visible: true,
          },
          PESO_PERFORMANCE_ORGANIZZATIVA: {
            type: "number",
            caption: "Peso perfromance org.",
            visible: true,
          },
          VALUTAZIONE: {
            type: "number",
            caption: "Valutazione",
            visible: true,
          },
          RAGG_OBIETTIVI_IND: {
            type: "number",
            caption: "Ragg. obiettivi individuali",
            visible: true,
          },
          RAGG_OBIETTIVI_STRUT: {
            type: "number",
            caption: "Ragg. obiettivi struttura",
            visible: true,
          },
          RAGG_QUEST: {
            type: "number",
            caption: "Ragg. questionario",
            visible: true,
          },
          RAGG_PERFORMANCE_ORGANIZZATIVA: {
            type: "number",
            caption: "Ragg. performance org.",
            visible: true,
          },
          QUESTIONARIO: {
            type: "string",
            caption: "Questionario",
            visible: true,
          },
          REGOLAMENTO: {
            type: "string",
            caption: "Regolamento",
            visible: true,
          },
          STRUTTURA: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          INIZIO_VALIDITA: {
            type: "date string",
            caption: "Inizio validità",
            pattern: "dd/MM/yyyy",
          },
          FINE_VALIDITA: {
            type: "date string",
            caption: "Fine validità",
            pattern: "dd/MM/yyyy",
          },
          VALUTATORE: {
            type: "string",
            caption: "Valutatore",
            visible: true,
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          PO: {
            type: "string",
            caption: "PO",
            visible: true,
          },
          INATTIVA: {
            type: "string",
            caption: "Non disponibile",
            visible: true,
          },
          INTERIM: {
            type: "string",
            caption: "Interim",
            visible: true,
          },
          MANCATA_ASSEGNAZIONE: {
            type: "string",
            caption: "Mancata assegnazione",
            visible: true,
          },
          MANCATO_COLLOQUIO: {
            type: "string",
            caption: "Mancato colloquio",
            visible: true,
          },
          FORZA_SCHEDA_SEPARATA: {
            type: "string",
            caption: "Scheda separata",
            visible: true,
          },
          FORZA_VALUTATORE: {
            type: "string",
            caption: "Forza valutatore",
            visible: true,
          },
          OBIETTIVO: {
            type: "string",
            caption: "Obiettivo",
            visible: true,
          },
          PESO_OBIETTIVO: {
            type: "number",
            caption: "Peso obiettivo",
            visible: true,
          },
          RAGGIUNGIMENTO_OBIETTIVO: {
            type: "number",
            caption: "Ragg. obiettivo",
            visible: true,
          },
          VALUTAZIONE_OBIETTIVO: {
            type: "number",
            caption: "Valutazione obiettivo",
            visible: true,
          },
          INDICATORE: {
            type: "string",
            caption: "Indicatore",
            visible: true,
          },
          SCADENZA_INDICATORE: {
            type: "date string",
            caption: "Scadenza indicatore",
            pattern: "dd/MM/yyyy",
          },
          PESO_INDICATORE: {
            type: "number",
            caption: "Peso indicatore",
            visible: true,
          },
          RAGGIUNGIMENTO_INDICATORE: {
            type: "number",
            caption: "Ragg. indicatore",
            visible: true,
          },
          VALUTAZIONE_INDICATORE: {
            type: "number",
            caption: "Valutazione indicatore",
            visible: true,
          },
          VALUTAZIONE_RELATIVA: {
            type: "number",
            caption: "Valutazione relativa indicatore",
            visible: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }

  REPORT7(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/risorse?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          CODICE_FISCALE: {
            type: "string",
            caption: "Codice fiscale",
            visible: false,
          },
          CODICE_INTERNO: {
            type: "string",
            caption: "Matricola",
            visible: true,
          },
          COGNOME: {
            type: "string",
            caption: "Cognome",
            visible: true,
          },
          NOME: {
            type: "string",
            caption: "Nome",
            visible: true,
          },
          EMAIL: {
            type: "string",
            caption: "Email",
            visible: true,
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
          CODICE_UO: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          DAL: {
            type: "date string",
            caption: "Inizio",
            pattern: "dd/MM/yyyy",
          },
          AL: {
            type: "date string",
            caption: "Fine",
            pattern: "dd/MM/yyyy",
          },
          FLAG_POSIZIONE_ORGANIZZATIVA: {
            type: "number",
            caption: "Flag Posizione Organizzativa",
            visible: true,
          },
          RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          PROFILO: {
            type: "string",
            caption: "Profilo",
            visible: true,
          },
          DATA_AGGIORNAMENTO: {
            type: "date string",
            caption: "Data agg.",
            pattern: "dd/MM/yyyy",
            visible: false,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT8(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/responsabili?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          DATA_AGGIORNAMENTO: {
            type: "date string",
            caption: "Data agg.",
            pattern: "dd/MM/yyyy",
            visible: false,
          },
          CODICE_FISCALE: {
            type: "string",
            caption: "Codice Fiscale",
            visible: false,
          },
          CODICE_INTERNO: {
            type: "string",
            caption: "Matricola",
            visible: true,
          },
          CODICE_UO: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          F_FORZATURA: {
            type: "number",
            caption: "Flag forzatura",
            visible: true,
          },
          F_INTERIM: {
            type: "number",
            caption: "Flag interim",
            visible: true,
          },
          DAL: {
            type: "date string",
            caption: "Inizio",
            pattern: "dd/MM/yyyy",
          },
          AL: {
            type: "date string",
            caption: "Fine",
            pattern: "dd/MM/yyyy",
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT9(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/strutture?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          CODICE: {
            type: "string",
            caption: "Codice",
            visible: true,
          },
          INTESTAZIONE: {
            type: "string",
            caption: "Intestazione",
            visible: true,
          },
          LIVELLO: {
            type: "string",
            caption: "Livello",
            visible: true,
          },
          PADRE_CODICE: {
            type: "string",
            caption: "Codice padre",
            visible: true,
          },
          PADRE_INTESTAZIONE: {
            type: "string",
            caption: "Intestazione padre",
            visible: true,
          },
          CODICE_RESPONSABILE: {
            type: "string",
            caption: "Responsabile",
            visible: true,
          },
          F_INTERIM: {
            type: "number",
            caption: "Interim",
            visible: true,
          },
          DAL: {
            type: "date string",
            caption: "Inizio",
            pattern: "dd/MM/yyyy",
          },
          AL: {
            type: "date string",
            caption: "Fine",
            pattern: "dd/MM/yyyy",
          },
          DATA_AGGIORNAMENTO: {
            type: "date string",
            caption: "Data agg.",
            pattern: "dd/MM/yyyy",
            visible: false,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT10(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/risorse?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          COGNOME: {
            type: "string",
            caption: "Cognome",
            visible: true,
          },
          NOME: {
            type: "string",
            caption: "Nome",
            visible: true,
          },
          MATRICOLA: {
            type: "string",
            caption: "Matricola",
            visible: true,
          },
          EMAIL: {
            type: "string",
            caption: "Email",
            visible: true,
          },
          INIZIO: {
            type: "date string",
            caption: "Inizio",
            pattern: "dd/MM/yyyy",
          },
          FINE: {
            type: "date string",
            caption: "Fine",
            pattern: "dd/MM/yyyy",
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
          CODICE_STRUTTURA: {
            type: "string",
            caption: "Codice Struttura",
            visible: true,
          },
          STRUTTURA: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          INIZIO_IN_STRUTTURA: {
            type: "date string",
            caption: "Inizio in struttura",
            pattern: "dd/MM/yyyy",
          },
          FINE_IN_STRUTTURA: {
            type: "date string",
            caption: "Fine in struttura",
            pattern: "dd/MM/yyyy",
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
  REPORT11(url: string, anno: number): Flexmonster.Report {
    return {
      localization: {
        grid: {
          dateInvalidCaption: "",
        },
      },
      dataSource: {
        type: "json",
        filename: url + "cube/utenti?anno=" + anno,
        mapping: {
          ANNO: {
            type: "string",
            caption: "Anno",
            visible: false,
          },
          USERNAME: {
            type: "string",
            caption: "Username",
            visible: true,
          },
          NOME: {
            type: "string",
            caption: "Nome",
            visible: true,
          },
          RUOLO: {
            type: "string",
            caption: "Ruolo",
            visible: true,
          },
          AGGIUNTO: {
            type: "string",
            caption: "Flag Aggiunto",
            visible: true,
          },
          CODICE_STRUTTURA: {
            type: "string",
            caption: "Codice struttura",
            visible: true,
          },
          STRUTTURA: {
            type: "string",
            caption: "Struttura",
            visible: true,
          },
          RISORSA: {
            type: "string",
            caption: "Risorsa",
            visible: true,
          },
          MATRICOLA: {
            type: "string",
            caption: "Matricola",
            visible: true,
          },
          CATEGORIA: {
            type: "string",
            caption: "Categoria",
            visible: true,
          },
          INCARICO: {
            type: "string",
            caption: "Incarico",
            visible: true,
          },
        },
      },
      options: {
        dateTimePattern: "dd/MM/yyyy",
        grid: {
          type: "flat",
          showTotals: "off",
          showGrandTotals: "off",
        },
      },
    };
  }
}
