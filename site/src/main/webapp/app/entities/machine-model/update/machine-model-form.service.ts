import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMachineModel, NewMachineModel } from '../machine-model.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMachineModel for edit and NewMachineModelFormGroupInput for create.
 */
type MachineModelFormGroupInput = IMachineModel | PartialWithRequiredKeyOf<NewMachineModel>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMachineModel | NewMachineModel> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type MachineModelFormRawValue = FormValueOf<IMachineModel>;

type NewMachineModelFormRawValue = FormValueOf<NewMachineModel>;

type MachineModelFormDefaults = Pick<NewMachineModel, 'id' | 'createdAt' | 'updatedAt'>;

type MachineModelFormGroupContent = {
  id: FormControl<MachineModelFormRawValue['id'] | NewMachineModel['id']>;
  reference: FormControl<MachineModelFormRawValue['reference']>;
  name: FormControl<MachineModelFormRawValue['name']>;
  brandName: FormControl<MachineModelFormRawValue['brandName']>;
  description: FormControl<MachineModelFormRawValue['description']>;
  type: FormControl<MachineModelFormRawValue['type']>;
  manufacurerName: FormControl<MachineModelFormRawValue['manufacurerName']>;
  version: FormControl<MachineModelFormRawValue['version']>;
  createdAt: FormControl<MachineModelFormRawValue['createdAt']>;
  updatedAt: FormControl<MachineModelFormRawValue['updatedAt']>;
};

export type MachineModelFormGroup = FormGroup<MachineModelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MachineModelFormService {
  createMachineModelFormGroup(machineModel: MachineModelFormGroupInput = { id: null }): MachineModelFormGroup {
    const machineModelRawValue = this.convertMachineModelToMachineModelRawValue({
      ...this.getFormDefaults(),
      ...machineModel,
    });
    return new FormGroup<MachineModelFormGroupContent>({
      id: new FormControl(
        { value: machineModelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(machineModelRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(machineModelRawValue.name, {
        validators: [Validators.required],
      }),
      brandName: new FormControl(machineModelRawValue.brandName, {
        validators: [Validators.required],
      }),
      description: new FormControl(machineModelRawValue.description),
      type: new FormControl(machineModelRawValue.type),
      manufacurerName: new FormControl(machineModelRawValue.manufacurerName),
      version: new FormControl(machineModelRawValue.version),
      createdAt: new FormControl(machineModelRawValue.createdAt),
      updatedAt: new FormControl(machineModelRawValue.updatedAt),
    });
  }

  getMachineModel(form: MachineModelFormGroup): IMachineModel | NewMachineModel {
    return this.convertMachineModelRawValueToMachineModel(form.getRawValue() as MachineModelFormRawValue | NewMachineModelFormRawValue);
  }

  resetForm(form: MachineModelFormGroup, machineModel: MachineModelFormGroupInput): void {
    const machineModelRawValue = this.convertMachineModelToMachineModelRawValue({ ...this.getFormDefaults(), ...machineModel });
    form.reset(
      {
        ...machineModelRawValue,
        id: { value: machineModelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MachineModelFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertMachineModelRawValueToMachineModel(
    rawMachineModel: MachineModelFormRawValue | NewMachineModelFormRawValue,
  ): IMachineModel | NewMachineModel {
    return {
      ...rawMachineModel,
      createdAt: dayjs(rawMachineModel.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawMachineModel.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertMachineModelToMachineModelRawValue(
    machineModel: IMachineModel | (Partial<NewMachineModel> & MachineModelFormDefaults),
  ): MachineModelFormRawValue | PartialWithRequiredKeyOf<NewMachineModelFormRawValue> {
    return {
      ...machineModel,
      createdAt: machineModel.createdAt ? machineModel.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: machineModel.updatedAt ? machineModel.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
