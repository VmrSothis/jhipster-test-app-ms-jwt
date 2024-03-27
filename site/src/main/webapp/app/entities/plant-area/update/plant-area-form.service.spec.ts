import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../plant-area.test-samples';

import { PlantAreaFormService } from './plant-area-form.service';

describe('PlantArea Form Service', () => {
  let service: PlantAreaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlantAreaFormService);
  });

  describe('Service methods', () => {
    describe('createPlantAreaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlantAreaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            plant: expect.any(Object),
          }),
        );
      });

      it('passing IPlantArea should create a new form with FormGroup', () => {
        const formGroup = service.createPlantAreaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            plant: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlantArea', () => {
      it('should return NewPlantArea for default PlantArea initial value', () => {
        const formGroup = service.createPlantAreaFormGroup(sampleWithNewData);

        const plantArea = service.getPlantArea(formGroup) as any;

        expect(plantArea).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlantArea for empty PlantArea initial value', () => {
        const formGroup = service.createPlantAreaFormGroup();

        const plantArea = service.getPlantArea(formGroup) as any;

        expect(plantArea).toMatchObject({});
      });

      it('should return IPlantArea', () => {
        const formGroup = service.createPlantAreaFormGroup(sampleWithRequiredData);

        const plantArea = service.getPlantArea(formGroup) as any;

        expect(plantArea).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlantArea should not enable id FormControl', () => {
        const formGroup = service.createPlantAreaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlantArea should disable id FormControl', () => {
        const formGroup = service.createPlantAreaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
