import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BreadcrumbBarComponent } from './breadcrumb-bar.component';

describe('BreadcrumbBarComponent', () => {
  let component: BreadcrumbBarComponent;
  let fixture: ComponentFixture<BreadcrumbBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BreadcrumbBarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BreadcrumbBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
