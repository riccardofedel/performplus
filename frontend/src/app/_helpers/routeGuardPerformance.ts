import { Injectable } from "@angular/core";
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from "@angular/router";
import { AuthenticationService } from "../_services/authentication.service";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class RouteGuardPerformance implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}
  env=environment;
  
  public canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ) {
    const currentUser = this.authenticationService.currentUserValue;
    if (currentUser && currentUser.operator) {
      if (currentUser.operator.ruolo == "AMMINISTRATORE" || 
        currentUser.operator.ruolo == "SUPPORTO_SISTEMA") {
        this.router.navigateByUrl("/performance/registrazioni");
      } else if (
        (currentUser.operator.ruolo == "POSIZIONE_ORGANIZZATIVA" ||
          currentUser.operator.ruolo == "REFERENTE") &&
        currentUser.operator.valutatore
      ) {
        this.router.navigateByUrl("/performance/valutazioni");
      } else {
        this.router.navigateByUrl("/performance/valutazione");
      }
      return true;
    }

    // not logged in so redirect to login page with the return url
    if (!currentUser) {
      //console.log("---RouteGuardPerformance to login");
      if (
        this.authenticationService.currentAdminValue === true ||
        !this.env.ssoLoginUrl
      )
        this.router.navigate(["/login"], {
          queryParams: { returnUrl: state.url },
        });
      else
        this.router.navigate(["/login-sso"], {
          queryParams: { returnUrl: state.url },
        });
    }
    return false;
  }
}
