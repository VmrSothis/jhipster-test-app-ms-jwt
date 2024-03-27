import dayjs from 'dayjs/esm';

import { IOrganization, NewOrganization } from './organization.model';

export const sampleWithRequiredData: IOrganization = {
  id: 22555,
  reference: 'crushing',
  name: 'location what',
};

export const sampleWithPartialData: IOrganization = {
  id: 29432,
  reference: 'tiny that',
  name: 'yippee pfft',
  legalName: 'hm reacquaint officially',
  description: 'forenenst slink',
  taxId: 'further rigidly delightfully',
  telephone: '987.059.842',
  postalCode: 'quizzically',
  createdAt: dayjs('2024-03-26T17:00'),
  updatedAt: dayjs('2024-03-26T19:16'),
};

export const sampleWithFullData: IOrganization = {
  id: 30997,
  reference: 'gadzooks oh nobble',
  name: 'taut yoyo',
  legalName: 'boohoo along yet',
  description: 'mushroom bite-sized foreground',
  taxId: 'aha so',
  email: 'Jesus_CisnerosRenteria27@yahoo.com',
  telephone: '922.593.460',
  url: 'https://steep-transition.info',
  address: 'mochi sane',
  postalCode: 'hmph gosh yet',
  region: 'brr stand',
  locality: 'fund mostly indeed',
  country: 'Turquia',
  location: 'what',
  createdAt: dayjs('2024-03-26T21:18'),
  updatedAt: dayjs('2024-03-26T22:16'),
};

export const sampleWithNewData: NewOrganization = {
  reference: 'pace',
  name: 'pish pastor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
