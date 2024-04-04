import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { RouterOutlet, Router, ActivatedRoute } from '@angular/router';
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


@Component({
  selector: 'jhi-main',
  standalone: true,
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent, SideNavbarComponent, BreadcrumbBarComponent, CommonModule, MatDialogModule ],
})
export default class MainComponent implements OnInit {
  isMainPage: boolean = false;
  private renderer: Renderer2;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private appPageTitleStrategy: AppPageTitleStrategy,
    private accountService: AccountService,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();
    this.isMainPage = (this.router.url === '/');

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
}
