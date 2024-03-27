import dayjs from 'dayjs/esm';

export interface IMachineModel {
  id: number;
  reference?: string | null;
  name?: string | null;
  brandName?: string | null;
  description?: string | null;
  type?: string | null;
  manufacurerName?: string | null;
  version?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewMachineModel = Omit<IMachineModel, 'id'> & { id: null };
