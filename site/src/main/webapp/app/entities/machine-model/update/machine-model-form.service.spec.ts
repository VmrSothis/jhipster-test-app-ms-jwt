import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../machine-model.test-samples';

import { MachineModelFormService } from './machine-model-form.service';

describe('MachineModel Form Service', () => {
  let service: MachineModelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MachineModelFormService);
  });

  describe('Service methods', () => {
    describe('createMachineModelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMachineModelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            brandName: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            manufacurerName: expect.any(Object),
            version: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IMachineModel should create a new form with FormGroup', () => {
        const formGroup = service.createMachineModelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            brandName: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            manufacurerName: expect.any(Object),
            version: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getMachineModel', () => {
      it('should return NewMachineModel for default MachineModel initial value', () => {
        const formGroup = service.createMachineModelFormGroup(sampleWithNewData);

        const machineModel = service.getMachineModel(formGroup) as any;

        expect(machineModel).toMatchObject(sampleWithNewData);
      });

      it('should return NewMachineModel for empty MachineModel initial value', () => {
        const formGroup = service.createMachineModelFormGroup();

        const machineModel = service.getMachineModel(formGroup) as any;

        expect(machineModel).toMatchObject({});
      });

      it('should return IMachineModel', () => {
        const formGroup = service.createMachineModelFormGroup(sampleWithRequiredData);

        const machineModel = service.getMachineModel(formGroup) as any;

        expect(machineModel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMachineModel should not enable id FormControl', () => {
        const formGroup = service.createMachineModelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMachineModel should disable id FormControl', () => {
        const formGroup = service.createMachineModelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
