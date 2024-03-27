import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';

export interface IPlant {
  id: number;
  reference?: string | null;
  name?: string | null;
  description?: string | null;
  address?: string | null;
  location?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  site?: Pick<ISite, 'id' | 'name'> | null;
}

export type NewPlant = Omit<IPlant, 'id'> & { id: null };
