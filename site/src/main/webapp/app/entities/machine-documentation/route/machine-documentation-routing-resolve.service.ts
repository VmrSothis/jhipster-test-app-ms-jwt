import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMachineDocumentation } from '../machine-documentation.model';
import { MachineDocumentationService } from '../service/machine-documentation.service';

export const machineDocumentationResolve = (route: ActivatedRouteSnapshot): Observable<null | IMachineDocumentation> => {
  const id = route.params['id'];
  if (id) {
    return inject(MachineDocumentationService)
      .find(id)
      .pipe(
        mergeMap((machineDocumentation: HttpResponse<IMachineDocumentation>) => {
          if (machineDocumentation.body) {
            return of(machineDocumentation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default machineDocumentationResolve;
