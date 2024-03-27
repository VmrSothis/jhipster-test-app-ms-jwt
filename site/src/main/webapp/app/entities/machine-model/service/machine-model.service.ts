import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMachineModel, NewMachineModel } from '../machine-model.model';

export type PartialUpdateMachineModel = Partial<IMachineModel> & Pick<IMachineModel, 'id'>;

type RestOf<T extends IMachineModel | NewMachineModel> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestMachineModel = RestOf<IMachineModel>;

export type NewRestMachineModel = RestOf<NewMachineModel>;

export type PartialUpdateRestMachineModel = RestOf<PartialUpdateMachineModel>;

export type EntityResponseType = HttpResponse<IMachineModel>;
export type EntityArrayResponseType = HttpResponse<IMachineModel[]>;

@Injectable({ providedIn: 'root' })
export class MachineModelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/machine-models');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(machineModel: NewMachineModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machineModel);
    return this.http
      .post<RestMachineModel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(machineModel: IMachineModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machineModel);
    return this.http
      .put<RestMachineModel>(`${this.resourceUrl}/${this.getMachineModelIdentifier(machineModel)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(machineModel: PartialUpdateMachineModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(machineModel);
    return this.http
      .patch<RestMachineModel>(`${this.resourceUrl}/${this.getMachineModelIdentifier(machineModel)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMachineModel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMachineModel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMachineModelIdentifier(machineModel: Pick<IMachineModel, 'id'>): number {
    return machineModel.id;
  }

  compareMachineModel(o1: Pick<IMachineModel, 'id'> | null, o2: Pick<IMachineModel, 'id'> | null): boolean {
    return o1 && o2 ? this.getMachineModelIdentifier(o1) === this.getMachineModelIdentifier(o2) : o1 === o2;
  }

  addMachineModelToCollectionIfMissing<Type extends Pick<IMachineModel, 'id'>>(
    machineModelCollection: Type[],
    ...machineModelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const machineModels: Type[] = machineModelsToCheck.filter(isPresent);
    if (machineModels.length > 0) {
      const machineModelCollectionIdentifiers = machineModelCollection.map(
        machineModelItem => this.getMachineModelIdentifier(machineModelItem)!,
      );
      const machineModelsToAdd = machineModels.filter(machineModelItem => {
        const machineModelIdentifier = this.getMachineModelIdentifier(machineModelItem);
        if (machineModelCollectionIdentifiers.includes(machineModelIdentifier)) {
          return false;
        }
        machineModelCollectionIdentifiers.push(machineModelIdentifier);
        return true;
      });
      return [...machineModelsToAdd, ...machineModelCollection];
    }
    return machineModelCollection;
  }

  protected convertDateFromClient<T extends IMachineModel | NewMachineModel | PartialUpdateMachineModel>(machineModel: T): RestOf<T> {
    return {
      ...machineModel,
      createdAt: machineModel.createdAt?.toJSON() ?? null,
      updatedAt: machineModel.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMachineModel: RestMachineModel): IMachineModel {
    return {
      ...restMachineModel,
      createdAt: restMachineModel.createdAt ? dayjs(restMachineModel.createdAt) : undefined,
      updatedAt: restMachineModel.updatedAt ? dayjs(restMachineModel.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMachineModel>): HttpResponse<IMachineModel> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMachineModel[]>): HttpResponse<IMachineModel[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
