import { Component, OnInit } from '@angular/core';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT } from 'app/config/navigation.constants';
import { ItemCountComponent } from 'app/shared/pagination';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import UserManagementDeleteDialogComponent from '../delete/user-management-delete-dialog.component';
import { ModalInfoComponent } from 'app/layouts/modals/modal-info/modal-info.component';
import { MatDialog, MatDialogConfig, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UserInfoModalComponent } from 'app/layouts/modals/user-info-modal/user-info-modal.component';

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
  imports: [RouterModule, SharedModule, MatDialogModule, SortDirective, SortByDirective, UserManagementDeleteDialogComponent, ItemCountComponent ],
})
export default class UserManagementComponent implements OnInit {
  currentAccount: Account | null = null;
  users: User[] | null = null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  dialogConfig = new MatDialogConfig();

  constructor(
    public dialog: MatDialog,
    private userService: UserManagementService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.handleNavigation();
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(_index: number, item: User): number {
    return item.id!;
  }

  openUserModal(user: User | null, modalType: string): void {
    this.dialogConfig.data = {
      modalType,      
      userdata: user
    };

    const dialogRef: MatDialogRef<UserInfoModalComponent, any> = this.dialog.open(UserInfoModalComponent, this.dialogConfig);
  
    dialogRef.afterClosed().subscribe(result => {
      console.error(result);
      this.loadAll();
      if (result.operation === 'delete') {
        // aquí dentro se gestiona la siguiente accion en caso de pulsar 'OK' en la modal
        if(user)
        this.deleteUser(user); //comentado hasta tener claro cómo borrar el user 
      }
    });
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserInfoModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'delete') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading = true;
    this.userService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers);
        },
        error: () => (this.isLoading = false),
      });
  }

  transition(): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: `${this.predicate},${this.ascending ? ASC : DESC}`,
      },
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ModalInfoComponent);

    dialogRef.afterClosed().subscribe(result => {
      console.error(`Dialog result: ${result}`);
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      this.predicate = sort[0];
      this.ascending = sort[1] === ASC;
      this.loadAll();
    });
  }

  private sort(): string[] {
    const result = [`${this.predicate},${this.ascending ? ASC : DESC}`];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(users: User[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.users = users;
  }

}
