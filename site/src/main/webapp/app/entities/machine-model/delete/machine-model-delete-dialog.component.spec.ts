jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MachineModelService } from '../service/machine-model.service';

import { MachineModelDeleteDialogComponent } from './machine-model-delete-dialog.component';

describe('MachineModel Management Delete Component', () => {
  let comp: MachineModelDeleteDialogComponent;
  let fixture: ComponentFixture<MachineModelDeleteDialogComponent>;
  let service: MachineModelService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MachineModelDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MachineModelDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MachineModelDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MachineModelService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
