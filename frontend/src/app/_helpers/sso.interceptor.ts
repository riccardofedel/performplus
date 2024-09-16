import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../_services/authentication.service';
import { tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class SSOInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) { }
 
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap((httpEvent) => {
        if (httpEvent.type === 0) {
          return;
        }
        //console.log("response: ", httpEvent); 
        if (httpEvent instanceof HttpResponse) {
          //console.log("response keys: ", httpEvent.headers.keys); 
          if (httpEvent.headers.has("tkn")) {
            let token = httpEvent.headers.get("tkn");
            if (token) {
              this.authenticationService.tkn(token);
              this.authenticationService.sso();
            }
          }
        }
      }));
  }
}
