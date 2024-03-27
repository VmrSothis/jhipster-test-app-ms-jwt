import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPlantArea } from 'app/entities/plant-area/plant-area.model';
import { PlantAreaService } from 'app/entities/plant-area/service/plant-area.service';
import { IMachineModel } from 'app/entities/machine-model/machine-model.model';
import { MachineModelService } from 'app/entities/machine-model/service/machine-model.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IMachine } from '../machine.model';
import { MachineService } from '../service/machine.service';
import { MachineFormService } from './machine-form.service';

import { MachineUpdateComponent } from './machine-update.component';

describe('Machine Management Update Component', () => {
  let comp: MachineUpdateComponent;
  let fixture: ComponentFixture<MachineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let machineFormService: MachineFormService;
  let machineService: MachineService;
  let plantAreaService: PlantAreaService;
  let machineModelService: MachineModelService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MachineUpdateComponent],
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
      .overrideTemplate(MachineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MachineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    machineFormService = TestBed.inject(MachineFormService);
    machineService = TestBed.inject(MachineService);
    plantAreaService = TestBed.inject(PlantAreaService);
    machineModelService = TestBed.inject(MachineModelService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PlantArea query and add missing value', () => {
      const machine: IMachine = { id: 456 };
      const plantArea: IPlantArea = { id: 1795 };
      machine.plantArea = plantArea;

      const plantAreaCollection: IPlantArea[] = [{ id: 996 }];
      jest.spyOn(plantAreaService, 'query').mockReturnValue(of(new HttpResponse({ body: plantAreaCollection })));
      const additionalPlantAreas = [plantArea];
      const expectedCollection: IPlantArea[] = [...additionalPlantAreas, ...plantAreaCollection];
      jest.spyOn(plantAreaService, 'addPlantAreaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      expect(plantAreaService.query).toHaveBeenCalled();
      expect(plantAreaService.addPlantAreaToCollectionIfMissing).toHaveBeenCalledWith(
        plantAreaCollection,
        ...additionalPlantAreas.map(expect.objectContaining),
      );
      expect(comp.plantAreasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MachineModel query and add missing value', () => {
      const machine: IMachine = { id: 456 };
      const machineModel: IMachineModel = { id: 27248 };
      machine.machineModel = machineModel;

      const machineModelCollection: IMachineModel[] = [{ id: 3962 }];
      jest.spyOn(machineModelService, 'query').mockReturnValue(of(new HttpResponse({ body: machineModelCollection })));
      const additionalMachineModels = [machineModel];
      const expectedCollection: IMachineModel[] = [...additionalMachineModels, ...machineModelCollection];
      jest.spyOn(machineModelService, 'addMachineModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      expect(machineModelService.query).toHaveBeenCalled();
      expect(machineModelService.addMachineModelToCollectionIfMissing).toHaveBeenCalledWith(
        machineModelCollection,
        ...additionalMachineModels.map(expect.objectContaining),
      );
      expect(comp.machineModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const machine: IMachine = { id: 456 };
      const organization: IOrganization = { id: 27464 };
      machine.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 27964 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const machine: IMachine = { id: 456 };
      const plantArea: IPlantArea = { id: 9766 };
      machine.plantArea = plantArea;
      const machineModel: IMachineModel = { id: 13364 };
      machine.machineModel = machineModel;
      const organization: IOrganization = { id: 6515 };
      machine.organization = organization;

      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      expect(comp.plantAreasSharedCollection).toContain(plantArea);
      expect(comp.machineModelsSharedCollection).toContain(machineModel);
      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.machine).toEqual(machine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachine>>();
      const machine = { id: 123 };
      jest.spyOn(machineFormService, 'getMachine').mockReturnValue(machine);
      jest.spyOn(machineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machine }));
      saveSubject.complete();

      // THEN
      expect(machineFormService.getMachine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(machineService.update).toHaveBeenCalledWith(expect.objectContaining(machine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachine>>();
      const machine = { id: 123 };
      jest.spyOn(machineFormService, 'getMachine').mockReturnValue({ id: null });
      jest.spyOn(machineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machine }));
      saveSubject.complete();

      // THEN
      expect(machineFormService.getMachine).toHaveBeenCalled();
      expect(machineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachine>>();
      const machine = { id: 123 };
      jest.spyOn(machineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(machineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlantArea', () => {
      it('Should forward to plantAreaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(plantAreaService, 'comparePlantArea');
        comp.comparePlantArea(entity, entity2);
        expect(plantAreaService.comparePlantArea).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMachineModel', () => {
      it('Should forward to machineModelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(machineModelService, 'compareMachineModel');
        comp.compareMachineModel(entity, entity2);
        expect(machineModelService.compareMachineModel).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrganization', () => {
      it('Should forward to organizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationService, 'compareOrganization');
        comp.compareOrganization(entity, entity2);
        expect(organizationService.compareOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
