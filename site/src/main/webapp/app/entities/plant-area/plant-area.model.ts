import dayjs from 'dayjs/esm';
import { IPlant } from 'app/entities/plant/plant.model';

export interface IPlantArea {
  id: number;
  reference?: string | null;
  name?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  plant?: Pick<IPlant, 'id' | 'name'> | null;
}

export type NewPlantArea = Omit<IPlantArea, 'id'> & { id: null };
