import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPlant } from 'app/entities/plant/plant.model';
import { PlantService } from 'app/entities/plant/service/plant.service';
import { PlantAreaService } from '../service/plant-area.service';
import { IPlantArea } from '../plant-area.model';
import { PlantAreaFormService } from './plant-area-form.service';

import { PlantAreaUpdateComponent } from './plant-area-update.component';

describe('PlantArea Management Update Component', () => {
  let comp: PlantAreaUpdateComponent;
  let fixture: ComponentFixture<PlantAreaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let plantAreaFormService: PlantAreaFormService;
  let plantAreaService: PlantAreaService;
  let plantService: PlantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PlantAreaUpdateComponent],
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
      .overrideTemplate(PlantAreaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlantAreaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    plantAreaFormService = TestBed.inject(PlantAreaFormService);
    plantAreaService = TestBed.inject(PlantAreaService);
    plantService = TestBed.inject(PlantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Plant query and add missing value', () => {
      const plantArea: IPlantArea = { id: 456 };
      const plant: IPlant = { id: 29208 };
      plantArea.plant = plant;

      const plantCollection: IPlant[] = [{ id: 4651 }];
      jest.spyOn(plantService, 'query').mockReturnValue(of(new HttpResponse({ body: plantCollection })));
      const additionalPlants = [plant];
      const expectedCollection: IPlant[] = [...additionalPlants, ...plantCollection];
      jest.spyOn(plantService, 'addPlantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ plantArea });
      comp.ngOnInit();

      expect(plantService.query).toHaveBeenCalled();
      expect(plantService.addPlantToCollectionIfMissing).toHaveBeenCalledWith(
        plantCollection,
        ...additionalPlants.map(expect.objectContaining),
      );
      expect(comp.plantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const plantArea: IPlantArea = { id: 456 };
      const plant: IPlant = { id: 5466 };
      plantArea.plant = plant;

      activatedRoute.data = of({ plantArea });
      comp.ngOnInit();

      expect(comp.plantsSharedCollection).toContain(plant);
      expect(comp.plantArea).toEqual(plantArea);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlantArea>>();
      const plantArea = { id: 123 };
      jest.spyOn(plantAreaFormService, 'getPlantArea').mockReturnValue(plantArea);
      jest.spyOn(plantAreaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantArea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plantArea }));
      saveSubject.complete();

      // THEN
      expect(plantAreaFormService.getPlantArea).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(plantAreaService.update).toHaveBeenCalledWith(expect.objectContaining(plantArea));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlantArea>>();
      const plantArea = { id: 123 };
      jest.spyOn(plantAreaFormService, 'getPlantArea').mockReturnValue({ id: null });
      jest.spyOn(plantAreaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantArea: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plantArea }));
      saveSubject.complete();

      // THEN
      expect(plantAreaFormService.getPlantArea).toHaveBeenCalled();
      expect(plantAreaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlantArea>>();
      const plantArea = { id: 123 };
      jest.spyOn(plantAreaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantArea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(plantAreaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlant', () => {
      it('Should forward to plantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(plantService, 'comparePlant');
        comp.comparePlant(entity, entity2);
        expect(plantService.comparePlant).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
