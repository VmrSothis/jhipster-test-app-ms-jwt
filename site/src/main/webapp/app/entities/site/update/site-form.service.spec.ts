import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../site.test-samples';

import { SiteFormService } from './site-form.service';

describe('Site Form Service', () => {
  let service: SiteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiteFormService);
  });

  describe('Service methods', () => {
    describe('createSiteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSiteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            email: expect.any(Object),
            telephone: expect.any(Object),
            address: expect.any(Object),
            postalCode: expect.any(Object),
            region: expect.any(Object),
            locality: expect.any(Object),
            country: expect.any(Object),
            location: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });

      it('passing ISite should create a new form with FormGroup', () => {
        const formGroup = service.createSiteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            email: expect.any(Object),
            telephone: expect.any(Object),
            address: expect.any(Object),
            postalCode: expect.any(Object),
            region: expect.any(Object),
            locality: expect.any(Object),
            country: expect.any(Object),
            location: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });
    });

    describe('getSite', () => {
      it('should return NewSite for default Site initial value', () => {
        const formGroup = service.createSiteFormGroup(sampleWithNewData);

        const site = service.getSite(formGroup) as any;

        expect(site).toMatchObject(sampleWithNewData);
      });

      it('should return NewSite for empty Site initial value', () => {
        const formGroup = service.createSiteFormGroup();

        const site = service.getSite(formGroup) as any;

        expect(site).toMatchObject({});
      });

      it('should return ISite', () => {
        const formGroup = service.createSiteFormGroup(sampleWithRequiredData);

        const site = service.getSite(formGroup) as any;

        expect(site).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISite should not enable id FormControl', () => {
        const formGroup = service.createSiteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSite should disable id FormControl', () => {
        const formGroup = service.createSiteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
