import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'modal-info',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './modal-info.component.html',
  styleUrl: './modal-info.component.scss'
})
export class ModalInfoComponent {
  dialogTitle: string = '';
  contentMessage: string = '';

  constructor(private dialogRef: MatDialogRef<string>,@Inject(MAT_DIALOG_DATA) public data: any ) {
    this.dialogTitle = this.data?.dialogTile;
    this.contentMessage = this.data?.modalMessage;
  }

  close(confirmResponse: boolean): void {
    this.dialogRef.close({ confirmed: confirmResponse});
  }
}
