import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from "rxjs";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2'
import { TranslateService } from '@ngx-translate/core';
import { AnagraficheService, AuthenticationService, ProgrammazioneService, RisorseService, TipoNodo } from 'src/app/_services';
import { FieldsService } from 'src/app/_services/fields.service';
import { User } from 'src/app/_models';
import { PermissionService } from '../../../_services/permission.service';



@Component({
  templateUrl: "aggiunginodo.component.html",
  styleUrls: ["aggiunginodo.component.scss"],
})
export class AggiunginodoComponent implements OnInit, OnDestroy {
  currentUser: User | undefined;

  constructor(
    public translate: TranslateService,
    public fieldsService: FieldsService,
    private formBuilder: FormBuilder,
    public programmazioneService: ProgrammazioneService,
    public activeModal: NgbActiveModal,

    private authenticationService: AuthenticationService,
    private risorseService: RisorseService,
    private permissionService: PermissionService
  ) {}

  //parametri: per modificare
  @Input() id: number | undefined;

  //per aggiungere
  @Input() idPadre: number | undefined;

  //a che livello siamo?
  @Input() livello: number | undefined;

  //tipo nodo in fase di modifica (in fase di creazione arriva dal servizio)
  @Input() tipoNodo: string | undefined;

  TipoNodo = TipoNodo;

  result = null;

  isLoadingResult = false;

  creaForm!: FormGroup;

  codiceCompleto: string | undefined;
  codiceRidottoPadre: string | undefined;

  loading = false;

  keyword = "description";

  enabledStrategico = false;

  fields: string[] = [];

  toggleValorePubblico = true;
  toggleBsc = false;

  listaProspettive = [
    "crescita e innovazione",
    "economico-finanziaria",
    "processi interni",
    "soddisfazione utente",
  ];
  dimensioni = [
    "ambientale",
    "economico",
    "salute interna",
    "sanitario",
    "sociale",
  ];

  listaTipo = [];
  //listaTipologia = [];
  //listaModalitaAttuative=[];

  /**
   * vengono chiamate solo quelle che servono
   * secondo la valorizzazione di fields
   */
  anagrafiche: any = {};

  organizzazioni = [];

  responsabili: any[] = [];

