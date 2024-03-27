import dayjs from 'dayjs/esm';

import { IMachineModel, NewMachineModel } from './machine-model.model';

export const sampleWithRequiredData: IMachineModel = {
  id: 11870,
  reference: 'politely',
  name: 'apud woot',
  brandName: 'whenever',
};

export const sampleWithPartialData: IMachineModel = {
  id: 16861,
  reference: 'opposite shrivel lopsided',
  name: 'confess',
  brandName: 'yowza old-fashioned',
  description: 'boo pish freely',
  type: 'anxiously beyond',
  updatedAt: dayjs('2024-03-26T18:58'),
};

export const sampleWithFullData: IMachineModel = {
  id: 30243,
  reference: 'um',
  name: 'item by hm',
  brandName: 'triangular warped slot',
  description: 'fooey',
  type: 'mmm',
  manufacurerName: 'inside before absent',
  version: 'modulo towards',
  createdAt: dayjs('2024-03-27T10:46'),
  updatedAt: dayjs('2024-03-27T10:08'),
};

export const sampleWithNewData: NewMachineModel = {
  reference: 'qua',
  name: 'demotivate meaningfully',
  brandName: 'refuse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
