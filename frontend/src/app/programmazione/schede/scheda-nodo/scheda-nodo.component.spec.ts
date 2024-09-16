import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaNodoComponent } from './scheda-nodo.component';

describe("SchedaNodoComponent", () => {
  let component: SchedaNodoComponent;
  let fixture: ComponentFixture<SchedaNodoComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaNodoComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaNodoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
