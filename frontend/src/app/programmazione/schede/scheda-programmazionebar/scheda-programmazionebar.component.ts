import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from "@angular/core";
import { first } from "rxjs/operators";

import { ActivatedRoute, Router } from "@angular/router";
import { Subject, Subscription } from "rxjs";
import {
  AuthenticationService,
  Permission,
  PermissionService,
  ProgrammazioneService,
} from "src/app/_services";
import { User } from "src/app/_models";
import { AlberoProgrammazioneService } from 'src/app/_services/albero.programmazione.service';
import { Pages, Schede } from "src/app/programmazione/constants";

@Component({
  selector: "scheda-programmazionebar",
  templateUrl: "scheda-programmazionebar.component.html",
  host: {
    "(document:click)": "onClick($event)",
  },
})
export class SchedaProgrammazionebarComponent implements OnInit {
  @Input() page: string | undefined;
  @Input() item: string | undefined;
  @Output() cambiaScheda: EventEmitter<any> = new EventEmitter<any>();
  @ViewChild("dropdown") dropdown: ElementRef | undefined;

  constructor(
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private programmazioneService: ProgrammazioneService,
    private alberoProgrammazioneService: AlberoProgrammazioneService
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
  }

  Permission = Permission;

  dropdownShown: any = {};

  subs: Subscription[] = [];

  currentUser: User | undefined;

  introduzioneElementi: any | undefined;

  onClick(event: any) {
    if (this.dropdown && !this.dropdown.nativeElement.contains(event.target)) {
      this.closeAllDropdown();
    }
  }

  closeAllDropdown() {
    this.dropdownShown = {};
  }

  toggleDropDown(key: string) {
    //se sto aprendo un figlio li chiudo tutti
    if (!this.dropdownShown[key]) {
      this.closeAllDropdown();
      this.dropdownShown["top"] = true;
    }
    this.dropdownShown[key] = !this.dropdownShown[key];
  }

  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub1);
    let sub2: Subscription = this.programmazioneService
      .getIntroduzioneElementi()
      .subscribe((result: any) => {
        this.introduzioneElementi = result;
      });
      this.subs.push(sub2);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  cambiaIntroduzione(item: string) {
    this.router.navigate(["/programmazione/introduzione/" + item]);
  }
  vaiAConsuntivazione() {
    //console.log("vaiAConsuntivazione:", this.page);
    if (this.page === Pages.programmazione) {
      this.cambiaScheda.emit(Schede.consuntivazione);
    } else {
      this.router.navigate(["/programmazione/consuntivazione"]);
    }
  }
  vaiAProgrammazione() {
    //console.log("vaiAProgrammazione:", this.page);
    if (this.page === Pages.consuntivazione) {
      this.cambiaScheda.emit(Schede.programmazione);
    } else {
      this.router.navigate(["/programmazione"]);
    }
  }
}
