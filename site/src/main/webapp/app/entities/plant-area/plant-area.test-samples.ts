import dayjs from 'dayjs/esm';

import { IPlantArea, NewPlantArea } from './plant-area.model';

export const sampleWithRequiredData: IPlantArea = {
  id: 6980,
  reference: 'stealthily gown',
  name: 'trait',
};

export const sampleWithPartialData: IPlantArea = {
  id: 18488,
  reference: 'along',
  name: 'successfully loudly shore',
  description: 'harass per',
  createdAt: dayjs('2024-03-26T17:57'),
};

export const sampleWithFullData: IPlantArea = {
  id: 8091,
  reference: 'basis new',
  name: 'pack jumpy whoa',
  description: 'aha admired boohoo',
  createdAt: dayjs('2024-03-26T15:38'),
  updatedAt: dayjs('2024-03-26T22:40'),
};

export const sampleWithNewData: NewPlantArea = {
  reference: 'concerning even',
  name: 'republic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
