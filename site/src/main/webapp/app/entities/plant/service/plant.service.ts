import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlant, NewPlant } from '../plant.model';

export type PartialUpdatePlant = Partial<IPlant> & Pick<IPlant, 'id'>;

type RestOf<T extends IPlant | NewPlant> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestPlant = RestOf<IPlant>;

export type NewRestPlant = RestOf<NewPlant>;

export type PartialUpdateRestPlant = RestOf<PartialUpdatePlant>;

export type EntityResponseType = HttpResponse<IPlant>;
export type EntityArrayResponseType = HttpResponse<IPlant[]>;

@Injectable({ providedIn: 'root' })
export class PlantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plants');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(plant: NewPlant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plant);
    return this.http.post<RestPlant>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(plant: IPlant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plant);
    return this.http
      .put<RestPlant>(`${this.resourceUrl}/${this.getPlantIdentifier(plant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(plant: PartialUpdatePlant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plant);
    return this.http
      .patch<RestPlant>(`${this.resourceUrl}/${this.getPlantIdentifier(plant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlantIdentifier(plant: Pick<IPlant, 'id'>): number {
    return plant.id;
  }

  comparePlant(o1: Pick<IPlant, 'id'> | null, o2: Pick<IPlant, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlantIdentifier(o1) === this.getPlantIdentifier(o2) : o1 === o2;
  }

  addPlantToCollectionIfMissing<Type extends Pick<IPlant, 'id'>>(
    plantCollection: Type[],
    ...plantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const plants: Type[] = plantsToCheck.filter(isPresent);
    if (plants.length > 0) {
      const plantCollectionIdentifiers = plantCollection.map(plantItem => this.getPlantIdentifier(plantItem)!);
      const plantsToAdd = plants.filter(plantItem => {
        const plantIdentifier = this.getPlantIdentifier(plantItem);
        if (plantCollectionIdentifiers.includes(plantIdentifier)) {
          return false;
        }
        plantCollectionIdentifiers.push(plantIdentifier);
        return true;
      });
      return [...plantsToAdd, ...plantCollection];
    }
    return plantCollection;
  }

  protected convertDateFromClient<T extends IPlant | NewPlant | PartialUpdatePlant>(plant: T): RestOf<T> {
    return {
      ...plant,
      createdAt: plant.createdAt?.toJSON() ?? null,
      updatedAt: plant.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlant: RestPlant): IPlant {
    return {
      ...restPlant,
      createdAt: restPlant.createdAt ? dayjs(restPlant.createdAt) : undefined,
      updatedAt: restPlant.updatedAt ? dayjs(restPlant.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlant>): HttpResponse<IPlant> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlant[]>): HttpResponse<IPlant[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