  subs: Subscription[] = [];
  ngOnInit() {
    this.loading = true;
    this.codiceCompleto = "";
    this.codiceRidottoPadre = "";
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (x: any) => {
        this.currentUser = x;
        if (this.currentUser && this.currentUser) {
          this.subs.push(
            this.programmazioneService.getTipi().subscribe((result) => {
              this.listaTipo = result;
            })
          );
          // this.subs.push(
          //   this.programmazioneService.getTipologe().subscribe((result) => {
          //     this.listaTipologia = result;
          //   })
          // );
          //  this.subs.push(
          //    this.programmazioneService.getModalitaAttuative().subscribe((result) => {
          //      this.listaModalitaAttuative = result;
          //    })
          //  );

          let sub2: Subscription = this.programmazioneService
            .getOrganizzazioni(
              this.currentUser!.operator!.idEnte,
              this.currentUser!.operator!.anno,
              ""
            )
            .subscribe((result) => {
              this.organizzazioni = result;
            });
          this.subs.push(sub2);
          this.aggiornaFiltroResponsabili("");

          if (this.id !== undefined) {
            let sub4: Subscription = this.programmazioneService
              .preparaModificaProgrammazione(this.id)
              .subscribe((result) => {
                this.loading = false;
                this.fields = result.fields;
                this.codiceCompleto = result.codiceCompleto;
                this.codiceRidottoPadre = result.codiceRidottoPadre + ".";
                this.enabledStrategico = result.enabledStrategico;
                this.creaForm = this.formBuilder.group({
                  codice: [result.codice, Validators.required],
                  denominazione: [result.denominazione, [Validators.required]],
                  idPadre: result.idPadre,
                  descrizione: [result.descrizione],
                  note: [result.note],
                  descrizioneIntroduttivaMissione: [
                    result.descrizioneIntroduttivaMissione,
                  ],
                  organizzazione: [result.organizzazione, Validators.required],
                  organizzazioni: [result.organizzazioni],
                  idResponsabile: [
                    result.responsabile
                      ? {
                          id: result.responsabile,
                          description: result.nomeResponsabile,
                        }
                      : null,
                  ],
                  capitoloBilancio: [result.capitoloBilancio],
                  strategico: [result.strategico],
                  inizio: [result.inizio, Validators.required],
                  scadenza: [result.scadenza],

                  flagPnrr: [result.flagPnrr],
                  politica: [result.politica],
                  dimensione: [result.dimensione],
                  contributors: [result.contributors],
                  stakeholders: [result.stakeholders],

                  focusSemplificazione: [result.focusSemplificazione],
                  focusDigitalizzazione: [result.focusDigitalizzazione],
                  focusAccessibilita: [result.focusAccessibilita],
                  focusPariOpportunita: [result.focusPariOpportunita],
                  focusRisparmioEnergetico: [result.focusRisparmioEnergetico],

                  prospettiva: [result.prospettiva],
                  innovazione: [result.innovazione],
                  annualita: [result.annualita],

                  tipo: [result.tipo],
                  obiettivoImpatto: [result.obiettivoImpatto],
                  bloccato: [result.bloccato],

                  flagOIV: [result.flagOIV],
                  noteOIV: [result.noteOIV],
                });
              });
            this.subs.push(sub4);
          } else {
            let sub5: Subscription = this.programmazioneService
              .preparaProgrammazione(this.idPadre)
              .subscribe((result) => {
                this.tipoNodo = result.tipoNodo;
                this.fields = result.fields;
                this.loading = false;
                this.codiceCompleto = result.codiceCompleto;
                this.codiceRidottoPadre = result.codiceRidottoPadre + ".";
                this.enabledStrategico = result.enabledStrategico;

                this.creaForm = this.formBuilder.group({
                  codice: [result.codice, Validators.required],
                  denominazione: [null, Validators.required],
                  idPadre: this.idPadre,

                  descrizione: [""],

                  note: [""],
                  descrizioneIntroduttivaMissione: [""],
                  organizzazione: [null, Validators.required],
                  organizzazioni: [null],
                  idResponsabile: [
                    result.responsabile
                      ? {
                          id: result.responsabile,
                          description: result.nomeResponsabile,
                        }
                      : null,
                  ],

                  strategico: [false],
                  inizio: [null, Validators.required],
                  scadenza: [null],

                  flagPnrr: [null],
                  politica: [null],
                  dimensione: [null],
                  contributors: [null],
                  stakeholders: [null],

                  focusSemplificazione: [null],
                  focusDigitalizzazione: [null],
                  focusAccessibilita: [null],
                  focusPariOpportunita: [null],
                  focusRisparmioEnergetico: [null],

                  prospettiva: [null],
                  innovazione: [null],
                  annualita: [null],
                  tipo: [null],
                  obiettivoImpatto: [false],
                  bloccato: [false],

                  flagOIV: [false],
                  noteOIV: [null],
                });
              });
            this.subs.push(sub5);
          }
        }
      }
    );
    this.subs.push(sub1);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }

  onSubmit() {
    let value = this.creaForm.value;

    value.inizio = this.creaForm?.value?.inizio;
    value.scadenza = this.creaForm?.value?.scadenza;
    if (this.creaForm?.value?.idResponsabile?.id) {
      value.responsabile = this.creaForm?.value?.idResponsabile?.id;
    } else {
      value.responsabile = null;
    }

    // let tabs=this.programmazioneService.getVarieTabs();
    // for (let i in tabs) {
    //   if (!value[tabs[i]]) {
    //     value[tabs[i]]=null;
    //   }
    // }

    if (this.id !== undefined) {
      delete value.idPadre;
      let sub: Subscription = this.programmazioneService
        .updateProgrammazione(this.id, value)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'obiettivo: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub);
    } else {
      let sub: Subscription = this.programmazioneService
        .setProgrammazione(value)
        .subscribe(
          (result) => {
            this.activeModal.close("refresh");
          },
          (error) => {
            this.loading = false;
            Swal.fire({
              title: this.translate.instant("sorry"),
              text: "Errore nel salvataggio dell'obiettivo: " + error,
              icon: "error",
              confirmButtonText: "Ok",
            });
          }
        );
      this.subs.push(sub);
    }
  }

  visualizzaField(field: string) {
    return (
      this.fields === undefined ||
      this.fields === null ||
      this.fields.length == 0 ||
      this.fields.some((x) => x === field)
    );
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  aggiornaFiltroResponsabili(testo: string | undefined) {
    this.isLoadingResult = true;
    let sub: Subscription = this.risorseService
      .search(
        this.currentUser!.operator!.idEnte,
        this.currentUser!.operator!.anno,
        0,
        20,
        true,
        true,
        testo,
        null
      )
      .subscribe((result) => {
        let responsabili = [];
        for (let item of result.content) {
          responsabili.push({
            id: item.id,
            description: item.cognome + " " + item.nome,
          });
        }
        this.responsabili = responsabili;
        this.isLoadingResult = false;
      });
    this.subs.push(sub);
  }

  onChangeSearch($event: any) {
    this.aggiornaFiltroResponsabili($event);
  }

  isAdmin() {
    return this.permissionService.isAdmin(this.currentUser);
  }
}


