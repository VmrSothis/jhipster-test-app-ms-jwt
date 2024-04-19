import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import LoginComponent from 'app/login/login.component';
import { DashboardComponent } from 'app/layouts/dashboard/dashboard.component';
import { DxChartModule, DxSelectBoxModule } from 'devextreme-angular';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule, LoginComponent, DashboardComponent, DxChartModule, DxSelectBoxModule,],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  isLoading: boolean = true;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
  ) { }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.isLoading = false;
        this.account = account;
        console.error(account);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
