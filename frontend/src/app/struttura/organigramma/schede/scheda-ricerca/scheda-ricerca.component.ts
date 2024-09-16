import { Component, OnInit, NgZone, OnDestroy, ElementRef, Output, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

import { FormBuilder, FormGroup } from '@angular/forms';
import { Permission, PermissionService } from 'src/app/_services';
import { User } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlberoStruttureService } from 'src/app/_services/albero.strutture.service';



@Component({
  selector: "scheda-ricerca",
  templateUrl: "./scheda-ricerca.component.html",
  styleUrls: ["./scheda-ricerca.component.scss"],
  providers: [TranslatePipe],
})
export class SchedaRicercaComponent implements OnInit, OnDestroy {
  constructor(
    private router: Router,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    private formBuilder: FormBuilder,
    public translate: TranslateService,
    private _TranslatePipe: TranslatePipe,
    public fb: FormBuilder,
    private authenticationService: AuthenticationService,
    public alberoStrutturaService: AlberoStruttureService
  ) {}

  @Output() confirmSearch: EventEmitter<any> = new EventEmitter<any>();

  Permission = Permission;

  subs: Subscription[] = [];

  currentUser: User | undefined;

  sorter = null;

  loadedRisorse = false;

  closed: any | undefined;

  cercaForm!: FormGroup;

  ngOnInit() {
    this.cercaForm = this.formBuilder.group({
      intestazione: this.alberoStrutturaService.getSearchData()?.intestazione,
      responsabile: this.alberoStrutturaService.getSearchData()?.responsabile,
    });
    this.closed = {};
    let sub: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
      }
    );
    this.subs.push(sub);
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.cercaForm?.controls;
  }

  onSubmit() {
    if (this.cercaForm.valid) {
      this.alberoStrutturaService.updateSearchData(this.cercaForm.value);
      this.confirmSearch.emit(this.cercaForm.value);
    }
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}

