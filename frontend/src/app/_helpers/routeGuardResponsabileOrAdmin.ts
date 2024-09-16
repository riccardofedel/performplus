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
export class RouteGuardResponsabileOrAdmin implements CanActivate {
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
      return (
        currentUser.operator.ruolo == "AMMINISTRATORE" ||
        currentUser.operator.ruolo == "SUPPORTO_SISTEMA" ||
        currentUser.operator.ruolo == "POSIZIONE_ORGANIZZATIVA" ||
        currentUser.operator.ruolo == "REFERENTE"
      );
    }

    // not logged in so redirect to login page with the return url
    if (!currentUser) {
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
