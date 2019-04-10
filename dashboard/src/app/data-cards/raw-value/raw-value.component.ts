import { Component, OnInit, ElementRef } from '@angular/core';
import { UtilityService } from '../../services/utility.service';
import { StateService } from '../../services/state.service';
import * as constants from '../../services/constants';

@Component({
  selector: 'app-raw-value',
  templateUrl: './raw-value.component.html',
  styleUrls: ['./raw-value.component.scss']
})
export class RawValueComponent implements OnInit {
	state;
	element: ElementRef;
	sensorId;
	sensorIdLoaded: boolean = false;
	cardsData;
	cardData;
	cardDataLoaded: boolean = false;

	startDataCheckCycle;

	// Card settings
	lastCycleId: string;
	lastCycleSensorValues: any = [];
	lastCycleBatteryValues: any = [];

	tempId: string;
	titleValue: string;
	unitValue: string;
	sensorValue: string;
  maxNumberOfDecimals: number = 3;
  calculationType: number = 0;
  aggregationType: number = 0;
	lastRunRawValue: number;
	lastRunStringValue: string;
	lastRunVisible: boolean = false;

	batteryVisible: boolean = false;
	batterySensorId: number;
	batteryLevelValue: string;
  batteryLevel: number;
  batteryLevelClass: string;
  batteryAlarmLevelLow: number;
  batteryAlarmLevelMin: number;

  signalVisible: boolean = false;
  signalSensorId: number;
  signalLevelValue: string;
  signalLevel: number;
  signalLevelClass: string;
  signalAlarmLevelLow: number;
  signalAlarmLevelMin: number;

  alarmStatusOn: boolean = false;
  alarmStatus: string = ''; // options: alarmMax, alarmHigh, alarmLow, alarmMin, alarmOffline
  alarmMax: number;
  alarmHigh: number;
  alarmLow: number;
  alarmMin: number;

	//batteryLevelValueWidth: number;
	//batteryLevelValueColor: string;

  // flags
  sensorIdDefinedFlag: boolean = false;
  startDataReadFlag: boolean = false;

	constructor(element: ElementRef, private stateService: StateService, private utilityService: UtilityService) { 
		let self = this;

		self.element = element;

		//console.log('RawValueComponent.constructor (' + self.sensorId + ')  - element: ', element);

		if(self.state == null) {
			self.state = self.stateService.getState();
			//console.log('RawValueComponent.constructor (' + self.sensorId + ')  - state: ', self.state);

			if(self.state['dashboard'].hasOwnProperty('cards')) {
			  self.cardsData = self.state['dashboard']['cards'];
			  //console.log('RawValueComponent.constructor (' + self.sensorId + ') - self.cardsData: ', self.cardsData);
		  }
		}

		self.stateService.observableEvents.subscribe(function(state) {
			self.state = state;
			//console.log('RawValueComponent.observableEvents (' + self.sensorId + ') - self.state: ', self.state);

      self.updateData();

		});

	}

	ngOnInit() {
		let self = this;

    self.beginStartDataCheckCycle();
	}

  ngAfterViewChecked() {
    let self = this;

    if(self.sensorIdDefinedFlag == false) {
      self.sensorId = self.element.nativeElement.getAttribute('data-sensorid');
      self.tempId = self.element.nativeElement.getAttribute('data-tempid');
      self.calculationType = Number(self.element.nativeElement.getAttribute('data-calculationtype'));
      self.aggregationType = Number(self.element.nativeElement.getAttribute('data-aggregationtype'));
      
      self.sensorIdDefinedFlag = true;

      self.batterySensorId = self.element.nativeElement.getAttribute('data-batterysensorid');
      if(self.batterySensorId != null) {
        self.batteryVisible = true;
      }

      self.signalSensorId = self.element.nativeElement.getAttribute('data-signalsensorid');
      if(self.signalSensorId != null) {
        self.signalVisible = true;
      }

      //console.log('RawValueComponent.ngAfterViewChecked - batterySensorId: ', self.batterySensorId);
      //console.log('RawValueComponent.ngAfterViewChecked - signalSensorId: ', self.signalSensorId);
      //console.log('RawValueComponent.ngAfterViewChecked - calculationType: ', self.calculationType);
      //console.log('RawValueComponent.ngAfterViewChecked - aggregationType: ', self.aggregationType);
    }
  }

