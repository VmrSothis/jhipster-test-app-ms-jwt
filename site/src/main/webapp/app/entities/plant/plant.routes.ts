import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PlantComponent } from './list/plant.component';
import { PlantDetailComponent } from './detail/plant-detail.component';
import { PlantUpdateComponent } from './update/plant-update.component';
import PlantResolve from './route/plant-routing-resolve.service';

const plantRoute: Routes = [
  {
    path: '',
    component: PlantComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlantDetailComponent,
    resolve: {
      plant: PlantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlantUpdateComponent,
    resolve: {
      plant: PlantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlantUpdateComponent,
    resolve: {
      plant: PlantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default plantRoute;
