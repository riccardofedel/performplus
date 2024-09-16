import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { UtentidettaglioComponent } from './utentidettaglio.component';

describe('UtentidettaglioComponent', () => {
  let component: UtentidettaglioComponent;
  let fixture: ComponentFixture<UtentidettaglioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ UtentidettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UtentidettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
