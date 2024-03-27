import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MachineModelService } from '../service/machine-model.service';
import { IMachineModel } from '../machine-model.model';
import { MachineModelFormService } from './machine-model-form.service';

import { MachineModelUpdateComponent } from './machine-model-update.component';

describe('MachineModel Management Update Component', () => {
  let comp: MachineModelUpdateComponent;
  let fixture: ComponentFixture<MachineModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let machineModelFormService: MachineModelFormService;
  let machineModelService: MachineModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MachineModelUpdateComponent],
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
      .overrideTemplate(MachineModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MachineModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    machineModelFormService = TestBed.inject(MachineModelFormService);
    machineModelService = TestBed.inject(MachineModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const machineModel: IMachineModel = { id: 456 };

      activatedRoute.data = of({ machineModel });
      comp.ngOnInit();

      expect(comp.machineModel).toEqual(machineModel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineModel>>();
      const machineModel = { id: 123 };
      jest.spyOn(machineModelFormService, 'getMachineModel').mockReturnValue(machineModel);
      jest.spyOn(machineModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machineModel }));
      saveSubject.complete();

      // THEN
      expect(machineModelFormService.getMachineModel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(machineModelService.update).toHaveBeenCalledWith(expect.objectContaining(machineModel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineModel>>();
      const machineModel = { id: 123 };
      jest.spyOn(machineModelFormService, 'getMachineModel').mockReturnValue({ id: null });
      jest.spyOn(machineModelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineModel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: machineModel }));
      saveSubject.complete();

      // THEN
      expect(machineModelFormService.getMachineModel).toHaveBeenCalled();
      expect(machineModelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMachineModel>>();
      const machineModel = { id: 123 };
      jest.spyOn(machineModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ machineModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(machineModelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
