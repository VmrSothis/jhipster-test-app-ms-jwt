import dayjs from 'dayjs/esm';
import { IOrganization } from 'app/entities/organization/organization.model';

export interface ISite {
  id: number;
  reference?: string | null;
  name?: string | null;
  description?: string | null;
  email?: string | null;
  telephone?: string | null;
  address?: string | null;
  postalCode?: string | null;
  region?: string | null;
  locality?: string | null;
  country?: string | null;
  location?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
}

export type NewSite = Omit<ISite, 'id'> & { id: null };
