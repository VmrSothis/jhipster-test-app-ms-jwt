import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMachineDocumentation } from '../machine-documentation.model';
import { MachineDocumentationService } from '../service/machine-documentation.service';

@Component({
  standalone: true,
  templateUrl: './machine-documentation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MachineDocumentationDeleteDialogComponent {
  machineDocumentation?: IMachineDocumentation;

  constructor(
    protected machineDocumentationService: MachineDocumentationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.machineDocumentationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
