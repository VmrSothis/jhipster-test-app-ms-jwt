import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISite, NewSite } from '../site.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISite for edit and NewSiteFormGroupInput for create.
 */
type SiteFormGroupInput = ISite | PartialWithRequiredKeyOf<NewSite>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISite | NewSite> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type SiteFormRawValue = FormValueOf<ISite>;

type NewSiteFormRawValue = FormValueOf<NewSite>;

type SiteFormDefaults = Pick<NewSite, 'id' | 'createdAt' | 'updatedAt'>;

type SiteFormGroupContent = {
  id: FormControl<SiteFormRawValue['id'] | NewSite['id']>;
  reference: FormControl<SiteFormRawValue['reference']>;
  name: FormControl<SiteFormRawValue['name']>;
  description: FormControl<SiteFormRawValue['description']>;
  email: FormControl<SiteFormRawValue['email']>;
  telephone: FormControl<SiteFormRawValue['telephone']>;
  address: FormControl<SiteFormRawValue['address']>;
  postalCode: FormControl<SiteFormRawValue['postalCode']>;
  region: FormControl<SiteFormRawValue['region']>;
  locality: FormControl<SiteFormRawValue['locality']>;
  country: FormControl<SiteFormRawValue['country']>;
  location: FormControl<SiteFormRawValue['location']>;
  createdAt: FormControl<SiteFormRawValue['createdAt']>;
  updatedAt: FormControl<SiteFormRawValue['updatedAt']>;
  organization: FormControl<SiteFormRawValue['organization']>;
};

export type SiteFormGroup = FormGroup<SiteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SiteFormService {
  createSiteFormGroup(site: SiteFormGroupInput = { id: null }): SiteFormGroup {
    const siteRawValue = this.convertSiteToSiteRawValue({
      ...this.getFormDefaults(),
      ...site,
    });
    return new FormGroup<SiteFormGroupContent>({
      id: new FormControl(
        { value: siteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(siteRawValue.reference, {
        validators: [Validators.required],
      }),
      name: new FormControl(siteRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(siteRawValue.description),
      email: new FormControl(siteRawValue.email),
      telephone: new FormControl(siteRawValue.telephone),
      address: new FormControl(siteRawValue.address),
      postalCode: new FormControl(siteRawValue.postalCode),
      region: new FormControl(siteRawValue.region),
      locality: new FormControl(siteRawValue.locality),
      country: new FormControl(siteRawValue.country),
      location: new FormControl(siteRawValue.location),
      createdAt: new FormControl(siteRawValue.createdAt),
      updatedAt: new FormControl(siteRawValue.updatedAt),
      organization: new FormControl(siteRawValue.organization, {
        validators: [Validators.required],
      }),
    });
  }

  getSite(form: SiteFormGroup): ISite | NewSite {
    return this.convertSiteRawValueToSite(form.getRawValue() as SiteFormRawValue | NewSiteFormRawValue);
  }

  resetForm(form: SiteFormGroup, site: SiteFormGroupInput): void {
    const siteRawValue = this.convertSiteToSiteRawValue({ ...this.getFormDefaults(), ...site });
    form.reset(
      {
        ...siteRawValue,
        id: { value: siteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SiteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertSiteRawValueToSite(rawSite: SiteFormRawValue | NewSiteFormRawValue): ISite | NewSite {
    return {
      ...rawSite,
      createdAt: dayjs(rawSite.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawSite.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertSiteToSiteRawValue(
    site: ISite | (Partial<NewSite> & SiteFormDefaults),
  ): SiteFormRawValue | PartialWithRequiredKeyOf<NewSiteFormRawValue> {
    return {
      ...site,
      createdAt: site.createdAt ? site.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: site.updatedAt ? site.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
