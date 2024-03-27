import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';
import { AttachedType } from 'app/entities/enumerations/attached-type.model';
import { MachineDocumentationService } from '../service/machine-documentation.service';
import { IMachineDocumentation } from '../machine-documentation.model';
import { MachineDocumentationFormService, MachineDocumentationFormGroup } from './machine-documentation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-machine-documentation-update',
  templateUrl: './machine-documentation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MachineDocumentationUpdateComponent implements OnInit {
  isSaving = false;
  machineDocumentation: IMachineDocumentation | null = null;
  attachedTypeValues = Object.keys(AttachedType);

  machinesSharedCollection: IMachine[] = [];

  editForm: MachineDocumentationFormGroup = this.machineDocumentationFormService.createMachineDocumentationFormGroup();

  constructor(
    protected machineDocumentationService: MachineDocumentationService,
    protected machineDocumentationFormService: MachineDocumentationFormService,
    protected machineService: MachineService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMachine = (o1: IMachine | null, o2: IMachine | null): boolean => this.machineService.compareMachine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machineDocumentation }) => {
      this.machineDocumentation = machineDocumentation;
      if (machineDocumentation) {
        this.updateForm(machineDocumentation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machineDocumentation = this.machineDocumentationFormService.getMachineDocumentation(this.editForm);
    if (machineDocumentation.id !== null) {
      this.subscribeToSaveResponse(this.machineDocumentationService.update(machineDocumentation));
    } else {
      this.subscribeToSaveResponse(this.machineDocumentationService.create(machineDocumentation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachineDocumentation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(machineDocumentation: IMachineDocumentation): void {
    this.machineDocumentation = machineDocumentation;
    this.machineDocumentationFormService.resetForm(this.editForm, machineDocumentation);

    this.machinesSharedCollection = this.machineService.addMachineToCollectionIfMissing<IMachine>(
      this.machinesSharedCollection,
      machineDocumentation.machine,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.machineService
      .query()
      .pipe(map((res: HttpResponse<IMachine[]>) => res.body ?? []))
      .pipe(
        map((machines: IMachine[]) =>
          this.machineService.addMachineToCollectionIfMissing<IMachine>(machines, this.machineDocumentation?.machine),
        ),
      )
      .subscribe((machines: IMachine[]) => (this.machinesSharedCollection = machines));
  }
}
