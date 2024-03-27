import dayjs from 'dayjs/esm';

import { IMachine, NewMachine } from './machine.model';

export const sampleWithRequiredData: IMachine = {
  id: 32236,
  reference: 'accurate whereas atop',
  name: 'however',
};

export const sampleWithPartialData: IMachine = {
  id: 19154,
  reference: 'supposing despite',
  name: 'except reluctantly kooky',
  hardwareVersion: 'bah extreme underneath',
  supportedProtocol: 'as',
};

export const sampleWithFullData: IMachine = {
  id: 22765,
  reference: 'pantsuit robust',
  name: 'showcase',
  description: 'coaxingly boohoo nor',
  firmwareVersion: 'brilliant yuck',
  hardwareVersion: 'burly once aside',
  softwareVersion: 'catalysis',
  serialNumber: 'outlying',
  supportedProtocol: 'blissfully',
  createdAt: dayjs('2024-03-26T18:41'),
  updatedAt: dayjs('2024-03-27T09:15'),
};

export const sampleWithNewData: NewMachine = {
  reference: 'cudgel',
  name: 'apropos',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
