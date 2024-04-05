import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { IUser, User } from 'app/admin/user-management/user-management.model';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { LANGUAGES } from 'app/config/language.constants';
import SharedModule from 'app/shared/shared.module';

const userTemplate = {} as IUser;
const newUser: IUser = {
  langKey: 'es',
  activated: true,
} as IUser;

@Component({
    selector: 'user-info-modal',
    standalone: true,
    templateUrl: './user-info-modal.component.html',
    styleUrl: './user-info-modal.component.scss',
    imports: [SharedModule, MatDialogModule, MatButtonModule, FormsModule, ReactiveFormsModule]
})
export class UserInfoModalComponent {
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
  userSelected: User | null = null;
  modalType: string = 'view';
  editForm = new FormGroup({
    id: new FormControl(userTemplate.id),
    login: new FormControl(userTemplate.login, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    firstName: new FormControl(userTemplate.firstName, { validators: [Validators.maxLength(50)] }),
    lastName: new FormControl(userTemplate.lastName, { validators: [Validators.maxLength(50)] }),
    email: new FormControl(userTemplate.email, {
      nonNullable: true,
      validators: [Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    activated: new FormControl(userTemplate.activated ?? true, { nonNullable: true }),
    langKey: new FormControl(userTemplate.langKey, { nonNullable: true }),
    authorities: new FormControl(userTemplate.authorities, { nonNullable: true }),
  });

  constructor(private dialogRef: MatDialogRef<User>, @Inject(MAT_DIALOG_DATA) public data: any, private userService: UserManagementService,) {
    // se obtienen los valores necesarios en el template desde el objeto data que se recibe
    this.modalType = this.data.modalType ;
    this.userSelected = this.data.userdata;
    console.error(this.userSelected);

    if (this.userSelected) {
      this.editForm.reset(this.userSelected);
    } else {
      this.editForm.reset(newUser);
    }
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  save(): void {
    this.isSaving = true;
    const user = this.editForm.getRawValue();
    if (user.login) {
      this.userService.update(user).subscribe({
        next: () => {
          this.onSaveSuccess();
        },
        error: (err) => this.onSaveError(err),
        complete: () => {
          this.dialogRef.close({ confirmed: false });
        }
      });
    } else {
      this.userService.create(user).subscribe({
        next: () => this.onSaveSuccess(),
        error: (err) => this.onSaveError(err),
        complete: () => {
          this.dialogRef.close({ confirmed: false });
        }
      });
    }
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.dialogRef.close({ operation: this.data.modalType, confirmed: true, user: this.data.userdata });
  }

  private onSaveError(err: Error): void {
    console.error(err);
    
    this.isSaving = false;
    this.dialogRef.close({ operation: this.data.modalType, confirmed: false, user: this.data.userdata });
  }
}
