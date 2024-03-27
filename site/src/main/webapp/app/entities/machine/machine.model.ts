import dayjs from 'dayjs/esm';
import { IPlantArea } from 'app/entities/plant-area/plant-area.model';
import { IMachineModel } from 'app/entities/machine-model/machine-model.model';
import { IOrganization } from 'app/entities/organization/organization.model';

export interface IMachine {
  id: number;
  reference?: string | null;
  name?: string | null;
  description?: string | null;
  firmwareVersion?: string | null;
  hardwareVersion?: string | null;
  softwareVersion?: string | null;
  serialNumber?: string | null;
  supportedProtocol?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  plantArea?: Pick<IPlantArea, 'id' | 'name'> | null;
  machineModel?: Pick<IMachineModel, 'id' | 'name'> | null;
  organization?: Pick<IOrganization, 'id'> | null;
}

export type NewMachine = Omit<IMachine, 'id'> & { id: null };
