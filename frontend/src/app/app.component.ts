import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import { AuthenticationService } from "./_services/authentication.service";
import { User } from "./_models/user";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { TranslateService } from "@ngx-translate/core";
import { CruscottoService, Permission, PermissionService } from "./_services";
import { environment } from "src/environments/environment";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ApppasswordComponent } from "./apppassword/apppassword.component";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
  host: {
    "(document:click)": "onClick($event)",
  },
})
export class AppComponent implements OnInit, OnDestroy {
  favIcon: HTMLLinkElement | null = document.querySelector("#appIcon");

  title = "performplus-community";

  dropdownHidden = true;

  anni: string[] = [];

  currentUser: User | undefined;

  subs: Subscription[] = [];

  @ViewChild("dropdown") dropdown: ElementRef | undefined;

  Permission = Permission;

  logo = environment.flag_img;

  favImage = environment.favicon;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private modalService: NgbModal,
    private cruscottoService: CruscottoService,
    public permissionService: PermissionService
  ) {
    if (this.favIcon) {
      this.favIcon.href = this.favImage;
    }
  }

  ssoLogoutUrl = environment.ssoLogoutUrl;

  ngOnInit(): void {
    let sub1: Subscription = this.authenticationService.currentUser?.subscribe(
      (x) => {
        this.currentUser = x;
        if (this.currentUser?.operator) {
          var idEnte = this.currentUser?.operator?.idEnte;
          if (!idEnte) idEnte = 0;
          //console.log(">>>>>>>>>idEnte",idEnte);
          let sub2: Subscription = this.cruscottoService
            .getAnniList(idEnte)
            .subscribe((result) => {
              this.anni = result;
            });
          this.subs.push(sub2);
          this.dropdownHidden = true;
        }
        //if (this.currentUser && !this.currentUser.operator) {
        //  this.logout();
        //}
      }
    );
    this.subs.push(sub1);
  }

  onClick(event: any) {
    if (this.dropdown && !this.dropdown.nativeElement.contains(event.target)) {
      this.dropdownHidden = true;
    }
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(["/logout"]);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }

  isActive(name: string) {
    if (name === "/" && this.router.url === "/") {
      return true;
    }
    return this.router.url.substr(1, name.length) === name;
  }

  cambiaAnno(anno: number) {
    //console.log("cambia anno:", anno);
    this.permissionService.cambiaAnno(anno, this.currentUser);
    location.reload();
  }

  cambiaPassword() {
    const modalRef = this.modalService.open(ApppasswordComponent, {
      size: "lg",
      keyboard: false,
      backdrop: "static",
    });

    modalRef.result.then(
      (result) => {},
      (reason) => {}
    );
  }
}
