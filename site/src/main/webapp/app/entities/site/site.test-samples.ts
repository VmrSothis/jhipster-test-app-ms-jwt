import dayjs from 'dayjs/esm';

import { ISite, NewSite } from './site.model';

export const sampleWithRequiredData: ISite = {
  id: 7028,
  reference: 'pish blah',
  name: 'impartial fooey console',
};

export const sampleWithPartialData: ISite = {
  id: 29799,
  reference: 'upon whether',
  name: 'shadow or yuck',
  description: 'stinger quixotic',
  telephone: '945538060',
  address: 'workbench',
  postalCode: 'unacceptable',
  locality: 'skyjack admire',
  createdAt: dayjs('2024-03-26T20:54'),
};

export const sampleWithFullData: ISite = {
  id: 5030,
  reference: 'planning',
  name: 'hence',
  description: 'enormously',
  email: 'Teodoro_MataCintron@gmail.com',
  telephone: '948 355 054',
  address: 'equally uh-huh',
  postalCode: 'thoroughly modulo',
  region: 'wetly prosecutor',
  locality: 'midst by quiz',
  country: 'Congo',
  location: 'um as',
  createdAt: dayjs('2024-03-27T04:40'),
  updatedAt: dayjs('2024-03-27T02:20'),
};

export const sampleWithNewData: NewSite = {
  reference: 'upright after obediently',
  name: 'frail anenst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
