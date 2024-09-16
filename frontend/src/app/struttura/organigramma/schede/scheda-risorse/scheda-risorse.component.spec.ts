import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { SchedaRisorseComponent } from './scheda-risorse.component';


describe('SchedaRisorseComponent', () => {
  let component: SchedaRisorseComponent;
  let fixture: ComponentFixture<SchedaRisorseComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SchedaRisorseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaRisorseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
