import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RawValueComponent } from './raw-value.component';

describe('RawValueComponent', () => {
  let component: RawValueComponent;
  let fixture: ComponentFixture<RawValueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RawValueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RawValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
