import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { SchedaAlberoComponent } from './scheda-albero.component';



describe('SchedaAlberoComponent', () => {
  let component: SchedaAlberoComponent;
  let fixture: ComponentFixture<SchedaAlberoComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SchedaAlberoComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaAlberoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
