import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs';
import * as constants from './constants';

@Injectable({
  providedIn: 'root'
})
export class StateService {

	private initialState = {
    settings: {},
		dashboard: [],
		sensorData: {},
    sensorStartData: []
	}

	private state = { 
		
	}

	private originalState = { }

	private stateChanged;
	public observableEvents;

  	constructor() { 
  		let self = this;

  		self.state = JSON.parse(JSON.stringify(self.initialState));

  		self.stateChanged = new Subject<any>();
  		self.observableEvents = self.stateChanged.asObservable();
  	}

  	executeAction(action:number, payload?:any) {
  		let self = this;

  		switch(action) {
        // DASHBOARD
        case constants.SETTINGS_SET_DATA :
            self.state = self.settingsSetData(payload);
            break;

        case constants.DASHBOARD_SET_DATA :
          	self.state = self.dashboardSetData(payload);
          	break;

        case constants.SENSOR_DATA_ADD_DATA :
        	self.state = self.sensorDataAddData(payload);
        	break;

        case constants.SENSOR_DATA_ADD_START_DATA :
          self.state = self.sensorDataAddStartData(payload);
          break;

	  		// DEFAULT
	  		default:
	  			break;
  		}

  		self.stateChanged.next(self.getState());
  	}

  	getState() {
  		let self = this;
  		return self.state;
  	}



    ////////////////////////////////////////////////////////////
    // SETTINGS ACTIONS                                       //
    ////////////////////////////////////////////////////////////

    settingsSetData(payload) {
      // payload = { baseUrl: string, restServiceUrl: string, webSocketUrl: string }
      //console.log('StateService.settingsSetData - payload: ', payload);
      let self = this;
      let newState = Object.assign({}, self.state);

      newState['settings']['baseUrl'] = payload.baseUrl;
      newState['settings']['restServiceUrl'] = payload.restServiceUrl;
      newState['settings']['webSocketUrl'] = payload.webSocketUrl;

      return newState;
    }



    ////////////////////////////////////////////////////////////
    // DASHBOARD ACTIONS                                      //
    ////////////////////////////////////////////////////////////

    dashboardSetData(payload) {
      // payload = { startState: object }
      //console.log('StateService.dashboardSetData - payload: ', payload);
      let self = this;
      let newState = Object.assign({}, self.state);

      newState['dashboard'] = payload.dashboard;

      return newState;
    }

    ////////////////////////////////////////////////////////////
    // DASHBOARD ACTIONS                                      //
    ////////////////////////////////////////////////////////////

    sensorDataAddData(payload) {
      	// payload = { sensorDataObject: object }
      	let self = this;
      	let newState = Object.assign({}, self.state);

      	newState['sensorData'] = payload.sensorDataObject;

      	return newState;
    }

    sensorDataAddStartData(payload) {
        // payload = { startDataArray: object }
        //console.log('StateService.sensorDataAddStartData - payload: ', payload);
        let self = this;
        let newState = Object.assign({}, self.state);

        newState['sensorStartData'] = payload.startDataArray;

        return newState;
    }
}
