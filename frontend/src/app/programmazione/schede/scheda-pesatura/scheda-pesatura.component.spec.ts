import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaPesaturaComponent } from './scheda-pesatura.component';

describe('SchedaPesaturaComponent', () => {
  let component: SchedaPesaturaComponent;
  let fixture: ComponentFixture<SchedaPesaturaComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaPesaturaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaPesaturaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
