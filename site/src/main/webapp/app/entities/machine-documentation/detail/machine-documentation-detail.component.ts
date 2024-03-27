import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMachineDocumentation } from '../machine-documentation.model';

@Component({
  standalone: true,
  selector: 'jhi-machine-documentation-detail',
  templateUrl: './machine-documentation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MachineDocumentationDetailComponent {
  @Input() machineDocumentation: IMachineDocumentation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
