import { Component, OnInit, NgZone, ViewChild, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { TranslateService, TranslatePipe } from "@ngx-translate/core";
import { User, Utente } from "src/app/_models";
import { Subscription } from "rxjs";
import { AuthenticationService, StampeService } from "src/app/_services";
import { FieldsService } from "src/app/_services/fields.service";
import { PermissionService } from "src/app/_services/permission.service";
import * as Flexmonster from "flexmonster";
import { FlexmonsterPivot, FlexmonsterPivotModule } from "ng-flexmonster";
import { environment } from "src/environments/environment";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import Swal from "sweetalert2";
import { TemplateReports } from "src/app/_services/bi.reports";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CsvDialogComponent } from "./csv/csv.dialog.component";

@Component({
  selector: "app-bi",
  templateUrl: "./bi.component.html",
  styleUrls: ["./bi.component.scss"],
  providers: [TranslatePipe],
})
export class BIComponent implements OnInit, OnDestroy {
  env = environment;

  constructor(
    private router: Router,
    private fieldsService: FieldsService,
    private route: ActivatedRoute,
    public permissionService: PermissionService,
    public translate: TranslateService,
    private readonly zone: NgZone,
    private authenticationService: AuthenticationService,
    private stampeService: StampeService,
    private _TranslatePipe: TranslatePipe,
    private formBuilder: FormBuilder,
    private templateReports: TemplateReports,
    private modalService: NgbModal
  ) {}

  flex = 2;

  model: any;

  subs: Subscription[] = [];

  currentUser?: User;

  anno: number | undefined;
  idEnte: number | undefined;

  loading = false;

  pivot1: Flexmonster.Pivot | undefined;
  pivot2: Flexmonster.Pivot | undefined;
  pivot3: Flexmonster.Pivot | undefined;
  pivot4: Flexmonster.Pivot | undefined;
  pivot5: Flexmonster.Pivot | undefined;
  pivot6: Flexmonster.Pivot | undefined;
  pivot7: Flexmonster.Pivot | undefined;
  pivot8: Flexmonster.Pivot | undefined;
  pivot9: Flexmonster.Pivot | undefined;
  pivot10: Flexmonster.Pivot | undefined;
  pivot11: Flexmonster.Pivot | undefined;

  flexmonsterKey: string = environment.flexmonsterKey;

  flexDataServer: string = environment.flexDataServer;

  API_URL: string = environment.API_URL;

  creaForm!: FormGroup;

  formSubscription: Subscription | undefined;

  reports: any = [];

  condividi = false;

  custom = false;

