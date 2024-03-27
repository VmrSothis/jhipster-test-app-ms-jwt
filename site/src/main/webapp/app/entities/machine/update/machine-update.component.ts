import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlantArea } from 'app/entities/plant-area/plant-area.model';
import { PlantAreaService } from 'app/entities/plant-area/service/plant-area.service';
import { IMachineModel } from 'app/entities/machine-model/machine-model.model';
import { MachineModelService } from 'app/entities/machine-model/service/machine-model.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { MachineService } from '../service/machine.service';
import { IMachine } from '../machine.model';
import { MachineFormService, MachineFormGroup } from './machine-form.service';

@Component({
  standalone: true,
  selector: 'jhi-machine-update',
  templateUrl: './machine-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MachineUpdateComponent implements OnInit {
  isSaving = false;
  machine: IMachine | null = null;

  plantAreasSharedCollection: IPlantArea[] = [];
  machineModelsSharedCollection: IMachineModel[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  editForm: MachineFormGroup = this.machineFormService.createMachineFormGroup();

  constructor(
    protected machineService: MachineService,
    protected machineFormService: MachineFormService,
    protected plantAreaService: PlantAreaService,
    protected machineModelService: MachineModelService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePlantArea = (o1: IPlantArea | null, o2: IPlantArea | null): boolean => this.plantAreaService.comparePlantArea(o1, o2);

  compareMachineModel = (o1: IMachineModel | null, o2: IMachineModel | null): boolean =>
    this.machineModelService.compareMachineModel(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machine }) => {
      this.machine = machine;
      if (machine) {
        this.updateForm(machine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machine = this.machineFormService.getMachine(this.editForm);
    if (machine.id !== null) {
      this.subscribeToSaveResponse(this.machineService.update(machine));
    } else {
      this.subscribeToSaveResponse(this.machineService.create(machine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachine>>): void {
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

  protected updateForm(machine: IMachine): void {
    this.machine = machine;
    this.machineFormService.resetForm(this.editForm, machine);

    this.plantAreasSharedCollection = this.plantAreaService.addPlantAreaToCollectionIfMissing<IPlantArea>(
      this.plantAreasSharedCollection,
      machine.plantArea,
    );
    this.machineModelsSharedCollection = this.machineModelService.addMachineModelToCollectionIfMissing<IMachineModel>(
      this.machineModelsSharedCollection,
      machine.machineModel,
    );
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      machine.organization,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.plantAreaService
      .query()
      .pipe(map((res: HttpResponse<IPlantArea[]>) => res.body ?? []))
      .pipe(
        map((plantAreas: IPlantArea[]) =>
          this.plantAreaService.addPlantAreaToCollectionIfMissing<IPlantArea>(plantAreas, this.machine?.plantArea),
        ),
      )
      .subscribe((plantAreas: IPlantArea[]) => (this.plantAreasSharedCollection = plantAreas));

    this.machineModelService
      .query()
      .pipe(map((res: HttpResponse<IMachineModel[]>) => res.body ?? []))
      .pipe(
        map((machineModels: IMachineModel[]) =>
          this.machineModelService.addMachineModelToCollectionIfMissing<IMachineModel>(machineModels, this.machine?.machineModel),
        ),
      )
      .subscribe((machineModels: IMachineModel[]) => (this.machineModelsSharedCollection = machineModels));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.machine?.organization),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
