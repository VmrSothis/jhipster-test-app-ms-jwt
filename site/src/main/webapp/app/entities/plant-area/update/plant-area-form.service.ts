import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlantArea, NewPlantArea } from '../plant-area.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlantArea for edit and NewPlantAreaFormGroupInput for create.
 */
type PlantAreaFormGroupInput = IPlantArea | PartialWithRequiredKeyOf<NewPlantArea>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlantArea | NewPlantArea> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type PlantAreaFormRawValue = FormValueOf<IPlantArea>;

type NewPlantAreaFormRawValue = FormValueOf<NewPlantArea>;

type PlantAreaFormDefaults = Pick<NewPlantArea, 'id' | 'createdAt' | 'updatedAt'>;

type PlantAreaFormGroupContent = {
  id: FormControl<PlantAreaFormRawValue['id'] | NewPlantArea['id']>;
  reference: FormControl<PlantAreaFormRawValue['reference']>;
  name: FormControl<PlantAreaFormRawValue['name']>;
  description: FormControl<PlantAreaFormRawValue['description']>;
  createdAt: FormControl<PlantAreaFormRawValue['createdAt']>;
  updatedAt: FormControl<PlantAreaFormRawValue['updatedAt']>;
  plant: FormControl<PlantAreaFormRawValue['plant']>;
};

export type PlantAreaFormGroup = FormGroup<PlantAreaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlantAreaFormService {
  createPlantAreaFormGroup(plantArea: PlantAreaFormGroupInput = { id: null }): PlantAreaFormGroup {
    const plantAreaRawValue = this.convertPlantAreaToPlantAreaRawValue({
      ...this.getFormDefaults(),
      ...plantArea,
    });
    return new FormGroup<PlantAreaFormGroupContent>({
      id: new FormControl(
        { value: plantAreaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(plantAreaRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(plantAreaRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(plantAreaRawValue.description),
      createdAt: new FormControl(plantAreaRawValue.createdAt),
      updatedAt: new FormControl(plantAreaRawValue.updatedAt),
      plant: new FormControl(plantAreaRawValue.plant, {
        validators: [Validators.required],
      }),
    });
  }

  getPlantArea(form: PlantAreaFormGroup): IPlantArea | NewPlantArea {
    return this.convertPlantAreaRawValueToPlantArea(form.getRawValue() as PlantAreaFormRawValue | NewPlantAreaFormRawValue);
  }

  resetForm(form: PlantAreaFormGroup, plantArea: PlantAreaFormGroupInput): void {
    const plantAreaRawValue = this.convertPlantAreaToPlantAreaRawValue({ ...this.getFormDefaults(), ...plantArea });
    form.reset(
      {
        ...plantAreaRawValue,
        id: { value: plantAreaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlantAreaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertPlantAreaRawValueToPlantArea(rawPlantArea: PlantAreaFormRawValue | NewPlantAreaFormRawValue): IPlantArea | NewPlantArea {
    return {
      ...rawPlantArea,
      createdAt: dayjs(rawPlantArea.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawPlantArea.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertPlantAreaToPlantAreaRawValue(
    plantArea: IPlantArea | (Partial<NewPlantArea> & PlantAreaFormDefaults),
  ): PlantAreaFormRawValue | PartialWithRequiredKeyOf<NewPlantAreaFormRawValue> {
    return {
      ...plantArea,
      createdAt: plantArea.createdAt ? plantArea.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: plantArea.updatedAt ? plantArea.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
