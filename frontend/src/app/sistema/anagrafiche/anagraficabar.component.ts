import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { first } from 'rxjs/operators';

import { ActivatedRoute, Router } from "@angular/router";
import { Subject, Subscription } from "rxjs";
import { AuthenticationService, Permission, PermissionService } from '../../_services';
import { User } from '../../_models';

@Component({
  selector: "anagraficabar",
  templateUrl: "anagraficabar.component.html",
})
export class AnagraficabarComponent implements OnInit, OnDestroy {
  @Input() page: string | undefined;

  Permission = Permission;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService
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
}
