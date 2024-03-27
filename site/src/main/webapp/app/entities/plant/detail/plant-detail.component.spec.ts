import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlantDetailComponent } from './plant-detail.component';

describe('Plant Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlantDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlantDetailComponent,
              resolve: { plant: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlantDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load plant on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlantDetailComponent);

      // THEN
      expect(instance.plant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
