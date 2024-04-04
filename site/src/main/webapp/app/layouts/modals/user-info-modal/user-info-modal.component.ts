import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { User } from 'app/admin/user-management/user-management.model';

@Component({
  selector: 'user-info-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './user-info-modal.component.html',
  styleUrl: './user-info-modal.component.scss'
})
export class UserInfoModalComponent {
  userSelected: User | null = null;
  modalType: string = 'view';

  constructor(private dialogRef: MatDialogRef<User>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.modalType = this.data.modalType ;
    this.userSelected = this.data.userdata;
  }
  
  confirm(): void {
    this.dialogRef.close({ option: 'confirm', user: this.data.userdata});
  }

}
