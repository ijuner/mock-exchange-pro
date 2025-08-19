// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

//  Cloud Run URL
const BASE = 'https://auth-service-xxxx.a.run.app/api/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}
  // Call backend login endpoint, expect { token: string }
  login(email: string, password: string) {
    return this.http.post<{ token: string }>(`${BASE}/login`, { email, password });
  }
  // Get current user profile using JWT (interceptor adds the token)
  me() {
    return this.http.get<{ id: string; username: string; email: string }>(`${BASE}/me`);
  }
}