  ngOnInit() {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser) {
          this.loading = true;
          this.anno = this.currentUser.operator?.anno;
          this.idEnte = this.currentUser.operator?.idEnte;
        }
        let sub2: Subscription = this.stampeService
          .getReports(this.idEnte!, this.anno!, this.tipo())
          .subscribe((res) => {
            this.reports = res;
          });
        this.subs.push(sub2);
      }
    );
    this.subs.push(sub1);
    this.creaForm = this.formBuilder.group({
      nomeReport: [null, Validators.required],
      descrizione: [null],
      report: [null],
    });
    this.pivot1 = new Flexmonster({
      container: "pivot-container1",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT1(this.API_URL, this.anno!),
    });

    this.pivot2 = new Flexmonster({
      container: "pivot-container2",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT2(this.API_URL, this.anno!),
    });

    this.pivot3 = new Flexmonster({
      container: "pivot-container3",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT3(this.API_URL, this.anno!),
    });

    this.pivot4 = new Flexmonster({
      container: "pivot-container4",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT4(this.API_URL, this.anno!),
    });
    this.pivot5 = new Flexmonster({
      container: "pivot-container5",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT5(this.API_URL, this.anno!),
    });
    this.pivot6 = new Flexmonster({
      container: "pivot-container6",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT6(this.API_URL, this.anno!),
    });
    this.pivot7 = new Flexmonster({
      container: "pivot-container7",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT7(this.API_URL, this.anno!),
    });
    this.pivot8 = new Flexmonster({
      container: "pivot-container8",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT8(this.API_URL, this.anno!),
    });
    this.pivot9 = new Flexmonster({
      container: "pivot-container9",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT9(this.API_URL, this.anno!),
    });

    this.pivot10 = new Flexmonster({
      container: "pivot-container10",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT10(this.API_URL, this.anno!),
    });

    this.pivot11 = new Flexmonster({
      container: "pivot-container11",
      licenseKey: this.flexmonsterKey,
      toolbar: true,
      beforetoolbarcreated: this.customizeToolbar,
      global: {
        localization: "https://cdn.flexmonster.com/loc/it.json",
      },
      report: this.templateReports.REPORT11(this.API_URL, this.anno!),
    });
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  customizeToolbar(toolbar: Flexmonster.Toolbar) {
    //console.log("customizeToolbar", toolbar);

    toolbar.showShareReportTab = false;
    // get all tabs

    let tabs = toolbar.getTabs();
    //console.log(tabs);
    toolbar.getTabs = function () {
      // remove the Connect tab using its id
      tabs = tabs.filter((tab) => tab.id != "fm-tab-connect");

      return tabs;
    };
  }

  mostra(nro: number) {
    //console.log("-----mostra");
    let cambia = false;
    this.creaForm.controls["report"].setValue(null);
    this.creaForm.controls["nomeReport"].setValue(null);
    this.creaForm.controls["descrizione"].setValue(null);
    if (this.custom || !this.flex || this.flex != nro) {
      cambia = true;
    }
    this.custom = false;
    this.flex = nro;
    switch (this.flex) {
      case 1:
        this.pivot1?.setReport(
          this.templateReports.REPORT1(this.API_URL, this.anno!)
        );
        break;
      case 2:
        this.pivot2?.setReport(
          this.templateReports.REPORT2(this.API_URL, this.anno!)
        );
        break;
      case 3:
        this.pivot3?.setReport(
          this.templateReports.REPORT3(this.API_URL, this.anno!)
        );
        break;
      case 4:
        this.pivot4?.setReport(
          this.templateReports.REPORT4(this.API_URL, this.anno!)
        );
        break;
      case 5:
        this.pivot5?.setReport(
          this.templateReports.REPORT5(this.API_URL, this.anno!)
        );
        break;
      case 6:
        this.pivot6?.setReport(
          this.templateReports.REPORT6(this.API_URL, this.anno!)
        );
        break;
      case 7:
        this.pivot7?.setReport(
          this.templateReports.REPORT7(this.API_URL, this.anno!)
        );
        break;
      case 8:
        this.pivot8?.setReport(
          this.templateReports.REPORT8(this.API_URL, this.anno!)
        );
        break;
      case 9:
        this.pivot9?.setReport(
          this.templateReports.REPORT9(this.API_URL, this.anno!)
        );
        break;
      case 10:
        this.pivot10?.setReport(
          this.templateReports.REPORT10(this.API_URL, this.anno!)
        );
        break;
      case 11:
        this.pivot11?.setReport(
          this.templateReports.REPORT11(this.API_URL, this.anno!)
        );
        break;
    }
    if (cambia) {
      let sub: Subscription = this.stampeService
        .getReports(this.idEnte!, this.anno!, this.tipo())
        .subscribe((res) => {
          this.reports = res;
        });
      this.subs.push(sub);
    }
  }
  tipo() {
    switch (this.flex) {
      case 1:
        return "PIAO";
      case 2:
        return "INDICATORI";
      case 3:
        return "ORGANIGRAMMA";
      case 4:
        return "REGISTRAZIONI";
      default:
        return "PIAO";
    }
  }
  salva() {
    //console.log("----salva");
    const nomeReport = this.f.nomeReport.value;
    const descrizione = this.f.descrizione.value;
    const tipoReport = this.tipo();
    let a = {
      filename: nomeReport + ".json",
      destination: "server",
      url:
        this.API_URL +
        "cube/saveReport/" +
        this.idEnte +
        "/" +
        this.anno +
        "?nomeReport=" +
        nomeReport +
        "&tipoReport=" +
        tipoReport +
        (descrizione ? "&descrizione=" + descrizione : ""),
      callbackHandler: function (result: any, error: any) {
        if (error) {
          Swal.fire({
            title: "Errore",
            text: "Errore nel salvataggio del report: " + error.message,
            icon: "error",
            confirmButtonText: "Ok",
          });
        } else {
          Swal.fire({
            text: "Aggiornamento avvenuto con successo",
            icon: "success",
            showConfirmButton: false,
            timer: 1000,
          });
        }
      },
    };
    switch (this.flex) {
      case 1:
        this.pivot1!.save(a);
        break;
      case 2:
        this.pivot2!.save(a);
        break;
      case 3:
        this.pivot3!.save(a);
        break;
      case 4:
        this.pivot4!.save(a);
        break;
      case 5:
        this.pivot5!.save(a);
        break;
      case 6:
        this.pivot6!.save(a);
        break;
      case 7:
        this.pivot7!.save(a);
        break;
      case 8:
        this.pivot8!.save(a);
        break;
      case 9:
        this.pivot9!.save(a);
        break;
      case 10:
        this.pivot10!.save(a);
        break;
      case 11:
        this.pivot11!.save(a);
        break;
    }
    let sub: Subscription = this.stampeService
      .getReports(this.idEnte!, this.anno!, this.tipo())
      .subscribe((res) => {
        this.reports = res;
      });
    this.subs.push(sub);
  }
  // convenience getter for easy access to form fields
  get f() {
    return this.creaForm?.controls;
  }
  changeReport(r: any) {
    this.creaForm.controls["nomeReport"].setValue(r?.codice);
    this.creaForm.controls["descrizione"].setValue(r?.descrizione);
  }
  ricarica() {
    //console.log("----ricarica");
    let nomeReport = this.f.nomeReport.value;
    if (!nomeReport) {
      return;
    }
    let sub: Subscription = this.stampeService
      .getReport(this.idEnte!, this.anno!, this.f.nomeReport.value)
      .subscribe((a) => {
        if (a) {
          switch (this.flex) {
            case 1:
              this.pivot1!.setReport(a);
              break;
            case 2:
              this.pivot2!.setReport(a);
              break;
            case 3:
              this.pivot3!.setReport(a);
              break;
            case 4:
              this.pivot4!.setReport(a);
              break;
            case 5:
              this.pivot5!.setReport(a);
              break;
            case 6:
              this.pivot6!.setReport(a);
              break;
            case 7:
              this.pivot7!.setReport(a);
              break;
            case 8:
              this.pivot8!.setReport(a);
              break;
            case 9:
              this.pivot9!.setReport(a);
              break;
            case 10:
              this.pivot10!.setReport(a);
              break;
            case 11:
              this.pivot11!.setReport(a);
              break;
          }
        }
        this.custom = true;
      });
    this.subs.push(sub);
  }
  condividiToggle() {
    this.condividi = !this.condividi;
  }
  elimina() {
    //console.log("----elimina");
    let nomeReport = this.f.nomeReport.value;
    const tipoReport = this.tipo();
    if (!nomeReport) {
      return;
    }
    Swal.fire({
      title: this.translate.instant("sure?"),
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: "Elimina report:'" + nomeReport + "'",
    }).then((result) => {
      if (result.isConfirmed) {
        let sub1: Subscription = this.stampeService
          .deleteReport(this.idEnte!, this.anno!, nomeReport)
          .subscribe(
            (result: any) => {
              let sub2: Subscription = this.stampeService
                .getReports(this.idEnte!, this.anno!, this.tipo())
                .subscribe((res) => {
                  this.reports = res;
                  this.mostra(this.flex);
                });
              this.subs.push(sub2);
              Swal.fire({
                text: "Cancellazione avvenuta con successo",
                icon: "success",
                showConfirmButton: false,
                timer: 1000,
              });
            },
            (error) => {
              Swal.fire({
                title: "Errore",
                text: "Errore nella cancellazione del report: " + error.message,
                icon: "error",
                confirmButtonText: "Ok",
              });
            }
          );
        this.subs.push(sub1);
      }
    });
  }

  exportToCsvName() {
    if (!this.flex) return "export to csv";
    return this.tipo().toLowerCase() + " to csv";
  }
  exportToCsv() {
    if (!this.flex) return;
    const report = this.tipo().toLowerCase();
    var pivot = undefined;
    switch (this.flex) {
      case 1:
        pivot = this.pivot1;
        break;
      case 2:
        pivot = this.pivot2;
        break;
      case 3:
        pivot = this.pivot3;
        break;
      case 4:
        pivot = this.pivot4;
        break;
      case 5:
        pivot = this.pivot5;
        break;
      case 6:
        pivot = this.pivot6;
        break;
      case 7:
        pivot = this.pivot7;
        break;
      case 8:
        pivot = this.pivot8;
        break;
      case 9:
        pivot = this.pivot9;
        break;
      case 10:
        pivot = this.pivot10;
        break;
      case 11:
        pivot = this.pivot11;
        break;
    }
    const modalRef = this.modalService.open(CsvDialogComponent, {
      size: "md",
      keyboard: false,
      backdrop: "static",
    });
    modalRef.componentInstance.nomeReport = report;
    modalRef.componentInstance.pivot = pivot;

    modalRef.result.then(
      (result) => {
        if (result === "refresh") {
          // this.aggiornaFigli();
        }
      },
      (reason) => {}
    );
  }
}
