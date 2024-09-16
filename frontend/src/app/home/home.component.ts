import { Component, OnDestroy, OnInit } from "@angular/core";
import { UserService } from "../_services/user.service";
import { AuthenticationService } from "../_services/authentication.service";
import { Router } from "@angular/router";
import { CruscottoService, Permission, PermissionService } from "../_services";
import { User } from "../_models";
import { Subscription } from "rxjs";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.css"],
})
export class HomeComponent implements OnInit, OnDestroy {
  content?: string;

  currentUser: User | undefined;

  subs: Subscription[] = [];

  Permission = Permission;

  nessunUtente = false;

  activeUser = true;

  constructor(
    private userService: UserService,
    private router: Router,
    private cruscottoService: CruscottoService,
    public permissionService: PermissionService,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    let sub1: Subscription = this.authenticationService.currentUser.subscribe(
      (user: User) => {
        this.currentUser = user;
        this.subs.push(
          this.authenticationService
            .aggiornaDatiUtente(user)
            .subscribe((result) => {
              if (result?.status !== "ACTIVE") {
                this.activeUser = false;
              } else {
                this.activeUser = true;
                if (this.currentUser) {
                  this.currentUser.operator = result;
                  //console.log("currentUser trovato:", this.currentUser);
                } else {
                  if (result) {
                    this.currentUser = {
                      username: result.code,
                      token: "",
                      operator: result,
                    };
                    //console.log("currentUser prima vuoto:", this.currentUser);
                    window.location.reload();
                  }
                }
              }
            })
        );
      }
    );
    this.subs.push(sub1);
  }

  ngOnDestroy() {
    if (this.subs) {
      this.subs.map((s) => s.unsubscribe());
    }
  }
}
