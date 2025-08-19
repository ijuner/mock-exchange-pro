// src/app/pages/login/login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Login page:
 * - Two-way binding [(ngModel)] for form inputs
 * - *ngIf to show error
 * - DI of AuthService to call backend API
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    this.error = '';
    if (!this.email || !this.password) {
      this.error = 'Email and password are required';
      return;
    }
    this.auth.login(this.email, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        this.router.navigateByUrl('/dashboard');
      },
      error: () => (this.error = 'Login failed')
    });
  }
}
