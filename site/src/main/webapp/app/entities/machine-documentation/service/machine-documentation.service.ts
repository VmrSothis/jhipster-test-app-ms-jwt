import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMachineDocumentation, NewMachineDocumentation } from '../machine-documentation.model';

export type PartialUpdateMachineDocumentation = Partial<IMachineDocumentation> & Pick<IMachineDocumentation, 'id'>;

export type EntityResponseType = HttpResponse<IMachineDocumentation>;
export type EntityArrayResponseType = HttpResponse<IMachineDocumentation[]>;

@Injectable({ providedIn: 'root' })
export class MachineDocumentationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/machine-documentations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(machineDocumentation: NewMachineDocumentation): Observable<EntityResponseType> {
    return this.http.post<IMachineDocumentation>(this.resourceUrl, machineDocumentation, { observe: 'response' });
  }

  update(machineDocumentation: IMachineDocumentation): Observable<EntityResponseType> {
    return this.http.put<IMachineDocumentation>(
      `${this.resourceUrl}/${this.getMachineDocumentationIdentifier(machineDocumentation)}`,
      machineDocumentation,
      { observe: 'response' },
    );
  }

  partialUpdate(machineDocumentation: PartialUpdateMachineDocumentation): Observable<EntityResponseType> {
    return this.http.patch<IMachineDocumentation>(
      `${this.resourceUrl}/${this.getMachineDocumentationIdentifier(machineDocumentation)}`,
      machineDocumentation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMachineDocumentation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMachineDocumentation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMachineDocumentationIdentifier(machineDocumentation: Pick<IMachineDocumentation, 'id'>): number {
    return machineDocumentation.id;
  }

  compareMachineDocumentation(o1: Pick<IMachineDocumentation, 'id'> | null, o2: Pick<IMachineDocumentation, 'id'> | null): boolean {
    return o1 && o2 ? this.getMachineDocumentationIdentifier(o1) === this.getMachineDocumentationIdentifier(o2) : o1 === o2;
  }

  addMachineDocumentationToCollectionIfMissing<Type extends Pick<IMachineDocumentation, 'id'>>(
    machineDocumentationCollection: Type[],
    ...machineDocumentationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const machineDocumentations: Type[] = machineDocumentationsToCheck.filter(isPresent);
    if (machineDocumentations.length > 0) {
      const machineDocumentationCollectionIdentifiers = machineDocumentationCollection.map(
        machineDocumentationItem => this.getMachineDocumentationIdentifier(machineDocumentationItem)!,
      );
      const machineDocumentationsToAdd = machineDocumentations.filter(machineDocumentationItem => {
        const machineDocumentationIdentifier = this.getMachineDocumentationIdentifier(machineDocumentationItem);
        if (machineDocumentationCollectionIdentifiers.includes(machineDocumentationIdentifier)) {
          return false;
        }
        machineDocumentationCollectionIdentifiers.push(machineDocumentationIdentifier);
        return true;
      });
      return [...machineDocumentationsToAdd, ...machineDocumentationCollection];
    }
    return machineDocumentationCollection;
  }
}
