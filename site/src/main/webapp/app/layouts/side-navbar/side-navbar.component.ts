import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from '../profiles/profile.service';
import { MatDialog, MatDialogConfig, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { ModalInfoComponent } from '../modals/modal-info/modal-info.component';
import { TranslateService } from '@ngx-translate/core'; // Se importa el servicio de traducción de ngx-translate

@Component({
  selector: 'jhi-side-navbar',
  standalone: true,
  imports: [FontAwesomeModule, MatDialogModule],
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.scss']
})
export class SideNavbarComponent implements OnInit {
  account: Account | null = null;
  inProduction?: boolean;
  dialogConfig = new MatDialogConfig();

  constructor(
    public dialog: MatDialog,
    private location: Location,
    private router: Router,
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private translateService: TranslateService // Se añade el servicio de traducción de ngx-translate
  ) { }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  navigate(route: string): void {
    this.router.navigateByUrl(route);
  }

  goBack(): void {
    this.location.back();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  confirmLogout(): void {
    //Se han añadido traducciones
    this.translateService.get(['Logout', 'questionLogOut']).subscribe(translations => {
      this.dialogConfig.data = {
        dialogTile: translations['Logout'],
        modalMessage: translations['questionLogOut']
      };

      const dialogRef: MatDialogRef<ModalInfoComponent, any> = this.dialog.open(ModalInfoComponent, this.dialogConfig);

      dialogRef.afterClosed().subscribe(result => {
        if (result?.confirmed) {
          this.logout();
        }
      });
    });
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
