import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../_services/authentication.service';
import { tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) { }
 env=environment;
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    let currentUser = this.authenticationService.currentUserValue;
    if(currentUser && currentUser.token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${currentUser.token}`
          },
          withCredentials: false

        });
    } else {
      let currentToken = this.authenticationService.currentTokenValue;
      if(currentToken) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${currentToken}`
          },
          withCredentials: false

        });
      }
    }
    
    return next.handle(request);
    
  }
}
