import {ChangeDetectionStrategy, Component, ElementRef, Input, OnChanges, ViewChild, OnInit} from '@angular/core';
import { UtilityService } from '../../services/utility.service';
import { StateService } from '../../services/state.service';
import * as constants from '../../services/constants';
import * as d3 from 'd3';

export  class DataPoint {
  timestamp: number;
  value: number;
}

@Component({
  selector: 'app-line-chart-standard',
  templateUrl: './line-chart-standard.component.html',
  styleUrls: ['./line-chart-standard.component.scss']
})
export class LineChartStandardComponent implements OnInit {
	state;
	titleValue: string;
	sensorId: number;
	tempId: string;
	calculationType: number = 0;
	aggregationType: number = 0;

	startDataCheckCycle;

	cardData;
  	cardDataLoaded:boolean = false;

	dataPoints: DataPoint[] = [];

	dataPointsMaxSize: number = 1200;

  	@ViewChild('chart')
  	chartElement: ElementRef;
  	chartBox;

  	parseDate = d3.timeParse('%d-%m-%Y');

  	@Input()

  	private svgElement: HTMLElement;
  	private chartProps: any;

  	sensorIdDefinedFlag: boolean = false;

  	constructor(private element: ElementRef, private stateService: StateService) { 
  		let self = this;

  		self.chartElement = element;
  		//console.log('LineChartStandardComponent.constructor - chartElement: ', self.chartElement);

  		self.stateService.observableEvents.subscribe(function(state) {
  			self.state = state;
  			//console.log('RawValueComponent.observableEvents (' + self.sensorId + ') - self.state: ', self.state);

        	self.updateData();
  		});
  	}

  	ngOnInit() {
  		let self = this;

  		//console.log('LineChartStandardComponent - ngOnInit');

      	self.beginStartDataCheckCycle();

  		self.chartElement = self.element;
  		//console.log('LineChartStandardComponent.ngOnInit - chartElement: ', self.chartElement);

  	}

  	ngAfterViewChecked() {
      let self = this;

      if(self.sensorIdDefinedFlag == false) {
        self.sensorId = self.element.nativeElement.getAttribute('data-sensorid');
        self.tempId = self.element.nativeElement.getAttribute('data-tempid');
        self.calculationType = Number(self.element.nativeElement.getAttribute('data-calculationtype'));
        self.aggregationType = Number(self.element.nativeElement.getAttribute('data-aggregationtype'));

        self.sensorIdDefinedFlag = true;

        self.chartBox = document.querySelectorAll('[data-tempid="' + self.tempId + '"]')[0].getElementsByClassName('line-chart-box')[0];
  		//console.log('LineChartStandardComponent.ngAfterViewChecked - chartBox: ', self.chartBox);

  		this.buildChart();
      }
    }
  
  	ngOnChanges() {
  		//this.buildChart();
	}

	beginStartDataCheckCycle() {
      let self = this;

      self.startDataCheckCycle = setInterval(function() {
        if( self.state['sensorStartData'].length > 0) {
          //console.log('LineChartStandardComponent.beginStartDataCheckCycle (' +  self.sensorId + ') - self.state.sensorStartData: ', self.state['sensorStartData']);

          self.readStartData();
        }
      }, 500);
    }

    readStartData() {
      let self = this;

      //console.log('LineChartStandardComponent.readStartData (' +  self.sensorId + ') - self.state.sensorStartData: ', self.state['sensorStartData']);

      self.fetchCardData(self.sensorId);

	}

	fetchCardData(sensorId) {
		let self = this;

  		//console.log('LineChartStandardComponent.fetchCardData - cards pile: ', self.state['dashboard']['cards']);

  		for(let i = 0; i < self.state['dashboard']['cards'].length; i++) {
  			let cardCandidate = self.state['dashboard']['cards'][i];
  			if(cardCandidate.tempId == self.tempId) {
  				self.cardData = cardCandidate;
  				//console.log('LineChartStandardComponent.fetchCardData - card HIT: ', self.cardData);
  				self.cardDataLoaded = true;
  				self.loadCard();
  				break;
  			}
  		}
  	}

