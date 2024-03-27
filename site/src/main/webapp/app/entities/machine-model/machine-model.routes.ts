import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MachineModelComponent } from './list/machine-model.component';
import { MachineModelDetailComponent } from './detail/machine-model-detail.component';
import { MachineModelUpdateComponent } from './update/machine-model-update.component';
import MachineModelResolve from './route/machine-model-routing-resolve.service';

const machineModelRoute: Routes = [
  {
    path: '',
    component: MachineModelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MachineModelDetailComponent,
    resolve: {
      machineModel: MachineModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MachineModelUpdateComponent,
    resolve: {
      machineModel: MachineModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MachineModelUpdateComponent,
    resolve: {
      machineModel: MachineModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default machineModelRoute;
