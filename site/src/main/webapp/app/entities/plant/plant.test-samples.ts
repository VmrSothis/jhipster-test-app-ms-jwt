import dayjs from 'dayjs/esm';

import { IPlant, NewPlant } from './plant.model';

export const sampleWithRequiredData: IPlant = {
  id: 17127,
  reference: 'readily',
  name: 'awkwardly decipher',
  description: 'easily snow',
};

export const sampleWithPartialData: IPlant = {
  id: 17183,
  reference: 'miserably discuss tog',
  name: 'forenenst',
  description: 'now',
  address: 'geez helium on',
  location: 'thankfully unto',
  updatedAt: dayjs('2024-03-26T15:30'),
};

export const sampleWithFullData: IPlant = {
  id: 9385,
  reference: 'ew law',
  name: 'considering considering alluvium',
  description: 'brush',
  address: 'since boohoo till',
  location: 'frantically abet flame',
  createdAt: dayjs('2024-03-26T16:31'),
  updatedAt: dayjs('2024-03-26T23:01'),
};

export const sampleWithNewData: NewPlant = {
  reference: 'because given',
  name: 'per',
  description: 'above boohoo worst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
