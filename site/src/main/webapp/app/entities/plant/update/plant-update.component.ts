import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IPlant } from '../plant.model';
import { PlantService } from '../service/plant.service';
import { PlantFormService, PlantFormGroup } from './plant-form.service';

@Component({
  standalone: true,
  selector: 'jhi-plant-update',
  templateUrl: './plant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlantUpdateComponent implements OnInit {
  isSaving = false;
  plant: IPlant | null = null;

  sitesSharedCollection: ISite[] = [];

  editForm: PlantFormGroup = this.plantFormService.createPlantFormGroup();

  constructor(
    protected plantService: PlantService,
    protected plantFormService: PlantFormService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSite = (o1: ISite | null, o2: ISite | null): boolean => this.siteService.compareSite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plant }) => {
      this.plant = plant;
      if (plant) {
        this.updateForm(plant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plant = this.plantFormService.getPlant(this.editForm);
    if (plant.id !== null) {
      this.subscribeToSaveResponse(this.plantService.update(plant));
    } else {
      this.subscribeToSaveResponse(this.plantService.create(plant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlant>>): void {
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

  protected updateForm(plant: IPlant): void {
    this.plant = plant;
    this.plantFormService.resetForm(this.editForm, plant);

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing<ISite>(this.sitesSharedCollection, plant.site);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing<ISite>(sites, this.plant?.site)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }
}
