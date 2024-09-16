import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { first } from 'rxjs/operators';

import { ActivatedRoute, Router } from "@angular/router";
import { Subject, Subscription } from "rxjs";
import { AuthenticationService, Permission, PermissionService } from '../_services';
import { User } from '../_models';

@Component({
  selector: "strutturabar",
  templateUrl: "strutturabar.component.html",
})
export class StrutturabarComponent implements OnInit, OnDestroy {
  @Input() page: string | undefined;

  Permission = Permission;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    public permissionService: PermissionService
  ) {}

  subs: Subscription[] = [];

  currentUser: User | undefined;

  ngOnInit() {
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  vediTutto():boolean{
    return this.currentUser?.operator?.ruolo==='AMMINISTRATORE' ||
     this.currentUser?.operator?.ruolo==='SUPPORTO_SISTEMA' ||
	    this.currentUser?.operator?.ruolo==='DIRETTORE_GENERALE' ||
      this.currentUser?.operator?.ruolo==='OIV';
  }
}
