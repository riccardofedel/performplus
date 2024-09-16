import { Component, OnInit, NgZone, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { User, Utente } from 'src/app/_models';
import { Subscription } from 'rxjs';
import { AuthenticationService, StampeService } from "src/app/_services";
import { FieldsService } from 'src/app/_services/fields.service';
import { PermissionService } from '../../_services/permission.service';
import * as Flexmonster from 'flexmonster';
import { FlexmonsterPivot } from 'ng-flexmonster';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: "app-bi",
  templateUrl: "./bi.component.html",
  styleUrls: ["./bi.component.scss"],
  providers: [TranslatePipe],
})
export class ReportEGraficiComponent implements OnInit, OnDestroy {
  env = environment;

  constructor(
    private router: Router,
    private fieldsService: FieldsService,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private authenticationService: AuthenticationService,
    private _TranslatePipe: TranslatePipe,
    private stampeService: StampeService,
    private formBuilder: FormBuilder
  ) {}

  nomeReport: string | undefined;

  model: any;

  subs: Subscription[] = [];

  currentUser?: User;

  anno: number | undefined;
  idEnte: number | undefined;

  loading = false;

  pivot: Flexmonster.Pivot | undefined;

  flexmonsterKey: string = environment.flexmonsterKey;

  flexDataServer: string = environment.flexDataServer;

  API_URL: string = environment.API_URL;

  searchForm!: FormGroup;

  reports: any = [];
  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.loading = true;
          this.anno = this.currentUser.operator?.anno;
          this.idEnte = this.currentUser.operator?.idEnte;
          let sub2: Subscription = this.stampeService
            .getReports(this.idEnte!, this.anno!, "")
            .subscribe((res) => {
              //console.log("items:", res);
              this.reports = res;
            });
            this.subs.push(sub2);
        }
      }
    );
    this.subs.push(sub1);
    this.searchForm = this.formBuilder.group({
      report: [null, Validators.required],
    });
    this.pivot = new Flexmonster({
      container: "pivot-container",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        options: {
          configuratorButton: false,
        },
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
    });
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
  customizeToolbar(toolbar: Flexmonster.Toolbar) {
    toolbar.showShareReportTab = false;
    // get all tabs

    let tabs = toolbar.getTabs();
    //console.log("----------------", tabs);
    toolbar.getTabs = function () {
      // remove the Connect tab using its id
      tabs = tabs.filter((tab) => tab.id == "fm-tab-export");
      return tabs;
    };
  }

  get f() {
    return this.searchForm?.controls;
  }
  ricarica() {
    //console.log("ricarica:", this.f);
    let nomeReport = this.f.report.value;
    if (!nomeReport) {
      return;
    }
    let sub: Subscription = this.stampeService
      .getReport(this.idEnte!, this.anno!, nomeReport)
      .subscribe((a) => {
        if (a) {
          this.pivot!.setReport(a);
        }
      });
      this.subs.push(sub);
  }
}