import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMachine, NewMachine } from '../machine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMachine for edit and NewMachineFormGroupInput for create.
 */
type MachineFormGroupInput = IMachine | PartialWithRequiredKeyOf<NewMachine>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMachine | NewMachine> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type MachineFormRawValue = FormValueOf<IMachine>;

type NewMachineFormRawValue = FormValueOf<NewMachine>;

type MachineFormDefaults = Pick<NewMachine, 'id' | 'createdAt' | 'updatedAt'>;

type MachineFormGroupContent = {
  id: FormControl<MachineFormRawValue['id'] | NewMachine['id']>;
  reference: FormControl<MachineFormRawValue['reference']>;
  name: FormControl<MachineFormRawValue['name']>;
  description: FormControl<MachineFormRawValue['description']>;
  firmwareVersion: FormControl<MachineFormRawValue['firmwareVersion']>;
  hardwareVersion: FormControl<MachineFormRawValue['hardwareVersion']>;
  softwareVersion: FormControl<MachineFormRawValue['softwareVersion']>;
  serialNumber: FormControl<MachineFormRawValue['serialNumber']>;
  supportedProtocol: FormControl<MachineFormRawValue['supportedProtocol']>;
  createdAt: FormControl<MachineFormRawValue['createdAt']>;
  updatedAt: FormControl<MachineFormRawValue['updatedAt']>;
  plantArea: FormControl<MachineFormRawValue['plantArea']>;
  machineModel: FormControl<MachineFormRawValue['machineModel']>;
  organization: FormControl<MachineFormRawValue['organization']>;
};

export type MachineFormGroup = FormGroup<MachineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MachineFormService {
  createMachineFormGroup(machine: MachineFormGroupInput = { id: null }): MachineFormGroup {
    const machineRawValue = this.convertMachineToMachineRawValue({
      ...this.getFormDefaults(),
      ...machine,
    });
    return new FormGroup<MachineFormGroupContent>({
      id: new FormControl(
        { value: machineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(machineRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(machineRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(machineRawValue.description),
      firmwareVersion: new FormControl(machineRawValue.firmwareVersion),
      hardwareVersion: new FormControl(machineRawValue.hardwareVersion),
      softwareVersion: new FormControl(machineRawValue.softwareVersion),
      serialNumber: new FormControl(machineRawValue.serialNumber),
      supportedProtocol: new FormControl(machineRawValue.supportedProtocol),
      createdAt: new FormControl(machineRawValue.createdAt),
      updatedAt: new FormControl(machineRawValue.updatedAt),
      plantArea: new FormControl(machineRawValue.plantArea, {
        validators: [Validators.required],
      }),
      machineModel: new FormControl(machineRawValue.machineModel, {
        validators: [Validators.required],
      }),
      organization: new FormControl(machineRawValue.organization),
    });
  }

  getMachine(form: MachineFormGroup): IMachine | NewMachine {
    return this.convertMachineRawValueToMachine(form.getRawValue() as MachineFormRawValue | NewMachineFormRawValue);
  }

  resetForm(form: MachineFormGroup, machine: MachineFormGroupInput): void {
    const machineRawValue = this.convertMachineToMachineRawValue({ ...this.getFormDefaults(), ...machine });
    form.reset(
      {
        ...machineRawValue,
        id: { value: machineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MachineFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertMachineRawValueToMachine(rawMachine: MachineFormRawValue | NewMachineFormRawValue): IMachine | NewMachine {
    return {
      ...rawMachine,
      createdAt: dayjs(rawMachine.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawMachine.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertMachineToMachineRawValue(
    machine: IMachine | (Partial<NewMachine> & MachineFormDefaults),
  ): MachineFormRawValue | PartialWithRequiredKeyOf<NewMachineFormRawValue> {
    return {
      ...machine,
      createdAt: machine.createdAt ? machine.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: machine.updatedAt ? machine.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
