import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { User } from '../_models';
import { AuthenticationService } from '.';

export enum Role {
  Amministratore = 'AMMINISTRATORE',
  Risorsa = 'RISORSA',
  SuppertoSistema = 'SUPPORTO_SISTEMA',
  DirettoreGenerale = 'DIRETTORE_GENERALE',
  PosizioneOrganizzativa = 'POSIZIONE_ORGANIZZATIVA',
  Referente = 'REFERENTE',
  OIV = 'OIV'
}

export enum Permission
    {
        Sistema = "SISTEMA",
        Struttura = "STRUTTURA",
        AmministratoriStruttura = "STRUTTURA.AMMINISTATORI",
        RisorseStruttura = "STRUTTURA.RISORSE",
        OrganigrammaStruttura = "STRUTTURA.ORGANIGRAMMA",
        Dup = "DUP",
        DupIntroduzione = "DUP.INTRODUZIONE",
        DupProgrammazione = "DUP.PROGRAMMAZIONE",
        DupConsuntivazione = "DUP.CONSUNTIVAZIONE",
        DupGestioneAzioni = "DUP.GESTIONE_AZIONI",
        RisorseProgrammazione = "DUP.PROGRAMMAZIONE.RISORSE",
        Stampe = "STAMPE",
        Performance = "DUP",
        Regolamenti = "REGOLAMENTI",
        Questionari = "QUESTIONARI",
      }

@Injectable({
  providedIn: "root",
})
export class PermissionService {
  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService
  ) {}

  env = environment;

  isAdmin(user: User | undefined) {
    return (
      user?.operator?.ruolo === Role.Amministratore ||
      user?.operator?.ruolo === Role.SuppertoSistema
    );
  }

  // isAudit(user: User | undefined) {
  //   return (user?.operator?.ruolo===Role.Audit);
  // }

  hasRole(user: User | undefined, ruolo: Role) {
    return user?.operator?.ruolo === ruolo;
  }

  annoCorrente(user: User | undefined) {
    return user?.operator?.anno;
  }

  isAnno(user: User, anno: number) {
    return user?.operator?.anno === anno;
  }

  cambiaAnno(anno: number, user: User | undefined) {
    //console.log("CAMBIOOOO",anno,user,user?.operator);
    if (user && user.operator) {
      user.operator.anno = anno;
      localStorage.setItem("currentUser", JSON.stringify(user));
      this.authenticationService.updateUser(user);
    }
  }

  getPermission(user: User | undefined, permission: string, read: boolean) {
    let enablings = user?.operator?.enablings;

    const permissionFirst = permission.split(".")[0];
    const enablingpermissionFirsting = enablings?.find(
      (val) => val.name == permissionFirst
    );
    if (
      enablingpermissionFirsting &&
      read &&
      !enablingpermissionFirsting?.read
    ) {
      return false;
    }
    if (
      enablingpermissionFirsting &&
      !read &&
      !enablingpermissionFirsting?.write
    ) {
      return false;
    }

    const enabling = enablings?.find((val) => val.name == permission);
    if (enabling && read) {
      return enabling?.read;
    } else {
      return enabling?.write;
    }
  }
}



