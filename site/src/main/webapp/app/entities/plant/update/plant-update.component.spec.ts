import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { PlantService } from '../service/plant.service';
import { IPlant } from '../plant.model';
import { PlantFormService } from './plant-form.service';

import { PlantUpdateComponent } from './plant-update.component';

describe('Plant Management Update Component', () => {
  let comp: PlantUpdateComponent;
  let fixture: ComponentFixture<PlantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let plantFormService: PlantFormService;
  let plantService: PlantService;
  let siteService: SiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PlantUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    plantFormService = TestBed.inject(PlantFormService);
    plantService = TestBed.inject(PlantService);
    siteService = TestBed.inject(SiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Site query and add missing value', () => {
      const plant: IPlant = { id: 456 };
      const site: ISite = { id: 4245 };
      plant.site = site;

      const siteCollection: ISite[] = [{ id: 15863 }];
      jest.spyOn(siteService, 'query').mockReturnValue(of(new HttpResponse({ body: siteCollection })));
      const additionalSites = [site];
      const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
      jest.spyOn(siteService, 'addSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ plant });
      comp.ngOnInit();

      expect(siteService.query).toHaveBeenCalled();
      expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(
        siteCollection,
        ...additionalSites.map(expect.objectContaining),
      );
      expect(comp.sitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const plant: IPlant = { id: 456 };
      const site: ISite = { id: 30711 };
      plant.site = site;

      activatedRoute.data = of({ plant });
      comp.ngOnInit();

      expect(comp.sitesSharedCollection).toContain(site);
      expect(comp.plant).toEqual(plant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlant>>();
      const plant = { id: 123 };
      jest.spyOn(plantFormService, 'getPlant').mockReturnValue(plant);
      jest.spyOn(plantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plant }));
      saveSubject.complete();

      // THEN
      expect(plantFormService.getPlant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(plantService.update).toHaveBeenCalledWith(expect.objectContaining(plant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlant>>();
      const plant = { id: 123 };
      jest.spyOn(plantFormService, 'getPlant').mockReturnValue({ id: null });
      jest.spyOn(plantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plant }));
      saveSubject.complete();

      // THEN
      expect(plantFormService.getPlant).toHaveBeenCalled();
      expect(plantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlant>>();
      const plant = { id: 123 };
      jest.spyOn(plantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(plantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSite', () => {
      it('Should forward to siteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(siteService, 'compareSite');
        comp.compareSite(entity, entity2);
        expect(siteService.compareSite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
