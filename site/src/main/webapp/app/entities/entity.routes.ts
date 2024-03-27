import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'organization',
    data: { pageTitle: 'jhipsterSiteApp.organization.home.title' },
    loadChildren: () => import('./organization/organization.routes'),
  },
  {
    path: 'site',
    data: { pageTitle: 'jhipsterSiteApp.site.home.title' },
    loadChildren: () => import('./site/site.routes'),
  },
  {
    path: 'plant',
    data: { pageTitle: 'jhipsterSiteApp.plant.home.title' },
    loadChildren: () => import('./plant/plant.routes'),
  },
  {
    path: 'plant-area',
    data: { pageTitle: 'jhipsterSiteApp.plantArea.home.title' },
    loadChildren: () => import('./plant-area/plant-area.routes'),
  },
  {
    path: 'machine',
    data: { pageTitle: 'jhipsterSiteApp.machine.home.title' },
    loadChildren: () => import('./machine/machine.routes'),
  },
  {
    path: 'machine-model',
    data: { pageTitle: 'jhipsterSiteApp.machineModel.home.title' },
    loadChildren: () => import('./machine-model/machine-model.routes'),
  },
  {
    path: 'machine-documentation',
    data: { pageTitle: 'jhipsterSiteApp.machineDocumentation.home.title' },
    loadChildren: () => import('./machine-documentation/machine-documentation.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
