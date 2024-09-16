import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './_helpers/auth.guard';
import { environment } from 'src/environments/environment';
import { LoginSSOComponent } from './login-sso/login-sso.component';
import { LogoutComponent } from './logout/logout.component';
const routes: Routes = [
  { path: "login", component: LoginComponent },
  { path: "logout", component: LogoutComponent },
  { path: "admin", redirectTo: "login", pathMatch: "full" },
  { path: "login-sso", component: LoginSSOComponent },
  { path: "error", redirectTo: "login-sso", pathMatch: "full" },
  { path: "home", component: HomeComponent, canActivate: [AuthGuard] },
  { path: "", redirectTo: "home", pathMatch: "full" },
  {
    path: "sistema",
    loadChildren: () =>
      import("./sistema/sistema.module").then((m) => m.SistemaModule),
  },
  {
    path: "struttura",
    loadChildren: () =>
      import("./struttura/struttura.module").then((m) => m.StrutturaModule),
  },
  {
    path: "programmazione",
    loadChildren: () =>
      import("./programmazione/programmazione.module").then(
        (m) => m.ProgrammazioneModule
      ),
  },
  {
    path: "stampe",
    loadChildren: () =>
      import("./stampe/stampe.module").then((m) => m.StampeModule),
  },
  {
    path: "performance",
    loadChildren: () =>
      import("./performance/performance.module").then(
        (m) => m.PerformanceModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
