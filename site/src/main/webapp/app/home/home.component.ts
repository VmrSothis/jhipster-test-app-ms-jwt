import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import LoginComponent from 'app/login/login.component';
import { DashboardComponent } from 'app/dashboard/dashboard.component';
import { DxChartModule, DxSelectBoxModule } from 'devextreme-angular';

interface СorporationInfo {
  company: string;
  y2005: number;
  y2004: number;
}

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule, LoginComponent, DashboardComponent, DxChartModule, DxSelectBoxModule,],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  populationData = [{
    arg: 1960,
    val: 3032019978,
  }, {
    arg: 1965,
    val: 3683676306,
  }, {
    arg: 1970,
    val: 4434021975,
  }, {
    arg: 1975,
    val: 5281340078,
  }, {
    arg: 1980,
    val: 6115108363,
  }, {
    arg: 1985,
    val: 6922947261,
  }, {
    arg: 1990,
    val: 7795000000,
  }, {
    arg: 1995,
    val: 7795000000,
  }
  , {
    arg: 2000,
    val: 7795000000,
  }
  , {
    arg: 2005,
    val: 7795000000,
  }
  , {
    arg: 2010,
    val: 7795000000,
  }
  ];

  corporationsInfo: СorporationInfo[] = [{
    company: 'ExxonMobil',
    y2005: 377.28,
    y2004: 270.25,
  }, {
    company: 'GeneralElectric',
    y2005: 353.49,
    y2004: 311.43,
  }, {
    company: 'Microsoft',
    y2005: 269.86,
    y2004: 273.32,
  }, {
    company: 'Citigroup',
    y2005: 252.95,
    y2004: 280.50,
  }, {
    company: 'Royal Dutch Shell plc',
    y2005: 197.67,
    y2004: 165.89,
  }, {
    company: 'Procted & Gamble',
    y2005: 184.72,
    y2004: 130.52,
  }];

  types: string[] = ['splinearea', 'stackedsplinearea', 'fullstackedsplinearea'];
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
