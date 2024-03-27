import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MachineDocumentationDetailComponent } from './machine-documentation-detail.component';

describe('MachineDocumentation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MachineDocumentationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MachineDocumentationDetailComponent,
              resolve: { machineDocumentation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MachineDocumentationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load machineDocumentation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MachineDocumentationDetailComponent);

      // THEN
      expect(instance.machineDocumentation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
