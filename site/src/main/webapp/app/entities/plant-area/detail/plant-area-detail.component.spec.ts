import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlantAreaDetailComponent } from './plant-area-detail.component';

describe('PlantArea Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlantAreaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlantAreaDetailComponent,
              resolve: { plantArea: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlantAreaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load plantArea on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlantAreaDetailComponent);

      // THEN
      expect(instance.plantArea).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
