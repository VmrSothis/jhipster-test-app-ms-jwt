import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlant } from '../plant.model';
import { PlantService } from '../service/plant.service';

@Component({
  standalone: true,
  templateUrl: './plant-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlantDeleteDialogComponent {
  plant?: IPlant;

  constructor(
    protected plantService: PlantService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
