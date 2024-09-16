import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AmministratoridettaglioComponent } from './amministratoridettaglio.component';

describe('AmministratoridettaglioComponent', () => {
  let component: AmministratoridettaglioComponent;
  let fixture: ComponentFixture<AmministratoridettaglioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AmministratoridettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmministratoridettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
