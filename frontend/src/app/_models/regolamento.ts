import { Categoria } from "./regolamento.categorie";
import { Incarico } from "./regolamento.incarichi";

export class Regolamento {
  id!: number;
  pesoObiettiviIndividuali?: number;
  pesoObiettiviDiStruttura?: number;
  pesoObiettiviDiPerformance?: number;
  pesoComportamentiOrganizzativi?: number;
  po: boolean = false;
  categorie?: Categoria[];
  incarichi?: Incarico[];
  intestazione!: string;
  descCategorie?: string;
  descIncarichi?: string;
}
