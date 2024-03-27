import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MachineModelDetailComponent } from './machine-model-detail.component';

describe('MachineModel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MachineModelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MachineModelDetailComponent,
              resolve: { machineModel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MachineModelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load machineModel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MachineModelDetailComponent);

      // THEN
      expect(instance.machineModel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
