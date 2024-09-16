import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RisorseComponent } from './risorse.component';

describe('RisorseComponent', () => {
  let component: RisorseComponent;
  let fixture: ComponentFixture<RisorseComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RisorseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RisorseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
