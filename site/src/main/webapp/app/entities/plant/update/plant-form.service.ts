import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlant, NewPlant } from '../plant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlant for edit and NewPlantFormGroupInput for create.
 */
type PlantFormGroupInput = IPlant | PartialWithRequiredKeyOf<NewPlant>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlant | NewPlant> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type PlantFormRawValue = FormValueOf<IPlant>;

type NewPlantFormRawValue = FormValueOf<NewPlant>;

type PlantFormDefaults = Pick<NewPlant, 'id' | 'createdAt' | 'updatedAt'>;

type PlantFormGroupContent = {
  id: FormControl<PlantFormRawValue['id'] | NewPlant['id']>;
  reference: FormControl<PlantFormRawValue['reference']>;
  name: FormControl<PlantFormRawValue['name']>;
  description: FormControl<PlantFormRawValue['description']>;
  address: FormControl<PlantFormRawValue['address']>;
  location: FormControl<PlantFormRawValue['location']>;
  createdAt: FormControl<PlantFormRawValue['createdAt']>;
  updatedAt: FormControl<PlantFormRawValue['updatedAt']>;
  site: FormControl<PlantFormRawValue['site']>;
};

export type PlantFormGroup = FormGroup<PlantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlantFormService {
  createPlantFormGroup(plant: PlantFormGroupInput = { id: null }): PlantFormGroup {
    const plantRawValue = this.convertPlantToPlantRawValue({
      ...this.getFormDefaults(),
      ...plant,
    });
    return new FormGroup<PlantFormGroupContent>({
      id: new FormControl(
        { value: plantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(plantRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(plantRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(plantRawValue.description, {
        validators: [Validators.required],
      }),
      address: new FormControl(plantRawValue.address),
      location: new FormControl(plantRawValue.location),
      createdAt: new FormControl(plantRawValue.createdAt),
      updatedAt: new FormControl(plantRawValue.updatedAt),
      site: new FormControl(plantRawValue.site, {
        validators: [Validators.required],
      }),
    });
  }

  getPlant(form: PlantFormGroup): IPlant | NewPlant {
    return this.convertPlantRawValueToPlant(form.getRawValue() as PlantFormRawValue | NewPlantFormRawValue);
  }

  resetForm(form: PlantFormGroup, plant: PlantFormGroupInput): void {
    const plantRawValue = this.convertPlantToPlantRawValue({ ...this.getFormDefaults(), ...plant });
    form.reset(
      {
        ...plantRawValue,
        id: { value: plantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlantFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertPlantRawValueToPlant(rawPlant: PlantFormRawValue | NewPlantFormRawValue): IPlant | NewPlant {
    return {
      ...rawPlant,
      createdAt: dayjs(rawPlant.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawPlant.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertPlantToPlantRawValue(
    plant: IPlant | (Partial<NewPlant> & PlantFormDefaults),
  ): PlantFormRawValue | PartialWithRequiredKeyOf<NewPlantFormRawValue> {
    return {
      ...plant,
      createdAt: plant.createdAt ? plant.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: plant.updatedAt ? plant.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
