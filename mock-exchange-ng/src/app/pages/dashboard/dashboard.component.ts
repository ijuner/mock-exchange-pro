// src/app/pages/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

/**
 * Dashboard:
 * - call /me to render user
 * - demo fetching orders and render with *ngFor
 */
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  user?: { id: string; username: string; email: string };
  orders: any[] = [];

  constructor(private auth: AuthService, private http: HttpClient) {}

  ngOnInit(): void {
    // Load current user
    this.auth.me().subscribe((u) => (this.user = u));

    // Demo: load orders from order-service (replace BASE)
    const ORDERS_BASE = 'https://order-service-xxx.a.run.app/api/orders';
    this.http.get<any[]>(`${ORDERS_BASE}?page=0&size=10`).subscribe((list) => (this.orders = list));
  }

  logout(): void {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
}
