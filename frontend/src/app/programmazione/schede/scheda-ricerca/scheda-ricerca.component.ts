import { Component, OnInit, NgZone, OnDestroy, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup } from '@angular/forms';
import { AlberoProgrammazioneService, ConsuntivazioneService, Permission, PermissionService, ProgrammazioneService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';

@Component({
  selector: "scheda-ricerca",
  templateUrl: "./scheda-ricerca.component.html",
  styleUrls: ["./scheda-ricerca.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaRicercaComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    public permissionService: PermissionService,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    public programmazioneService: ProgrammazioneService,
    public consuntivazioneService: ConsuntivazioneService,
    private authenticationService: AuthenticationService,
    public alberoProgrammazioneService: AlberoProgrammazioneService
  ) {}

  @Input() searchData: any | undefined;
  @Input() consuntivazione: boolean | undefined;
  @Output() confirmSearch: EventEmitter<any> = new EventEmitter<any>();

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  loadedRisorse = false;

  //per la ricerca (select)
  direzioni: any[] = [];
  responsabili: any[] = [];
  risorse: any[] = [];
  //programmi: any[] = [];
  statiNodo: any[] = [];
  statiProposta: any[] = [];

  isLoadingDirezioni = false;
  isLoadingResponsabili = false;
  isLoadingRisorse = false;

  closed: any | undefined;

  cercaForm!: FormGroup;

  cercaRisorseForm!: FormGroup;

  strategicoEnum = [
    { flag: true, descrizione: "Strategico" },
    { flag: false, descrizione: "Non strategico" },
  ];

  keyword = "description";

  ngOnInit() {
    this.reload();
  }

  resetRisorse() {
    this.loadedRisorse = false;
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  onSubmit() {
    if (this.cercaForm.valid) {
      let cerca = this.cercaForm?.value;
      cerca.idResponsabile = this.f?.idResponsabile?.value?.id;
      cerca.idRisorsa = this.f?.idRisorsa?.value?.id;
      this.alberoProgrammazioneService.updateSearchData(cerca);
      this.confirmSearch.emit(this.cercaForm.value);
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  onChangeSearchResponsabile($event: any) {
    if (!$event) {
      return;
    }
    this.isLoadingResponsabili = true;
    let sub: Subscription = this.programmazioneService
      .getResponsabili(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingResponsabili = false;
        this.responsabili = result.map((val: any) => {
          return { id: val.id, description: val.descrizione +" ["+val.codice+"]"};
        });
      });
    this.subs.push(sub);
  }
  onChangeSearchRisorsa($event: any) {
    if (!$event) {
      return;
    }
    this.isLoadingRisorse = true;
    let sub: Subscription = this.programmazioneService
      .getRisorse(
        this.currentUser?.operator?.idEnte,
        this.currentUser?.operator?.anno,
        $event
      )
      .subscribe((result) => {
        this.isLoadingRisorse = false;
        this.risorse = result.map((val: any) => {
          return {
            id: val.id,
            description: val.descrizione + " [" + val.codice + "]",
          };
        });
      });
    this.subs.push(sub);
  }
  reload() {
    this.cercaForm = this.formBuilder.group({
      testo: this.alberoProgrammazioneService.getSearchData()?.testo,
      idResponsabile:
        this.alberoProgrammazioneService.getSearchData()?.idResponsabile,
      idRisorsa: this.alberoProgrammazioneService.getSearchData()?.idRisorsa,
      idDirezione:
        this.alberoProgrammazioneService.getSearchData()?.idDirezione,
      codice: this.alberoProgrammazioneService.getSearchData()?.codice,

    });
    this.cercaRisorseForm = this.formBuilder.group({
      nome: [""],
    });
    this.closed = {};
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          let sub6: Subscription = this.programmazioneService
            .getDirezioni(
              this.currentUser?.operator?.idEnte,
              this.currentUser?.operator?.anno,
              ""
            )
            .subscribe((result) => {
              this.isLoadingDirezioni = false;
              this.direzioni = result;
            });
          this.subs.push(sub6);
        }
      }
    );
    this.subs.push(sub1);
  }
  ngOnChanges(changes: SimpleChanges) {
    /*console.log(
      "scheda-ricerca changes:",
      changes?.consuntivazione?.currentValue !==
        changes?.consuntivazione?.previousValue,
      "::",
      changes
    );*/
    if (
      changes?.consuntivazione?.currentValue !==
      changes?.consuntivazione?.previousValue
    )
      this.reload();
  }
}

