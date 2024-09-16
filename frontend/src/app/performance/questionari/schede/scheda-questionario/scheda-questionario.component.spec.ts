import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaQuestionarioComponent } from './scheda-questionario.component';

describe('ConsuntivazioneComponent', () => {
  let component: SchedaQuestionarioComponent;
  let fixture: ComponentFixture<SchedaQuestionarioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaQuestionarioComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaQuestionarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
