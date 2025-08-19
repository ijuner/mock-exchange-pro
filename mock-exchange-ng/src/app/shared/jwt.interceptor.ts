// src/app/shared/jwt.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

// Attach JWT from localStorage to Authorization header
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Read token from localStorage (you may switch to cookie if needed)
    const token = localStorage.getItem('token');
    if (token) {
      // Clone request and add Authorization header
      const authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}