  beginStartDataCheckCycle() {
    let self = this;

    self.startDataCheckCycle = setInterval(function() {
      if( self.state['sensorStartData'].length > 0) {
        //console.log('RawValueComponent.beginStartDataCheckCycle (' +  self.sensorId + ') - self.state.sensorStartData: ', self.state['sensorStartData']);

        self.readStartData();
      }
    }, 500);
  }

  readStartData() {
    let self = this;

    //console.log('RawValueComponent.readStartData (' +  self.sensorId + ') - self.state.sensorStartData: ', self.state['sensorStartData']);

    self.fetchCardData(self.sensorId);

    let startData = self.state['sensorStartData'];

    // get sensor value
    for( let i = 0; i < startData.length; i++ ) {
      let startDataItem = startData[i];

      if(startDataItem['sensorId'] == self.sensorId) {
        //console.log('RawValueComponent.readStartData - (' + self.sensorId + ') card data: ', startDataItem);

        //self.sensorValue = startDataItem['sensorValue'];
        //self.lastRunRawValue = startDataItem['sensorTimeStamp']; // convert
        //self.lastRunStringValue = self.utilityService.dateConversionDayTime(self.lastRunRawValue);

        if( startDataItem.hasOwnProperty('calculationAndStores') ) {
          let calcStoreArray = startDataItem['calculationAndStores']; 
          for(let c = 0; c < calcStoreArray.length; c++) {
            let calcStore = calcStoreArray[c];
            if(calcStore['calculation'] == self.calculationType) {
              let firstAggStore = calcStore['aggregationAndStores'][0];
              if(firstAggStore['statusOn'] == true) {
                //console.log('RawValueComponent.readStartData - (' + self.sensorId + ') firstAggStore active: ', firstAggStore);

                self.alarmStatusOn = true;

                if(firstAggStore.hasOwnProperty('max')) {
                  self.alarmMax = firstAggStore['max'];
                }
                if(firstAggStore.hasOwnProperty('high')) {
                  self.alarmHigh = firstAggStore['high'];
                }
                if(firstAggStore.hasOwnProperty('low')) {
                  self.alarmLow = firstAggStore['low'];
                }
                if(firstAggStore.hasOwnProperty('min')) {
                  self.alarmMin = firstAggStore['min'];
                }
              }

              break;
            }
          }
        }

        break;
      }

    }

    // get battery value
    if(self.batteryVisible == true) {
      //console.log('RawValueComponent.updateData - self.batterySensorId: ', self.batterySensorId);

      for(let b = 0; b < startData.length; b++) {
        let startDataBatteryItem = startData[b];
        
        if(startDataBatteryItem['sensorId'] == self.batterySensorId) {

          //console.log('RawValueComponent.readStartData - startDataBatteryItem: ', startDataBatteryItem);
          //console.log('RawValueComponent.readStartData - self.batteryLevel: ', self.batteryLevel);

          self.batteryAlarmLevelMin;
          self.batteryAlarmLevelLow; 
          for(let i = 0; i < startDataBatteryItem['calculationAndStores'].length; i++) {
            let calcAndStore = startDataBatteryItem['calculationAndStores'][i];
            if(calcAndStore['calculation'] == 0) {
              let aggAndStore = calcAndStore['aggregationAndStores'];

              for(let x = 0; x < aggAndStore.length; x++) {
                if(aggAndStore[x]['aggregationType'] == 0) {
                  self.batteryAlarmLevelMin = aggAndStore[x]['min']
                  self.batteryAlarmLevelLow = aggAndStore[x]['low']
                }
              }
            }
          }

          //console.log('RawValueComponent.readStartData - self.batteryAlarmLevelLow: ', self.batteryAlarmLevelLow);
          //console.log('RawValueComponent.readStartData - self.batteryAlarmLevelMin: ', self.batteryAlarmLevelMin);

          if(self.batteryLevel != null) {
            
            if(self.batteryLevel == 0) {
              self.batteryLevelClass = 'battery-status-empty';
            }
            else if(self.batteryLevel <= self.batteryAlarmLevelMin) {
              self.batteryLevelClass = 'battery-status-empty';
            }
            else if(self.batteryLevel <= self.batteryAlarmLevelMin) {
              self.batteryLevelClass = 'battery-status-quarter';
            }
            else if(self.batteryLevel > self.batteryAlarmLevelMin && self.batteryLevel <= self.batteryAlarmLevelLow) {
              self.batteryLevelClass = 'battery-status-half';
            }
            else if(self.batteryLevel > self.batteryAlarmLevelLow) {
              self.batteryLevelClass = 'battery-status-threequarters';
            }
            else if(self.batteryLevel > 90) {
              self.batteryLevelClass = 'battery-status-full';
            }
          }
        }
      }
    }

    // get signal strength value
    if(self.signalVisible == true) {
      //console.log('RawValueComponent.updateData - self.signalSensorId: ', self.signalSensorId);

      for(let b = 0; b < startData.length; b++) {
        let startDataSignalItem = startData[b];
        
        if(startDataSignalItem['sensorId'] == self.signalSensorId) {

          //console.log('RawValueComponent.readStartData - startDataSignalItem: ', startDataSignalItem);

          self.signalAlarmLevelLow;
          self.signalAlarmLevelMin;

          for(let i = 0; i < startDataSignalItem['calculationAndStores'].length; i++) {
            let calcAndStore = startDataSignalItem['calculationAndStores'][i];
            if(calcAndStore['calculation'] == 0) {
              let aggAndStore = calcAndStore['aggregationAndStores'];

              for(let x = 0; x < aggAndStore.length; x++) {
                if(aggAndStore[x]['aggregationType'] == 0) {
                  self.signalAlarmLevelMin = aggAndStore[x]['min']
                  self.signalAlarmLevelLow = aggAndStore[x]['low']
                }
              }
            }
          }

          if(self.signalLevel != null) {
            if(self.signalLevel > self.signalAlarmLevelMin && self.signalLevel <= self.signalAlarmLevelLow) {
            self.signalLevelClass = 'signal-status-low'
            }
            else if(self.signalLevel <= self.signalAlarmLevelMin) {
              self.signalLevelClass = 'signal-status-min'
            }
            else {
              self.signalLevelClass = 'signal-status-normal'
            }
          }
        }
      }
    }

    self.startDataReadFlag = true;
    clearInterval(self.startDataCheckCycle);
  }

