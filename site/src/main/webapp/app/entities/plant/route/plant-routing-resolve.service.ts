import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlant } from '../plant.model';
import { PlantService } from '../service/plant.service';

export const plantResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlant> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlantService)
      .find(id)
      .pipe(
        mergeMap((plant: HttpResponse<IPlant>) => {
          if (plant.body) {
            return of(plant.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default plantResolve;
