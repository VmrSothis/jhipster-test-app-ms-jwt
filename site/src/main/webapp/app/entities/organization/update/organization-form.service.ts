import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrganization, NewOrganization } from '../organization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganization for edit and NewOrganizationFormGroupInput for create.
 */
type OrganizationFormGroupInput = IOrganization | PartialWithRequiredKeyOf<NewOrganization>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrganization | NewOrganization> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type OrganizationFormRawValue = FormValueOf<IOrganization>;

type NewOrganizationFormRawValue = FormValueOf<NewOrganization>;

type OrganizationFormDefaults = Pick<NewOrganization, 'id' | 'createdAt' | 'updatedAt'>;

type OrganizationFormGroupContent = {
  id: FormControl<OrganizationFormRawValue['id'] | NewOrganization['id']>;
  reference: FormControl<OrganizationFormRawValue['reference']>;
  name: FormControl<OrganizationFormRawValue['name']>;
  legalName: FormControl<OrganizationFormRawValue['legalName']>;
  description: FormControl<OrganizationFormRawValue['description']>;
  taxId: FormControl<OrganizationFormRawValue['taxId']>;
  email: FormControl<OrganizationFormRawValue['email']>;
  telephone: FormControl<OrganizationFormRawValue['telephone']>;
  url: FormControl<OrganizationFormRawValue['url']>;
  address: FormControl<OrganizationFormRawValue['address']>;
  postalCode: FormControl<OrganizationFormRawValue['postalCode']>;
  region: FormControl<OrganizationFormRawValue['region']>;
  locality: FormControl<OrganizationFormRawValue['locality']>;
  country: FormControl<OrganizationFormRawValue['country']>;
  location: FormControl<OrganizationFormRawValue['location']>;
  createdAt: FormControl<OrganizationFormRawValue['createdAt']>;
  updatedAt: FormControl<OrganizationFormRawValue['updatedAt']>;
};

export type OrganizationFormGroup = FormGroup<OrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationFormService {
  createOrganizationFormGroup(organization: OrganizationFormGroupInput = { id: null }): OrganizationFormGroup {
    const organizationRawValue = this.convertOrganizationToOrganizationRawValue({
      ...this.getFormDefaults(),
      ...organization,
    });
    return new FormGroup<OrganizationFormGroupContent>({
      id: new FormControl(
        { value: organizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(organizationRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(organizationRawValue.name, {
        validators: [Validators.required],
      }),
      legalName: new FormControl(organizationRawValue.legalName),
      description: new FormControl(organizationRawValue.description),
      taxId: new FormControl(organizationRawValue.taxId),
      email: new FormControl(organizationRawValue.email),
      telephone: new FormControl(organizationRawValue.telephone),
      url: new FormControl(organizationRawValue.url),
      address: new FormControl(organizationRawValue.address),
      postalCode: new FormControl(organizationRawValue.postalCode),
      region: new FormControl(organizationRawValue.region),
      locality: new FormControl(organizationRawValue.locality),
      country: new FormControl(organizationRawValue.country),
      location: new FormControl(organizationRawValue.location),
      createdAt: new FormControl(organizationRawValue.createdAt),
      updatedAt: new FormControl(organizationRawValue.updatedAt),
    });
  }

  getOrganization(form: OrganizationFormGroup): IOrganization | NewOrganization {
    return this.convertOrganizationRawValueToOrganization(form.getRawValue() as OrganizationFormRawValue | NewOrganizationFormRawValue);
  }

  resetForm(form: OrganizationFormGroup, organization: OrganizationFormGroupInput): void {
    const organizationRawValue = this.convertOrganizationToOrganizationRawValue({ ...this.getFormDefaults(), ...organization });
    form.reset(
      {
        ...organizationRawValue,
        id: { value: organizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertOrganizationRawValueToOrganization(
    rawOrganization: OrganizationFormRawValue | NewOrganizationFormRawValue,
  ): IOrganization | NewOrganization {
    return {
      ...rawOrganization,
      createdAt: dayjs(rawOrganization.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawOrganization.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertOrganizationToOrganizationRawValue(
    organization: IOrganization | (Partial<NewOrganization> & OrganizationFormDefaults),
  ): OrganizationFormRawValue | PartialWithRequiredKeyOf<NewOrganizationFormRawValue> {
    return {
      ...organization,
      createdAt: organization.createdAt ? organization.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: organization.updatedAt ? organization.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
