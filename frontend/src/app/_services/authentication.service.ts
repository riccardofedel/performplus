import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { environment } from "../../environments/environment";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { first } from "rxjs/internal/operators/first";
import { Router } from "@angular/router";
import { User } from "../_models/user";
import Swal from "sweetalert2";
import { AlberoProgrammazioneService, ConsuntivazioneService } from ".";
import { UserOperator } from "../_models/operator";

@Injectable({ providedIn: "root" })
export class AuthenticationService {
  private currentUserSubject = new BehaviorSubject<User>(
    this.getUserInitialValue()
  );
  // public currentUser!: Observable<User>;
  private currentTokenSubject = new BehaviorSubject<string>(
    this.getTokenInitialValue()
  );

  private currentAdminSubject = new BehaviorSubject<boolean>(
    this.getAdminInitialValue()
  );

  env = environment;

  constructor(
    private http: HttpClient,
    private router: Router,
    private alberoProgrammazioneService: AlberoProgrammazioneService
  ) {}

  getAdminInitialValue() {
    const admin = localStorage.getItem("admin");
    if (admin === "true") {
      return true;
    }
    return false;
  }
  getUserInitialValue() {
    const user = localStorage.getItem("currentUser");
    if (user) {
      return JSON.parse(user);
    }
    return null;
  }
  getTokenInitialValue(): string {
    const token = localStorage.getItem("token");
    return token ? token : "";
  }
  get currentAdmin() {
    return this.currentAdminSubject.asObservable();
  }
  get currentToken() {
    return this.currentTokenSubject.asObservable();
  }
  public get currentTokenValue(): string {
    return this.currentTokenSubject?.value;
  }
  get currentUser() {
    return this.currentUserSubject.asObservable();
  }
  public get currentAdminValue(): boolean {
    return this.currentAdminSubject?.value;
  }
  public get currentUserValue(): User {
    return this.currentUserSubject?.value;
  }
  admin(t:boolean){
        localStorage.setItem("admin", t+"");
        this.currentAdminSubject.next(t);
  }
  tkn(token: string) {
    localStorage.setItem("tkn", token);
    this.currentTokenSubject.next(token);
  }
  updateUser(user:any){
    this.currentUserSubject.next(user);
  }
  sso() {
    const token = this.currentTokenSubject?.value;
    if (!token) {
      this.logout();
      this.router.navigate(["/login-sso"], {
        queryParams: { status: "anonimo" },
      });
      return;
    }
    const username = "anonymous";
    const password = token;
    return this.http
      .post<any>(this.env.API_URL + `auth/logintkn`, {
        username: username,
        password: password,
      })
      .subscribe((user) => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem("currentUser", JSON.stringify(user));
        this.currentUserSubject.next(user);
        
        //devo rinfrescare i ruoli
        this.getWhoami(
         user?.operator?.idEnte?user?.operator?.idEnte:0,
          user?.operator?.anno
            ? user?.operator?.anno
            : new Date().getFullYear(),
          user?.token
        ).subscribe((result) => {
          if (result.status === "ACTIVE") {
            user.operator = result;
            localStorage.setItem("currentUser", JSON.stringify(user));
            this.currentUserSubject.next(user);
            this.router.navigate(["/"]);
          }
          if (result.status === "DEACTIVE") {
            Swal.fire({
              title: "Mi spiace",
              text: "Utente non attivo",
              icon: "error",
              confirmButtonText: "Ok",
            });
            this.logout();
            this.router.navigate(["/login-sso"], {
              queryParams: { status: result.status.toLowerCase() },
            });
          }
          if (result.status === "PENDING") {
            Swal.fire({
              title: "Mi spiace",
              text: "Utente in stato pending",
              icon: "error",
              confirmButtonText: "Ok",
            });
            this.logout();
            this.router.navigate(["/login-sso"], {
              queryParams: { status: result.status.toLowerCase() },
            });
          }
        });
      });
  }
  login(username: any, password: any) {
    return this.http
      .post<any>(this.env.API_URL + `auth/login`, { username, password })
      .pipe(
        map((user) => {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem("currentUser", JSON.stringify(user));
          this.currentUserSubject.next(user);

          //devo rinfrescare i ruoli
          this.getWhoami(
            user?.operator?.idEnte?user?.operator?.idEnte:0,
            user.operator?.anno
              ? user.operator?.anno
              : new Date().getFullYear(),
            user.token
          ).subscribe((result) => {
            if (result.status === "ACTIVE") {
              user.operator = result;
              localStorage.setItem("currentUser", JSON.stringify(user));
              this.currentUserSubject.next(user);
              //va tutto bene, continuiamo
            }
            if (result.status === "DEACTIVE") {
              Swal.fire({
                title: "Mi spiace",
                text: "Utente non attivo",
                icon: "error",
                confirmButtonText: "Ok",
              });
              this.logout();
              this.router.navigate(["/login-sso"], {
                queryParams: { status: result.status.toLowerCase() },
              });
            }
            if (result.status === "PENDING") {
              Swal.fire({
                title: "Mi spiace",
                text: "Utente in stato pending",
                icon: "error",
                confirmButtonText: "Ok",
              });
              this.logout();
              this.router.navigate(["/login-sso"], {
                queryParams: { status: result.status.toLowerCase() },
              });
            }
          });

          return user;
        })
      );
  }

  // HttpClient API get() method => Whoami
  getWhoami(
    idEnte: number,
    anno: number,
    token: string | undefined
  ) {
    //console.log("-----getWhoami",anno);
    return this.http.get<any>(this.env.API_URL + "auth/whoami?idEnte="+idEnte+"&anno="+anno, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  logout() {
    this.alberoProgrammazioneService.initializeClosed();
    localStorage.removeItem("currentUser");
    this.currentUserSubject.next(null as any);
    localStorage.removeItem("tkn");
    this.currentTokenSubject.next(null as any);
    localStorage.setItem("admin","false");
    this.currentAdminSubject.next(false);
  }

  aggiornaDatiUtente(user: User) {
    //devo rinfrescare i ruoli
    return this.getWhoami(
      user?.operator?.idEnte?user?.operator?.idEnte:0,
      user?.operator?.anno ? user?.operator?.anno : new Date().getFullYear(),
      user?.token
    ).pipe(
      map((result) => {
        //console.log("aggiornaDatiUtente", result);
        if (result.status === "ACTIVE") {
          if (user) {
            user.operator = result;
            localStorage.setItem("currentUser", JSON.stringify(user));
          } else {
            let tmp = { username: result.code, token: "", operator: result };
            localStorage.setItem("currentUser", JSON.stringify(tmp));
          }
        } else {
          localStorage.removeItem("currentUser");
        }
        // this.setUserData();
        return result;
      })
    );
  }
  verificaUtente(user: User) {
    //devo rinfrescare i ruoli
    //console.log("verificaUtente user:", user);
    return this.getWhoami(
      user?.operator?.idEnte?user?.operator?.idEnte:0,
      user?.operator?.anno ? user?.operator?.anno : new Date().getFullYear(),
      user?.token
    ).pipe(
      map((result) => {
        return result;
      })
    );
  }
}
