import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlant } from 'app/entities/plant/plant.model';
import { PlantService } from 'app/entities/plant/service/plant.service';
import { IPlantArea } from '../plant-area.model';
import { PlantAreaService } from '../service/plant-area.service';
import { PlantAreaFormService, PlantAreaFormGroup } from './plant-area-form.service';

@Component({
  standalone: true,
  selector: 'jhi-plant-area-update',
  templateUrl: './plant-area-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlantAreaUpdateComponent implements OnInit {
  isSaving = false;
  plantArea: IPlantArea | null = null;

  plantsSharedCollection: IPlant[] = [];

  editForm: PlantAreaFormGroup = this.plantAreaFormService.createPlantAreaFormGroup();

  constructor(
    protected plantAreaService: PlantAreaService,
    protected plantAreaFormService: PlantAreaFormService,
    protected plantService: PlantService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePlant = (o1: IPlant | null, o2: IPlant | null): boolean => this.plantService.comparePlant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantArea }) => {
      this.plantArea = plantArea;
      if (plantArea) {
        this.updateForm(plantArea);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plantArea = this.plantAreaFormService.getPlantArea(this.editForm);
    if (plantArea.id !== null) {
      this.subscribeToSaveResponse(this.plantAreaService.update(plantArea));
    } else {
      this.subscribeToSaveResponse(this.plantAreaService.create(plantArea));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlantArea>>): void {
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

  protected updateForm(plantArea: IPlantArea): void {
    this.plantArea = plantArea;
    this.plantAreaFormService.resetForm(this.editForm, plantArea);

    this.plantsSharedCollection = this.plantService.addPlantToCollectionIfMissing<IPlant>(this.plantsSharedCollection, plantArea.plant);
  }

  protected loadRelationshipsOptions(): void {
    this.plantService
      .query()
      .pipe(map((res: HttpResponse<IPlant[]>) => res.body ?? []))
      .pipe(map((plants: IPlant[]) => this.plantService.addPlantToCollectionIfMissing<IPlant>(plants, this.plantArea?.plant)))
      .subscribe((plants: IPlant[]) => (this.plantsSharedCollection = plants));
  }
}
