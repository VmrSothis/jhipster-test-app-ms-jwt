import { IMachine } from 'app/entities/machine/machine.model';
import { AttachedType } from 'app/entities/enumerations/attached-type.model';

export interface IMachineDocumentation {
  id: number;
  reference?: string | null;
  name?: string | null;
  type?: keyof typeof AttachedType | null;
  description?: string | null;
  url?: string | null;
  machine?: Pick<IMachine, 'id' | 'name'> | null;
}

export type NewMachineDocumentation = Omit<IMachineDocumentation, 'id'> & { id: null };