	fetchCardData(sensorId) {
		let self = this;

		//console.log('RawValueComponent.fetchCardData - cards pile: ', self.state['dashboard']['cards']);

		for(let i = 0; i < self.state['dashboard']['cards'].length; i++) {
			let cardCandidate = self.state['dashboard']['cards'][i];
			if(cardCandidate.tempId == self.tempId) {
				self.cardData = cardCandidate;
				//console.log('RawValueComponent.fetchCardData - card HIT: ', self.cardData);
				self.cardDataLoaded = true;
				self.loadCard();
				break;
			}
		}
	}

	loadCard() {
		let self = this;

		//console.log('RawValueComponent.loadCard - card: ', self.cardData);

		if(self.cardData.hasOwnProperty('title')) {
			if(self.cardData['title'] != null) {
				self.titleValue = self.cardData['title'];
			}
		}

		if(self.cardData.hasOwnProperty('unit')) {
			if(self.cardData['unit'] != null) {
				self.unitValue = self.cardData['unit'];
			}
		}

		/*if(self.cardData.hasOwnProperty('batterySensorId')) {
			if(self.cardData['batterySensorId'] != null) {
				self.batteryVisible = true;
				self.batterySensorId = self.cardData['batterySensorId'];
			}
		}*/

    if(self.cardData.hasOwnProperty('signalSensorId')) {
      if(self.cardData['signalSensorId'] != null) {
        self.signalVisible = true;
        self.signalSensorId = self.cardData['signalSensorId'];
      }
    }
	}

