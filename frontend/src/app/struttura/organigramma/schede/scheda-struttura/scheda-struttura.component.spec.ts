import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaStrutturaComponent } from './scheda-struttura.component';

describe("SchedaNodoComponent", () => {
  let component: SchedaStrutturaComponent;
  let fixture: ComponentFixture<SchedaStrutturaComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaStrutturaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaStrutturaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
