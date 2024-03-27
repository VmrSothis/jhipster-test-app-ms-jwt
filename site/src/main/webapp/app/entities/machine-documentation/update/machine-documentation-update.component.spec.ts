import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMachine } from 'app/entities/machine/machine.model';
import { MachineService } from 'app/entities/machine/service/machine.service';
import { MachineDocumentationService } from '../service/machine-documentation.service';
import { IMachineDocumentation } from '../machine-documentation.model';
import { MachineDocumentationFormService } from './machine-documentation-form.service';

import { MachineDocumentationUpdateComponent } from './machine-documentation-update.component';

describe('MachineDocumentation Management Update Component', () => {
  let comp: MachineDocumentationUpdateComponent;
  let fixture: ComponentFixture<MachineDocumentationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let machineDocumentationFormService: MachineDocumentationFormService;
  let machineDocumentationService: MachineDocumentationService;
  let machineService: MachineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MachineDocumentationUpdateComponent],
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
      .overrideTemplate(MachineDocumentationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MachineDocumentationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    machineDocumentationFormService = TestBed.inject(MachineDocumentationFormService);
    machineDocumentationService = TestBed.inject(MachineDocumentationService);
    machineService = TestBed.inject(MachineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Machine query and add missing value', () => {
      const machineDocumentation: IMachineDocumentation = { id: 456 };
      const machine: IMachine = { id: 16687 };
      machineDocumentation.machine = machine;

      const machineCollection: IMachine[] = [{ id: 31948 }];
      jest.spyOn(machineService, 'query').mockReturnValue(of(new HttpResponse({ body: machineCollection })));
      const additionalMachines = [machine];
      const expectedCollection: IMachine[] = [...additionalMachines, ...machineCollection];
      jest.spyOn(machineService, 'addMachineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ machineDocumentation });
      comp.ngOnInit();

      expect(machineService.query).toHaveBeenCalled();
      expect(machineService.addMachineToCollectionIfMissing).toHaveBeenCalledWith(
        machineCollection,
        ...additionalMachines.map(expect.objectContaining),
      );
      expect(comp.machinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const machineDocumentation: IMachineDocumentation = { id: 456 };
      const machine: IMachine = { id: 23614 };
      machineDocumentation.machine = machine;

      activatedRoute.data = of({ machineDocumentation });
      comp.ngOnInit();

      expect(comp.machinesSharedCollection).toContain(machine);
      expect(comp.machineDocumentation).toEqual(machineDocumentation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineDocumentation>>();
      const machineDocumentation = { id: 123 };
      jest.spyOn(machineDocumentationFormService, 'getMachineDocumentation').mockReturnValue(machineDocumentation);
      jest.spyOn(machineDocumentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineDocumentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machineDocumentation }));
      saveSubject.complete();

      // THEN
      expect(machineDocumentationFormService.getMachineDocumentation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(machineDocumentationService.update).toHaveBeenCalledWith(expect.objectContaining(machineDocumentation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineDocumentation>>();
      const machineDocumentation = { id: 123 };
      jest.spyOn(machineDocumentationFormService, 'getMachineDocumentation').mockReturnValue({ id: null });
      jest.spyOn(machineDocumentationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineDocumentation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machineDocumentation }));
      saveSubject.complete();

      // THEN
      expect(machineDocumentationFormService.getMachineDocumentation).toHaveBeenCalled();
      expect(machineDocumentationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineDocumentation>>();
      const machineDocumentation = { id: 123 };
      jest.spyOn(machineDocumentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineDocumentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(machineDocumentationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMachine', () => {
      it('Should forward to machineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(machineService, 'compareMachine');
        comp.compareMachine(entity, entity2);
        expect(machineService.compareMachine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
