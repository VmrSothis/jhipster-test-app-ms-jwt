import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../machine-documentation.test-samples';

import { MachineDocumentationFormService } from './machine-documentation-form.service';

describe('MachineDocumentation Form Service', () => {
  let service: MachineDocumentationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MachineDocumentationFormService);
  });

  describe('Service methods', () => {
    describe('createMachineDocumentationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMachineDocumentationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            url: expect.any(Object),
            machine: expect.any(Object),
          }),
        );
      });

      it('passing IMachineDocumentation should create a new form with FormGroup', () => {
        const formGroup = service.createMachineDocumentationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            url: expect.any(Object),
            machine: expect.any(Object),
          }),
        );
      });
    });

    describe('getMachineDocumentation', () => {
      it('should return NewMachineDocumentation for default MachineDocumentation initial value', () => {
        const formGroup = service.createMachineDocumentationFormGroup(sampleWithNewData);

        const machineDocumentation = service.getMachineDocumentation(formGroup) as any;

        expect(machineDocumentation).toMatchObject(sampleWithNewData);
      });

      it('should return NewMachineDocumentation for empty MachineDocumentation initial value', () => {
        const formGroup = service.createMachineDocumentationFormGroup();

        const machineDocumentation = service.getMachineDocumentation(formGroup) as any;

        expect(machineDocumentation).toMatchObject({});
      });

      it('should return IMachineDocumentation', () => {
        const formGroup = service.createMachineDocumentationFormGroup(sampleWithRequiredData);

        const machineDocumentation = service.getMachineDocumentation(formGroup) as any;

        expect(machineDocumentation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMachineDocumentation should not enable id FormControl', () => {
        const formGroup = service.createMachineDocumentationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMachineDocumentation should disable id FormControl', () => {
        const formGroup = service.createMachineDocumentationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
