// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate, Router } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

class AuthGuard implements CanActivate {
  constructor(private router: Router) {}
  canActivate(): boolean {
    // Simple check: token exists in localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigateByUrl('/login');
      return false;
    }
    return true;
  }
}

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  providers: [AuthGuard],
  exports: [RouterModule]
})
export class AppRoutingModule {}
