import { UserValutatore } from "./valutatore";
import { UserValutato } from "./valutato";
import { RegQuestionario } from "./registrazione.questionario";
import { RegRegolamento } from "./registrazione.regolamento";
import { RegOrganizzazione } from "./registrazione.organizzazione";

export class Registrazione {
  id: number | undefined;
  nome: string | undefined;
  valutatore: UserValutatore | undefined;
  valutato: UserValutato | undefined;
  inizioValidita: string | undefined;
  fineValidita: string | undefined;
  percentuale: boolean | undefined;
  questionario: RegQuestionario | undefined;
  organizzazione: RegOrganizzazione | undefined;
  regolamento: RegRegolamento | undefined;
  anno: number | undefined;
  dataPubblicazioneScheda: string | undefined;
  notePubblicazioneScheda: string | undefined;
  dataPresaVisioneScheda: string | undefined;
  notePresaVisioneScheda: string | undefined;
  dataPubblicazioneValutazione: string | undefined;
  notePubblicazioneValutazione: string | undefined;
  dataPresaVisioneValutazione: string | undefined;
  notePresaVisioneValutazione: string | undefined;

  po: boolean | undefined;
  responsabile: boolean | undefined;
  interim: boolean | undefined;

  registrazioneSeparata = false;
  nonValutare = false;
  forzaSchedaSeparata=false;
	forzaValutatore=false;
  mancataAssegnazione=false;
  mancatoColloquio=false;

}
