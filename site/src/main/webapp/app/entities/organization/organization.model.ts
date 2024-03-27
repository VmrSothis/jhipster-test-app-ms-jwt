import dayjs from 'dayjs/esm';
import { IMachine } from 'app/entities/machine/machine.model';

export interface IOrganization {
  id: number;
  reference?: string | null;
  name?: string | null;
  legalName?: string | null;
  description?: string | null;
  taxId?: string | null;
  email?: string | null;
  telephone?: string | null;
  url?: string | null;
  address?: string | null;
  postalCode?: string | null;
  region?: string | null;
  locality?: string | null;
  country?: string | null;
  location?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  machines?: Pick<IMachine, 'id'>[] | null;
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
