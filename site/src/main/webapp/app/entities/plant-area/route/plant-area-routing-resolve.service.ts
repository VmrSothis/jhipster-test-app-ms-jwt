import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlantArea } from '../plant-area.model';
import { PlantAreaService } from '../service/plant-area.service';

export const plantAreaResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlantArea> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlantAreaService)
      .find(id)
      .pipe(
        mergeMap((plantArea: HttpResponse<IPlantArea>) => {
          if (plantArea.body) {
            return of(plantArea.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default plantAreaResolve;
