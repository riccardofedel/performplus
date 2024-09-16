import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaIndicatoriComponent } from './scheda-indicatori.component';

describe('SchedaIndicatoriComponent', () => {
  let component: SchedaIndicatoriComponent;
  let fixture: ComponentFixture<SchedaIndicatoriComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaIndicatoriComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaIndicatoriComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
