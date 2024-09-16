import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { environment } from "../../environments/environment";
import { AuthenticationService } from "../_services/authentication.service";

@Component({ templateUrl: "login-sso.component.html" })
export class LoginSSOComponent  {
  status: string | undefined;
  returnUrl: string | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.admin(false);
    // if (this.authenticationService.currentTokenValue) {
    //   this.router.navigate(["/"]);
    // }
    const status = this.route.snapshot.queryParams["status"];
    const tkn = this.route.snapshot.queryParams["tkn"];
    //console.log("login-sso",status,tkn);
    if (status){
      // pagina con errore
    } else if(tkn) {
      this.authenticationService.tkn(tkn);
      this.authenticationService.sso();
    } else {
       window.location.href = environment.ssoLoginUrl;
    }
  }

 
}
