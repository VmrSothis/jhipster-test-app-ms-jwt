import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MachineDocumentationComponent } from './list/machine-documentation.component';
import { MachineDocumentationDetailComponent } from './detail/machine-documentation-detail.component';
import { MachineDocumentationUpdateComponent } from './update/machine-documentation-update.component';
import MachineDocumentationResolve from './route/machine-documentation-routing-resolve.service';

const machineDocumentationRoute: Routes = [
  {
    path: '',
    component: MachineDocumentationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MachineDocumentationDetailComponent,
    resolve: {
      machineDocumentation: MachineDocumentationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MachineDocumentationUpdateComponent,
    resolve: {
      machineDocumentation: MachineDocumentationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MachineDocumentationUpdateComponent,
    resolve: {
      machineDocumentation: MachineDocumentationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default machineDocumentationRoute;
