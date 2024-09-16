import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RegistrazionidettaglioComponent } from './registrazionidettaglio.component';

describe('RegistrazionidettaglioComponent', () => {
  let component: RegistrazionidettaglioComponent;
  let fixture: ComponentFixture<RegistrazionidettaglioComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RegistrazionidettaglioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrazionidettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
