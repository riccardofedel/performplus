import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IndicatoridettaglioComponent } from './indicatoridettaglio.component';

describe('IndicatoridettaglioComponent', () => {
  let component: IndicatoridettaglioComponent;
  let fixture: ComponentFixture<IndicatoridettaglioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ IndicatoridettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndicatoridettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
