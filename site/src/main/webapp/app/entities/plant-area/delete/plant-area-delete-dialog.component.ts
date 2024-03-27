import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlantArea } from '../plant-area.model';
import { PlantAreaService } from '../service/plant-area.service';

@Component({
  standalone: true,
  templateUrl: './plant-area-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlantAreaDeleteDialogComponent {
  plantArea?: IPlantArea;

  constructor(
    protected plantAreaService: PlantAreaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plantAreaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
