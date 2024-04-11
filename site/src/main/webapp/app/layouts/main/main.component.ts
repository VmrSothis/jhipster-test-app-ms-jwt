import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { RouterOutlet, Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { SideNavbarComponent } from '../side-navbar/side-navbar.component';
import { BreadcrumbBarComponent } from '../breadcrumb-bar/breadcrumb-bar.component';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ModalInfoComponent } from '../modals/modal-info/modal-info.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { distinctUntilChanged, filter, map } from 'rxjs';


@Component({
  selector: 'jhi-main',
  standalone: true,
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent, SideNavbarComponent, BreadcrumbBarComponent, CommonModule, MatDialogModule ],
})
export default class MainComponent implements OnInit {
  isMainPage: boolean = false;
  Breakpoints = Breakpoints;
  currentBreakpoint:string = '';
  isMobile: boolean = true;
  readonly breakpoint$ = this.breakpointObserver
    .observe([Breakpoints.Large, Breakpoints.Medium, Breakpoints.Small, '(min-width: 500px)'])
    .pipe(
      distinctUntilChanged()
    );
  private renderer: Renderer2;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private appPageTitleStrategy: AppPageTitleStrategy,
    private accountService: AccountService,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    
    this.router.events.subscribe(res => {
      if(res instanceof NavigationEnd){ 
        this.isMainPage = (this.router.url.toString() === "/")
      }
    });
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();
    console.error(this.isMainPage)
    this.breakpoint$.subscribe(() =>
      this.breakpointChanged()
    );

    console.error(this.router.url);

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ModalInfoComponent);

    dialogRef.afterClosed().subscribe(result => {
      console.error(`Dialog result: ${result}`);
    });
  }

  private breakpointChanged(): void {
    this.breakpointObserver.observe('(max-width: 640px)').subscribe(result => {
      this.isMobile = result.matches;
    })
  }
}
