import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UtilityService } from './utility.service';
import { StateService } from './state.service';
import * as constants from './constants';
import {Title} from "@angular/platform-browser";

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DataService {

	title: string = '';
  	cardWidth: number = 320;

  	cardsData = [];
  	websocketSubscriptionObject;

  	sensorsList = [];

  	sensorIdsList = [];
  	batteryIdsList = [];
  	signalIdsList = [];
  	combinedIdsList = [];

  	exampleSocket;

  	constructor(private http: HttpClient, private stateService: StateService, private titleService: Title, private utlityService: UtilityService) { 

  	}

  	loadDashboardData(dashboardId) {
  		let self = this;
  		

  		let restServiceUrl = self.stateService.getState()['settings']['restServiceUrl'];

  		//console.log('URL for dashboard data: ', restServiceUrl + 'dashboard/' + dashboardId);

	  	self.http.get(restServiceUrl + 'dashboard/' + dashboardId)
	        .subscribe(
	          function(data) { 
	            //console.log('DataService.constructor - dashboard data: ', data);

	            if(data.hasOwnProperty('dashboard')) {
	            	if(data['dashboard'].hasOwnProperty('cards')) {
	            		self.title = data['dashboard']['title'];
	            		self.titleService.setTitle(data['dashboard']['title']);
	            		self.cardsData = JSON.parse(data['dashboard']['cards']);

	                	for(let c = 0; c < self.cardsData.length; c++) {
	                  		self.cardsData[c]['tempId'] = self.utlityService.generateUUID();
	                	}
	            	}
	            	
	            }

	            for(let i = 0; i < self.cardsData.length; i++) {
	            	let card = self.cardsData[i];
	            	//console.log('DataService.constructor - card: ', card);

	            	self.sensorsList.push(card);
	            	self.sensorIdsList.push(card.sensorId);

	            	if(self.combinedIdsList.indexOf(card.sensorId) == -1) {
	            		self.combinedIdsList.push(card.sensorId)
	            	}

	            	if(card.hasOwnProperty('batterySensorId')) {
	            		if(self.batteryIdsList.indexOf(card.batterySensorId) == -1) {
	            			self.batteryIdsList.push(card.batterySensorId)
	            		}

	            		if(self.combinedIdsList.indexOf(card.batterySensorId) == -1) {
	            			self.combinedIdsList.push(card.batterySensorId)
	            		}
	            	}

	            	if(card.hasOwnProperty('signalSensorId')) {

	            		if(self.signalIdsList.indexOf(card.signalSensorId) == -1) {
	            			self.signalIdsList.push(card.signalSensorId)
	            		}

	            		if(self.combinedIdsList.indexOf(card.signalSensorId) == -1) {
	            			self.combinedIdsList.push(card.signalSensorId)
	            		}
	            	}

	            	
	  				
	            }

	            let dashboardData = {
	              dashboardId: dashboardId,
	              title: self.title,
	              cards: self.cardsData,
	              sensorIdsList: self.sensorIdsList,
	              batteryIdsList: self.batteryIdsList,
	              signalIdsList: self.signalIdsList
	            }

	            //console.log('AppComponent.constructor - dashboardData: ', dashboardData);

	            self.stateService.executeAction(constants.DASHBOARD_SET_DATA, { dashboard: dashboardData });

	            self.processResize();

	            self.loadStartingData();
	           

	          },
	          function(error) {
	          	console.error('AppComponent.constructor - error: ', error);
	          }
	        );
  	}

  	loadStartingData() {
  		let self = this;

  		let restServiceUrl = self.stateService.getState()['settings']['restServiceUrl'];

  		let unconfiguredDataiourl= restServiceUrl + 'unconfigureddataio?ids=' + self.combinedIdsList.join(',');
  		//console.log('DataService.loadStartingData - URL for start unconfigured data: ', unconfiguredDataiourl);

  		let startDataArray = [];

	  	self.http.get(unconfiguredDataiourl)
	        .subscribe(
	          function(unconfiguredData) { 
	            //console.log('DataService.loadStartingData - start unconfigured data: ', unconfiguredData);

	            let unconfiguredResult = unconfiguredData['unConfiguredDataIos'];

	            

	            for(let i = 0; i < unconfiguredResult.length; i++) {
	            	let item = unconfiguredResult[i];
	            	let sensorDataObject = {
			        	sensorId: item['id'],
			        	sensorValue: item['lastValue'],
			        	sensorTimeStamp: item['lastRun'],
			        	sensorInterval: item['interval']
			        }

			        startDataArray.push(sensorDataObject)

			        //console.log('DataService.loadStartingData - sensorDataObject: ', sensorDataObject);
			        
	            }

	            let configuredDataIOUrl = restServiceUrl + 'dataconfiguration?ids=' + self.combinedIdsList.join(',');
	            //console.log('DataService.loadStartingData - URL for start configured data: ', configuredDataIOUrl);

	            self.http.get(configuredDataIOUrl)
	        		.subscribe(
	          			function(configuredData) { 
	            			//console.log('DataService.loadStartingData - start configured data: ', configuredData);

	            			let configuredResult = configuredData['configurations'];

	            			for(let c = 0; c < configuredResult.length; c++) {
	            				let configuredItem = configuredResult[c];
	            				let configuredItemID = configuredItem['id'];
	            				//console.log('DataService.loadStartingData - configuredItem: ', configuredItem);

	            				for(let a = 0; a < startDataArray.length; a++) {
	            					let startDataItem = startDataArray[a];
	            					//console.log('DataService.loadStartingData - startDataItem: ', startDataItem);

	            					if(configuredItemID == startDataItem['sensorId']) {
	            						startDataItem['calculationAndStores'] = configuredItem['calculationAndStores'];
	            					}
	            				}
	            			}

	            			self.stateService.executeAction(constants.SENSOR_DATA_ADD_START_DATA, { startDataArray: startDataArray });

	            			self.buildWebsocketSubscriptionObject();
	            		}
	            	);


	            
	        }
	    );

  		
  	}

  	buildWebsocketSubscriptionObject() {
  		let self = this;

  		let subscriptionObject = {};

  		subscriptionObject['type'] = 2;
  		
  		let subscriptionsArray = [];

  		for(let i = 0; i < self.sensorsList.length; i++) {
  			let sensorItem = self.sensorsList[i];

  			let sensorSubscriptionObject = {
  				hepwatDeviceId: sensorItem.sensorId,
		      	calctype: sensorItem.cardCalcuationType,
		      	aggtype: sensorItem.cardAggregationType,
		      	datatype: 0
  			}

  			subscriptionsArray.push(sensorSubscriptionObject);
  		}

  		for(let i = 0; i < self.batteryIdsList.length; i++) {
  			let batterySubscriptionObject = {
  				hepwatDeviceId: self.batteryIdsList[i],
		      	calctype: 0,
		      	aggtype: 0,
		      	datatype: 0
  			}

  			subscriptionsArray.push(batterySubscriptionObject);
  		}

  		for(let i = 0; i < self.signalIdsList.length; i++) {
  			let signalSubscriptionObject = {
  				hepwatDeviceId: self.signalIdsList[i],
		      	calctype: 0,
		      	aggtype: 0,
		      	datatype: 0
  			}

  			subscriptionsArray.push(signalSubscriptionObject);
  		}

		subscriptionObject['subscriptions'] = subscriptionsArray;

		self.websocketSubscriptionObject = subscriptionObject;

		self.initWebSocketConnection();
  	}

  	processResize() {
	  	let self = this;

	  	//console.log('AppComponent.processResize');

	  	let bodyWidth = document.body.scrollWidth;
	    //console.log('AppComponent.processResize - bodyWidth: ', bodyWidth);
	  	let maxRowCount = Math.floor(bodyWidth / self.cardWidth);

	  	if(self.cardsData.length < maxRowCount) {
	  		maxRowCount = self.cardsData.length;
	  	}

	  	//console.log('AppComponent.processResize - bodyWidth: ', bodyWidth);
	  	//console.log('AppComponent.processResize - maxRowCount: ', maxRowCount);

	  	let cardTotalWidth = maxRowCount * self.cardWidth;

	  	//console.log('AppComponent.processResize - cardTotalWidth: ', cardTotalWidth);

	  	let leftMargin = Math.floor((bodyWidth - cardTotalWidth) / 2);

	  	//console.log('AppComponent.processResize - leftMargin: ', leftMargin);

	  	let cardContainer = document.getElementsByClassName('card-container')[0]['style']['cssText'] = 'margin-left:' + leftMargin + 'px;'
	}

	initWebSocketConnection() {
	  	let self = this;

	  	//console.log('DataService.initWebSocketConnection - self.websocketSubscriptionObject: ', self.websocketSubscriptionObject);

	  	let webSocketUrl = self.stateService.getState()['settings']['webSocketUrl'];
	  	//console.log('DataService.initWebSocketConnection - webSocketUrl: ', webSocketUrl);

		self.exampleSocket = new WebSocket(webSocketUrl);

		self.exampleSocket.onopen = function(event) {
			//console.log('DataService.initWebSocketConnection - onopen, event: ', event);
			self.exampleSocket.send(JSON.stringify(self.websocketSubscriptionObject));
		}

	    self.exampleSocket.onmessage = function (event) {
	        //console.log('DataService.initWebSocketConnection - onmessage, event: ', event);

	        try {
		        let messageData = JSON.parse(event['data']);

		        let sensorDataObject = {
		        	sensorId: messageData['hepwatDeviceId'],
		        	sensorValue: messageData['value'],
		        	sensorTimeStamp: messageData['timestamp'],
		        	sensorInterval: messageData['interval'],
		        	calcType: (messageData.hasOwnProperty('calctype')) ? Number(messageData.calctype) : 0,
		        	aggType: (messageData.hasOwnProperty('aggtype')) ? Number(messageData.aggtype) : 0
		        }

		        self.stateService.executeAction(constants.SENSOR_DATA_ADD_DATA, { sensorDataObject: sensorDataObject });
		    }
		    catch(exception) {
		    	return false;
		    }
	    }
	}
}
