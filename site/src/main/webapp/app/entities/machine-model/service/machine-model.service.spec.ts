import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMachineModel } from '../machine-model.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../machine-model.test-samples';

import { MachineModelService, RestMachineModel } from './machine-model.service';

const requireRestSample: RestMachineModel = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('MachineModel Service', () => {
  let service: MachineModelService;
  let httpMock: HttpTestingController;
  let expectedResult: IMachineModel | IMachineModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MachineModelService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MachineModel', () => {
      const machineModel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(machineModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MachineModel', () => {
      const machineModel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(machineModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MachineModel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MachineModel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MachineModel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMachineModelToCollectionIfMissing', () => {
      it('should add a MachineModel to an empty array', () => {
        const machineModel: IMachineModel = sampleWithRequiredData;
        expectedResult = service.addMachineModelToCollectionIfMissing([], machineModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machineModel);
      });

      it('should not add a MachineModel to an array that contains it', () => {
        const machineModel: IMachineModel = sampleWithRequiredData;
        const machineModelCollection: IMachineModel[] = [
          {
            ...machineModel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMachineModelToCollectionIfMissing(machineModelCollection, machineModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MachineModel to an array that doesn't contain it", () => {
        const machineModel: IMachineModel = sampleWithRequiredData;
        const machineModelCollection: IMachineModel[] = [sampleWithPartialData];
        expectedResult = service.addMachineModelToCollectionIfMissing(machineModelCollection, machineModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machineModel);
      });

      it('should add only unique MachineModel to an array', () => {
        const machineModelArray: IMachineModel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const machineModelCollection: IMachineModel[] = [sampleWithRequiredData];
        expectedResult = service.addMachineModelToCollectionIfMissing(machineModelCollection, ...machineModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const machineModel: IMachineModel = sampleWithRequiredData;
        const machineModel2: IMachineModel = sampleWithPartialData;
        expectedResult = service.addMachineModelToCollectionIfMissing([], machineModel, machineModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machineModel);
        expect(expectedResult).toContain(machineModel2);
      });

      it('should accept null and undefined values', () => {
        const machineModel: IMachineModel = sampleWithRequiredData;
        expectedResult = service.addMachineModelToCollectionIfMissing([], null, machineModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machineModel);
      });

      it('should return initial array if no MachineModel is added', () => {
        const machineModelCollection: IMachineModel[] = [sampleWithRequiredData];
        expectedResult = service.addMachineModelToCollectionIfMissing(machineModelCollection, undefined, null);
        expect(expectedResult).toEqual(machineModelCollection);
      });
    });

    describe('compareMachineModel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMachineModel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMachineModel(entity1, entity2);
        const compareResult2 = service.compareMachineModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMachineModel(entity1, entity2);
        const compareResult2 = service.compareMachineModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMachineModel(entity1, entity2);
        const compareResult2 = service.compareMachineModel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
