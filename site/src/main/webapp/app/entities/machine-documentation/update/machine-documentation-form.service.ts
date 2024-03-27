import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMachineDocumentation, NewMachineDocumentation } from '../machine-documentation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMachineDocumentation for edit and NewMachineDocumentationFormGroupInput for create.
 */
type MachineDocumentationFormGroupInput = IMachineDocumentation | PartialWithRequiredKeyOf<NewMachineDocumentation>;

type MachineDocumentationFormDefaults = Pick<NewMachineDocumentation, 'id'>;

type MachineDocumentationFormGroupContent = {
  id: FormControl<IMachineDocumentation['id'] | NewMachineDocumentation['id']>;
  reference: FormControl<IMachineDocumentation['reference']>;
  name: FormControl<IMachineDocumentation['name']>;
  type: FormControl<IMachineDocumentation['type']>;
  description: FormControl<IMachineDocumentation['description']>;
  url: FormControl<IMachineDocumentation['url']>;
  machine: FormControl<IMachineDocumentation['machine']>;
};

export type MachineDocumentationFormGroup = FormGroup<MachineDocumentationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MachineDocumentationFormService {
  createMachineDocumentationFormGroup(
    machineDocumentation: MachineDocumentationFormGroupInput = { id: null },
  ): MachineDocumentationFormGroup {
    const machineDocumentationRawValue = {
      ...this.getFormDefaults(),
      ...machineDocumentation,
    };
    return new FormGroup<MachineDocumentationFormGroupContent>({
      id: new FormControl(
        { value: machineDocumentationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(machineDocumentationRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(machineDocumentationRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(machineDocumentationRawValue.type),
      description: new FormControl(machineDocumentationRawValue.description),
      url: new FormControl(machineDocumentationRawValue.url),
      machine: new FormControl(machineDocumentationRawValue.machine, {
        validators: [Validators.required],
      }),
    });
  }

  getMachineDocumentation(form: MachineDocumentationFormGroup): IMachineDocumentation | NewMachineDocumentation {
    return form.getRawValue() as IMachineDocumentation | NewMachineDocumentation;
  }

  resetForm(form: MachineDocumentationFormGroup, machineDocumentation: MachineDocumentationFormGroupInput): void {
    const machineDocumentationRawValue = { ...this.getFormDefaults(), ...machineDocumentation };
    form.reset(
      {
        ...machineDocumentationRawValue,
        id: { value: machineDocumentationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MachineDocumentationFormDefaults {
    return {
      id: null,
    };
  }
}
