import { IMachineDocumentation, NewMachineDocumentation } from './machine-documentation.model';

export const sampleWithRequiredData: IMachineDocumentation = {
  id: 31396,
  reference: 'acclimatise',
  name: 'qua to inasmuch',
};

export const sampleWithPartialData: IMachineDocumentation = {
  id: 28995,
  reference: 'often against',
  name: 'how',
  description: 'duh',
  url: 'https://beautiful-chemistry.es/',
};

export const sampleWithFullData: IMachineDocumentation = {
  id: 8873,
  reference: 'besiege team',
  name: 'dispose without',
  type: 'IMAGE',
  description: 'homogenate upbeat quizzically',
  url: 'https://plaintive-nudge.info/',
};

export const sampleWithNewData: NewMachineDocumentation = {
  reference: 'overdue unabashedly',
  name: 'because as',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
