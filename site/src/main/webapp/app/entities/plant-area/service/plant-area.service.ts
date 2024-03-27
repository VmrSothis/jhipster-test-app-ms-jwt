import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlantArea, NewPlantArea } from '../plant-area.model';

export type PartialUpdatePlantArea = Partial<IPlantArea> & Pick<IPlantArea, 'id'>;

type RestOf<T extends IPlantArea | NewPlantArea> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestPlantArea = RestOf<IPlantArea>;

export type NewRestPlantArea = RestOf<NewPlantArea>;

export type PartialUpdateRestPlantArea = RestOf<PartialUpdatePlantArea>;

export type EntityResponseType = HttpResponse<IPlantArea>;
export type EntityArrayResponseType = HttpResponse<IPlantArea[]>;

@Injectable({ providedIn: 'root' })
export class PlantAreaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plant-areas');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(plantArea: NewPlantArea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantArea);
    return this.http
      .post<RestPlantArea>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(plantArea: IPlantArea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantArea);
    return this.http
      .put<RestPlantArea>(`${this.resourceUrl}/${this.getPlantAreaIdentifier(plantArea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(plantArea: PartialUpdatePlantArea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantArea);
    return this.http
      .patch<RestPlantArea>(`${this.resourceUrl}/${this.getPlantAreaIdentifier(plantArea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlantArea>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlantArea[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlantAreaIdentifier(plantArea: Pick<IPlantArea, 'id'>): number {
    return plantArea.id;
  }

  comparePlantArea(o1: Pick<IPlantArea, 'id'> | null, o2: Pick<IPlantArea, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlantAreaIdentifier(o1) === this.getPlantAreaIdentifier(o2) : o1 === o2;
  }

  addPlantAreaToCollectionIfMissing<Type extends Pick<IPlantArea, 'id'>>(
    plantAreaCollection: Type[],
    ...plantAreasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const plantAreas: Type[] = plantAreasToCheck.filter(isPresent);
    if (plantAreas.length > 0) {
      const plantAreaCollectionIdentifiers = plantAreaCollection.map(plantAreaItem => this.getPlantAreaIdentifier(plantAreaItem)!);
      const plantAreasToAdd = plantAreas.filter(plantAreaItem => {
        const plantAreaIdentifier = this.getPlantAreaIdentifier(plantAreaItem);
        if (plantAreaCollectionIdentifiers.includes(plantAreaIdentifier)) {
          return false;
        }
        plantAreaCollectionIdentifiers.push(plantAreaIdentifier);
        return true;
      });
      return [...plantAreasToAdd, ...plantAreaCollection];
    }
    return plantAreaCollection;
  }

  protected convertDateFromClient<T extends IPlantArea | NewPlantArea | PartialUpdatePlantArea>(plantArea: T): RestOf<T> {
    return {
      ...plantArea,
      createdAt: plantArea.createdAt?.toJSON() ?? null,
      updatedAt: plantArea.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlantArea: RestPlantArea): IPlantArea {
    return {
      ...restPlantArea,
      createdAt: restPlantArea.createdAt ? dayjs(restPlantArea.createdAt) : undefined,
      updatedAt: restPlantArea.updatedAt ? dayjs(restPlantArea.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlantArea>): HttpResponse<IPlantArea> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlantArea[]>): HttpResponse<IPlantArea[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
