import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PlantAreaComponent } from './list/plant-area.component';
import { PlantAreaDetailComponent } from './detail/plant-area-detail.component';
import { PlantAreaUpdateComponent } from './update/plant-area-update.component';
import PlantAreaResolve from './route/plant-area-routing-resolve.service';

const plantAreaRoute: Routes = [
  {
    path: '',
    component: PlantAreaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlantAreaDetailComponent,
    resolve: {
      plantArea: PlantAreaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlantAreaUpdateComponent,
    resolve: {
      plantArea: PlantAreaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlantAreaUpdateComponent,
    resolve: {
      plantArea: PlantAreaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default plantAreaRoute;
