import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LineChartStandardComponent } from './line-chart-standard.component';

describe('LineChartStandardComponent', () => {
  let component: LineChartStandardComponent;
  let fixture: ComponentFixture<LineChartStandardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LineChartStandardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LineChartStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