  	loadCard() {
  		let self = this;

  		//console.log('LineChartStandardComponent.loadCard - card: ', self.cardData);

  		if(self.cardData.hasOwnProperty('title')) {
  			if(self.cardData['title'] != null) {
  				self.titleValue = self.cardData['title'];
  			}
  		}
  	}

	updateData() {
		let self = this;

 	    let data = self.state['sensorData']

        // set sensor value
        if(data['sensorId'] == self.sensorId && data['calcType'] == self.calculationType && data['aggType'] == self.aggregationType) {
	        //console.log('LineChartStandardComponent.updateData - (' + self.sensorId + ') card data: ', data);

	        let sensorValue = data['sensorValue'];
	        let lastRunRawValue = data['sensorTimeStamp']; // convert

	        

	        let date = new Date(lastRunRawValue);
	        let hour = date.getHours();
	        let minute = date.getMinutes();
	        let time = hour + ':' + minute;

			

			let dataPoint: DataPoint = { value: sensorValue, timestamp: lastRunRawValue };

	        self.dataPoints.push(dataPoint);

	        if( self.dataPoints.length == (self.dataPointsMaxSize + 1)) {
	        	self.dataPoints.shift();
	        }

	        //console.log('LineChartStandardComponent.updateData - self.dataPoints: ', self.dataPoints);

	        self.updateChart();
	    }
	}

  	buildChart() {
  		let self = this;

	  	this.chartProps = {};

	  	//console.log('LineChartStandardComponent - buildChart');

	  	// Set the dimensions of the canvas / graph
	  	var margin = { top: 0, right: 35, bottom: 20, left: 28 };
	  	var width = 268 - margin.left - 10;
	  	//var height = 268 - margin.bottom - 40;
	  	var height = 248 - margin.bottom - 40;

	  	// Set the ranges
	  	this.chartProps.x = d3.scaleTime().range([0, width]);
	  	this.chartProps.y = d3.scaleLinear().range([height, 0]);

	  	// Define the axes
	  	var xMin = (self.dataPoints.length > 0) ? self.dataPoints[0].timestamp : new Date().getTime();
    	var xMax = (self.dataPoints.length > 0) ? self.dataPoints[self.dataPoints.length - 1].timestamp : new Date().getTime();

	  	//var xAxis = d3.axisBottom(this.chartProps.x).ticks(d3.timeMinute.every(5)).tickFormat(d3.timeFormat("%H:%M"));
	  	var xAxis = d3.axisBottom(this.chartProps.x).tickValues([xMin, xMax]).tickFormat(d3.timeFormat("%H:%M"));;
	  	var yAxis = d3.axisLeft(this.chartProps.y).ticks(5);

	  	let _this = this;

	  	// Define the line
	  	var valueline = d3.line<DataPoint>()
	    	.x(function (d) {
	      		return _this.chartProps.x(d.timestamp); 
	    	})
	    	.y(function (d) { 
	    		return _this.chartProps.y(d.value); 
	    	});

	  	//var svg = d3.select(self.chartElement.nativeElement)
	  	var svg = d3.select(self.chartBox)

		    .append('svg')
		    .attr('width', width + margin.left + margin.right)
		    .attr('height', height + margin.top + margin.bottom)
		    .append('g')
		    .attr('transform', `translate(${margin.left},${margin.top})`);

		  	// Scale the range of the data
		  	this.chartProps.x.domain(
		    	d3.extent(_this.dataPoints, function (d) {
		    		return d.timestamp
		    	}));
		  
		  	this.chartProps.y.domain(
		  		[
		  			d3.min(
		  				this.dataPoints, 
		  				function (d) { 
		  					return Math.min(d.value) - 1;
		  				}
		  			), 
		  			d3.max(
		  				this.dataPoints, 
		  				function (d) { 
		  					return Math.max(d.value) + 1;
		  				}
		  			)
		  		]
			);

	  // Add the valueline2 path.
	  /*svg.append('path')
	    .attr('class', 'line line2')
	    .style('stroke', 'green')
	    .style('fill', 'none')
	    .attr('d', valueline2(_this.marketPositions));
	    */

	  // Add the valueline path.
	  svg.append('path')
	    .attr('class', 'line line1')
	    .style('stroke', 'black')
	    .style('fill', 'none')
	    .attr('d', valueline(_this.dataPoints));


	  // Add the X Axis
	  svg.append('g')
	    .attr('class', 'x axis')
	    .attr('transform', `translate(0,${height})`)
	    .call(xAxis);

	  // Add the Y Axis
	  svg.append('g')
	    .attr('class', 'y axis')
	    .call(yAxis);

	  // Setting the required objects in chartProps so they could be used to update the chart
	  this.chartProps.svg = svg;
	  this.chartProps.valueline = valueline;
	  //this.chartProps.valueline2 = valueline2;
	  this.chartProps.xAxis = xAxis;
	  this.chartProps.yAxis = yAxis;
	}

