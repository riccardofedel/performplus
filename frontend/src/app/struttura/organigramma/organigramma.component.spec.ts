import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { OrganigrammaComponent } from './organigramma.component';


describe('RisorseComponent', () => {
  let component: OrganigrammaComponent;
  let fixture: ComponentFixture<OrganigrammaComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [OrganigrammaComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganigrammaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
