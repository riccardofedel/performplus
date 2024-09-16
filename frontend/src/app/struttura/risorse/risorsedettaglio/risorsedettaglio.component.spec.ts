import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RisorsedettaglioComponent } from './risorsedettaglio.component';

describe('RisorsedettaglioComponent', () => {
  let component: RisorsedettaglioComponent;
  let fixture: ComponentFixture<RisorsedettaglioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RisorsedettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RisorsedettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
