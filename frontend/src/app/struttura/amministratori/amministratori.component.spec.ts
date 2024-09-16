import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AmministratoriComponent } from './amministratori.component';

describe('AmministratoriComponent', () => {
  let component: AmministratoriComponent;
  let fixture: ComponentFixture<AmministratoriComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AmministratoriComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmministratoriComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
