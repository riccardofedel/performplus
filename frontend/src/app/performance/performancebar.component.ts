import { Component, OnInit, Input, OnDestroy } from "@angular/core";
import { first } from "rxjs/operators";

import { ActivatedRoute, Router } from "@angular/router";
import { Subject, Subscription } from "rxjs";
import {
  AuthenticationService,
  Permission,
  PermissionService,
} from "../_services";
import { User } from "../_models";
import { RegistrazioniService } from '../_services/registrazioni.service';

@Component({
  selector: "performancebar",
  templateUrl: "performancebar.component.html",
})
export class PerformancebarComponent implements OnInit, OnDestroy {
  @Input() page: string | undefined;

  Permission = Permission;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    public permissionService: PermissionService,
    public registrazioniService: RegistrazioniService,
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;
  admin = false;
  responsabile = false;
  risorsa = false;
  referente=false;
  ngOnInit() {
    //console.log("-----verifica responsabile------")
    this.subs.push(
      this.authenticationService.currentUser?.subscribe((x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.admin =
            this.currentUser?.operator?.ruolo == "AMMINISTRATORE" ||
            this.currentUser?.operator?.ruolo == "SUPPORTO_SISTEMA";
          if (this.currentUser?.operator?.idRisorsa) {
            this.responsabile =
              this.currentUser?.operator?.ruolo == "AMMINISTRATORE" ||
              this.currentUser?.operator?.ruolo == "SUPPORTO_SISTEMA" ||
              this.currentUser?.operator?.ruolo == "POSIZIONE_ORGANIZZATIVA";
            this.risorsa =
              this.currentUser?.operator?.ruolo == "POSIZIONE_ORGANIZZATIVA" ||
              this.currentUser?.operator?.ruolo == "RISORSA" ||
              this.currentUser?.operator?.ruolo == "REFERENTE";
              this.referente =
                this.currentUser?.operator?.ruolo == "REFERENTE";
            this.subs.push(this.registrazioniService.verificaValutatore(
              this.currentUser.operator.idRisorsa
            ).subscribe({
              next: (result) =>{
                if(result===true){
                  this.responsabile = true;
                }else{
                  this.responsabile = false;
                }
              },
              error: (err)=>{

              }

            }));
            
          }
        }
      })
    );
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}
