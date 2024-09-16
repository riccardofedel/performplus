import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SchedaConsuntivazioneComponent } from './scheda-consuntivazione.component';

describe('ConsuntivazioneComponent', () => {
  let component: SchedaConsuntivazioneComponent;
  let fixture: ComponentFixture<SchedaConsuntivazioneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaConsuntivazioneComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaConsuntivazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
