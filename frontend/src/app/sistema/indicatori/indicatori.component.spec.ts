import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IndicatoriComponent } from './indicatori.component';

describe('IndicatoriComponent', () => {
  let component: IndicatoriComponent;
  let fixture: ComponentFixture<IndicatoriComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ IndicatoriComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndicatoriComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
