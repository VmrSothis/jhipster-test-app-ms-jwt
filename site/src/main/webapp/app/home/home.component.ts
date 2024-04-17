import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import LoginComponent from 'app/login/login.component';
import { DashboardComponent } from 'app/dashboard/dashboard.component';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule, LoginComponent, DashboardComponent],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
  ) {
    router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const segments: string[] = [];
        let currentRoute: ActivatedRouteSnapshot = router.routerState.root.snapshot;
          const path = currentRoute.routeConfig?.path;
          if (path && path !== '') { // Filter out these empty paths
            segments.push(path);
          }
          currentRoute = currentRoute.children[0];
        
        const rawPath = segments.join('/');
        console.error(rawPath);
        // Outputs for example: pets/:petIdentifier/food/:foodIdentifier/edit
    
      }
      
      const currentRoute: ActivatedRouteSnapshot = router.routerState.root.snapshot;
      console.error(currentRoute);
    });
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
