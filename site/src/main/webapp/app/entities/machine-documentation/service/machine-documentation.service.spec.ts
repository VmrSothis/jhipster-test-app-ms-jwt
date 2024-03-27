import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMachineDocumentation } from '../machine-documentation.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../machine-documentation.test-samples';

import { MachineDocumentationService } from './machine-documentation.service';

const requireRestSample: IMachineDocumentation = {
  ...sampleWithRequiredData,
};

describe('MachineDocumentation Service', () => {
  let service: MachineDocumentationService;
  let httpMock: HttpTestingController;
  let expectedResult: IMachineDocumentation | IMachineDocumentation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MachineDocumentationService);
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

    it('should create a MachineDocumentation', () => {
      const machineDocumentation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(machineDocumentation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MachineDocumentation', () => {
      const machineDocumentation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(machineDocumentation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MachineDocumentation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MachineDocumentation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MachineDocumentation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMachineDocumentationToCollectionIfMissing', () => {
      it('should add a MachineDocumentation to an empty array', () => {
        const machineDocumentation: IMachineDocumentation = sampleWithRequiredData;
        expectedResult = service.addMachineDocumentationToCollectionIfMissing([], machineDocumentation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machineDocumentation);
      });

      it('should not add a MachineDocumentation to an array that contains it', () => {
        const machineDocumentation: IMachineDocumentation = sampleWithRequiredData;
        const machineDocumentationCollection: IMachineDocumentation[] = [
          {
            ...machineDocumentation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMachineDocumentationToCollectionIfMissing(machineDocumentationCollection, machineDocumentation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MachineDocumentation to an array that doesn't contain it", () => {
        const machineDocumentation: IMachineDocumentation = sampleWithRequiredData;
        const machineDocumentationCollection: IMachineDocumentation[] = [sampleWithPartialData];
        expectedResult = service.addMachineDocumentationToCollectionIfMissing(machineDocumentationCollection, machineDocumentation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machineDocumentation);
      });

      it('should add only unique MachineDocumentation to an array', () => {
        const machineDocumentationArray: IMachineDocumentation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const machineDocumentationCollection: IMachineDocumentation[] = [sampleWithRequiredData];
        expectedResult = service.addMachineDocumentationToCollectionIfMissing(machineDocumentationCollection, ...machineDocumentationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const machineDocumentation: IMachineDocumentation = sampleWithRequiredData;
        const machineDocumentation2: IMachineDocumentation = sampleWithPartialData;
        expectedResult = service.addMachineDocumentationToCollectionIfMissing([], machineDocumentation, machineDocumentation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(machineDocumentation);
        expect(expectedResult).toContain(machineDocumentation2);
      });

      it('should accept null and undefined values', () => {
        const machineDocumentation: IMachineDocumentation = sampleWithRequiredData;
        expectedResult = service.addMachineDocumentationToCollectionIfMissing([], null, machineDocumentation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(machineDocumentation);
      });

      it('should return initial array if no MachineDocumentation is added', () => {
        const machineDocumentationCollection: IMachineDocumentation[] = [sampleWithRequiredData];
        expectedResult = service.addMachineDocumentationToCollectionIfMissing(machineDocumentationCollection, undefined, null);
        expect(expectedResult).toEqual(machineDocumentationCollection);
      });
    });

    describe('compareMachineDocumentation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMachineDocumentation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMachineDocumentation(entity1, entity2);
        const compareResult2 = service.compareMachineDocumentation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMachineDocumentation(entity1, entity2);
        const compareResult2 = service.compareMachineDocumentation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMachineDocumentation(entity1, entity2);
        const compareResult2 = service.compareMachineDocumentation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
