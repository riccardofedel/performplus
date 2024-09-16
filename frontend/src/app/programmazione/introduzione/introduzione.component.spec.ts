import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { IntroduzioneComponent } from './introduzione.component';

describe('IntroduzioneComponent', () => {
  let component: IntroduzioneComponent;
  let fixture: ComponentFixture<IntroduzioneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ IntroduzioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IntroduzioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