	updateChart() {
		let self = this;

		self.chartProps = {};

	  	// Set the dimensions of the canvas / graph
	  	var margin = { top: 0, right: 35, bottom: 20, left: 28 };
	  	var width = 268 - margin.left - 10;
	  	var height = 248 - margin.bottom - 40;

	  	// Set the ranges
	  	this.chartProps.x = d3.scaleTime().range([0, width]);
	  	this.chartProps.y = d3.scaleLinear().range([height, 0]);

	  	// Define the axes
	  	var xMin = (self.dataPoints.length > 0) ? self.dataPoints[0].timestamp : new Date().getTime();
    	var xMax = (self.dataPoints.length > 0) ? self.dataPoints[self.dataPoints.length - 1].timestamp : new Date().getTime();

	  	//var xAxis = d3.axisBottom(this.chartProps.x).ticks(d3.timeMinute.every(5)).tickFormat(d3.timeFormat("%H:%M"));
	  	var xAxis = d3.axisBottom(this.chartProps.x).tickValues([xMin, xMax]).tickFormat(d3.timeFormat("%H:%M"));;
	  	var yAxis = d3.axisLeft(this.chartProps.y).ticks(5);

		var svg = d3.select(self.chartBox);

		  	// Scale the range of the data
		  	this.chartProps.x.domain(
		    	d3.extent(self.dataPoints, function (d) {
		    		return d.timestamp
		    	}));
		  
		  	this.chartProps.y.domain(
		  		[
		  			d3.min(
		  				this.dataPoints, 
		  				function (d) { 
		  					return Math.min(d.value) - 1;
		  				}
		  			), 
		  			d3.max(
		  				this.dataPoints, 
		  				function (d) { 
		  					return Math.max(d.value) + 1;
		  				}
		  			)
		  		]
			);


	  	// Add the valueline path.
	  	var valueline = d3.line<DataPoint>()
	    	.x(function (d) {
	      		return self.chartProps.x(d.timestamp); 
	    	})
	    	.y(function (d) { 
	    		return self.chartProps.y(d.value); 
	    	});

	    svg.select(".line.line1")   // change the line
            .attr("d", valueline(self.dataPoints));
        svg.select(".x.axis") // change the x axis
            .call(xAxis);
        svg.select(".y.axis") // change the y axis
            .call(yAxis);

	    /*
	  	svg.append('path')
		    .attr('class', 'line line1')
		    .style('stroke', 'black')
		    .style('fill', 'none')
		    .attr('d', valueline(self.dataPoints));


	  	// Add the X Axis
	  	svg.append('g')
		    .attr('class', 'x axis')
		    .attr('transform', `translate(0,${height})`)
		    .call(xAxis);

	  	// Add the Y Axis
	  	svg.append('g')
		    .attr('class', 'y axis')
		    .call(yAxis);

	    */

	  // Setting the required objects in chartProps so they could be used to update the chart
	  this.chartProps.svg = svg;
	  this.chartProps.valueline = valueline;
	  //this.chartProps.valueline2 = valueline2;
	  
	  this.chartProps.xAxis = xAxis;
	  this.chartProps.yAxis = yAxis;
	}
}