	updateData() {
		let self = this;

    let data = self.state['sensorData']

    //console.log('RawValueComponent.updateData - (' + self.sensorId + ') card data: ', data);

    // set sensor value
    if(data['sensorId'] == self.sensorId && data['calcType'] == self.calculationType && data['aggType'] == self.aggregationType) {
      //console.log('RawValueComponent.updateData - (' + self.sensorId + ') card data: ', data);

      self.sensorValue = self.utilityService.getNumberWithSetDecimals(data['sensorValue'], self.maxNumberOfDecimals);
      self.lastRunRawValue = data['sensorTimeStamp']; // convert
      self.lastRunStringValue = self.utilityService.dateConversionDayTime(self.lastRunRawValue);

      // set alarms
      if(self.alarmStatusOn == true) {
        //console.log('RawValueComponent.updateData - (' + self.sensorId + ') card data: ', data);
        //console.log('RawValueComponent.updateData, max: ' + self.alarmMax);

        // alarmMax, alarmHigh, alarmLow, alarmMin
        if(self.alarmMax != null && Number(self.sensorValue) >= self.alarmMax) {
          self.alarmStatus = 'alarmMax'; 
        }
        else if(self.alarmHigh != null && (Number(self.sensorValue) >= self.alarmHigh && Number(self.sensorValue) < self.alarmMax)) {
          self.alarmStatus = 'alarmHigh'; 
        }
        else if(self.alarmLow != null && (Number(self.sensorValue) <= self.alarmLow && Number(self.sensorValue) > self.alarmMin)) {
          self.alarmStatus = 'alarmLow'; 
        }
        else if(self.alarmMin != null && Number(self.sensorValue) <= self.alarmMin) {
          self.alarmStatus = 'alarmMin'; 
        }
        else {
          self.alarmStatus = ''; 
        }

        document.querySelectorAll('[data-tempid="' + self.tempId + '"]')[0].setAttribute('data-alarmstatus', self.alarmStatus);
      }
    }

    // set battery status
		if(self.batteryVisible == true) {
			//console.log('RawValueComponent.updateData - self.batterySensorId: ', self.batterySensorId);

      if(data['sensorId'] == self.batterySensorId) {
        self.batteryLevelValue = Math.floor(Number(data['sensorValue'])) + '%';
        self.batteryLevel = Number(data['sensorValue']);

        //console.log('RawValueComponent.updateData - self.batteryLevel: ', self.batteryLevel);
        //console.log('RawValueComponent.updateData - batteryAlarmLevelMin: ', self.batteryAlarmLevelMin);
        //console.log('RawValueComponent.updateData - batteryAlarmLevelLow: ', self.batteryAlarmLevelLow);

        if(self.batteryLevel == 0) {
          self.batteryLevelClass = 'battery-status-empty';
        }
        else if(self.batteryLevel <= self.batteryAlarmLevelMin) {
          self.batteryLevelClass = 'battery-status-empty';
        }
        else if(self.batteryLevel <= self.batteryAlarmLevelMin) {
          self.batteryLevelClass = 'battery-status-quarter';
        }
        else if(self.batteryLevel > self.batteryAlarmLevelMin && self.batteryLevel <= self.batteryAlarmLevelLow) {
          self.batteryLevelClass = 'battery-status-half';
        }
        else if(self.batteryLevel > self.batteryAlarmLevelLow) {
          self.batteryLevelClass = 'battery-status-threequarters';
        }
        else if(self.batteryLevel > 90) {
          self.batteryLevelClass = 'battery-status-full';
        }
      }
  	}
    
    if(self.signalVisible == true) {

      if(data['sensorId'] == self.signalSensorId) {

        self.signalLevel = Number(data['sensorValue']);
        self.signalLevelValue = Math.floor(self.signalLevel) + ' dBm';

        if(self.signalLevel > self.signalAlarmLevelMin && self.signalLevel <= self.signalAlarmLevelLow) {
          self.signalLevelClass = 'signal-status-low'
        }
        else if(self.signalLevel <= self.signalAlarmLevelMin) {
          self.signalLevelClass = 'signal-status-min'
        }
        else {
          self.signalLevelClass = 'signal-status-normal'
        }
         
      }
    }
	}
}