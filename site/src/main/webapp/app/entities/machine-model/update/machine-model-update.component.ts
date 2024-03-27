import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMachineModel } from '../machine-model.model';
import { MachineModelService } from '../service/machine-model.service';
import { MachineModelFormService, MachineModelFormGroup } from './machine-model-form.service';

@Component({
  standalone: true,
  selector: 'jhi-machine-model-update',
  templateUrl: './machine-model-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MachineModelUpdateComponent implements OnInit {
  isSaving = false;
  machineModel: IMachineModel | null = null;

  editForm: MachineModelFormGroup = this.machineModelFormService.createMachineModelFormGroup();

  constructor(
    protected machineModelService: MachineModelService,
    protected machineModelFormService: MachineModelFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machineModel }) => {
      this.machineModel = machineModel;
      if (machineModel) {
        this.updateForm(machineModel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machineModel = this.machineModelFormService.getMachineModel(this.editForm);
    if (machineModel.id !== null) {
      this.subscribeToSaveResponse(this.machineModelService.update(machineModel));
    } else {
      this.subscribeToSaveResponse(this.machineModelService.create(machineModel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachineModel>>): void {
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

  protected updateForm(machineModel: IMachineModel): void {
    this.machineModel = machineModel;
    this.machineModelFormService.resetForm(this.editForm, machineModel);
  }
}
