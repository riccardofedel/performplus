import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ProgrammazioneComponent } from './programmazione.component';



describe('NodoComponent', () => {
  let component: ProgrammazioneComponent;
  let fixture: ComponentFixture<ProgrammazioneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ProgrammazioneComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgrammazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
