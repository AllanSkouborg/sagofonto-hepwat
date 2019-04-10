import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { HttpClientModule } from '@angular/common/http';

import {
	MatButtonModule, 
	MatCheckboxModule, 
	MatToolbarModule,
	MatCardModule,
	MatIconModule,
	MatIconRegistry
} from '@angular/material';

import { LineChartStandardComponent } from './data-cards/line-chart-standard/line-chart-standard.component';
import { RawValueComponent } from './data-cards/raw-value/raw-value.component';

import { D3Service } from './d3/d3.service';
import { D3_DIRECTIVES } from './d3/directives/index';

import { GraphComponent } from './visuals/graph/graph.component';
import { SHARED_VISUALS } from './visuals/shared';

@NgModule({
  declarations: [
    AppComponent,
    RawValueComponent,
    LineChartStandardComponent,

    GraphComponent,
    ...SHARED_VISUALS,
    ...D3_DIRECTIVES
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,

    MatButtonModule, 
    MatCheckboxModule,
    MatToolbarModule,
    MatCardModule,
    MatIconModule

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    MatIconRegistry,
    D3Service
  ],
  bootstrap: [AppComponent]
})
export class AppModule { 
	constructor(public matIconRegistry: MatIconRegistry) {
		matIconRegistry.registerFontClassAlias('fontawesome', 'fa');
	}

}
