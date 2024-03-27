import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMachineModel } from '../machine-model.model';
import { MachineModelService } from '../service/machine-model.service';

export const machineModelResolve = (route: ActivatedRouteSnapshot): Observable<null | IMachineModel> => {
  const id = route.params['id'];
  if (id) {
    return inject(MachineModelService)
      .find(id)
      .pipe(
        mergeMap((machineModel: HttpResponse<IMachineModel>) => {
          if (machineModel.body) {
            return of(machineModel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default machineModelResolve;
