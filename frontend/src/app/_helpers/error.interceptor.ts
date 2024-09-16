import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";

import { Router } from "@angular/router";
import { AuthenticationService } from "../_services/authentication.service";
import { environment } from "src/environments/environment";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}
    env=environment;
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((err) => {
        if (err.status === 401 || err.status === 403) {
          // auto logout if 401 response returned from api
          this.authenticationService.logout();
          if (
            this.authenticationService.currentAdminValue === true ||
            !this.env.ssoLoginUrl
          )
            this.router.navigate(["/login"]);
          else this.router.navigate(["/login-sso"]);

          //location.reload(true);
        }
        //console.log("----intercept error");
        var error = "errore";
        if (err) {
          //console.log(err);
          error =
            err.error && err.error.message
              ? err.error.message
              : err.statusText
              ? err.statusText
              : err.status;
        }
        return throwError(() => new Error(error));
      })
    );
  }
}
