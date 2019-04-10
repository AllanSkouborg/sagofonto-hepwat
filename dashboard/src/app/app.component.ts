import { Component, OnInit, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Title} from "@angular/platform-browser";

import { environment } from '../environments/environment';

import { DOCUMENT } from "@angular/platform-browser";

import { StateService } from './services/state.service';
import * as constants from './services/constants';
import { UtilityService } from './services/utility.service';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  state;

  dashboardId;
  title;
  fullscreen: boolean = false;
  cardWidth: number = 320;

  sagofontoLogoUrl: string;
  customerLogoUrl: string;
  artogisLogoUrl: string;

  productionEnvironment: boolean;

  constructor(
    private http: HttpClient, 
    private titleService: Title, 
    @Inject(DOCUMENT) private baseDocument, 
    private stateService: StateService, 
    private utilityService: UtilityService,
    private dataService: DataService
  ) {
  	let self = this;

    //console.log('AppComponent.constructor - decimal test: ', self.utilityService.getNumberWithSetDecimals(62.5259300000, 0));

  	//console.log('AppComponent.constructor - environment is production: ', environment.production);
    self.productionEnvironment = environment.production;
    
    let urlArray = window.location.href.split('?');
    if(urlArray.length > 1) {
      let urlParametersArray = urlArray[1].split('&');
      if(urlParametersArray.length > 0) {
        for( var i = 0; i < urlParametersArray.length; i++ ) {
          let parameter = urlParametersArray[i];
          let parameterName = parameter.split('=')[0];

          if(parameterName == 'id') {
            self.dashboardId = parameter.split('=')[1];
          }

          if(parameterName == 'fullscreen') {
            if(parameter.split('=')[1] == 'true') {
              self.fullscreen = true;
            }
            else if(parameter.split('=')[1] == 'false') {
            self.fullscreen = false;
            }
          }

        }
      }
    }

    if(environment.production == true) {
      //console.log('AppComponent.constructor - production environment, load settings.json file');

      var settingsFileUrlBase = window.location.href.split('?')[0];
      var lastPathValue = settingsFileUrlBase.substring(settingsFileUrlBase.lastIndexOf('/'));

      if (lastPathValue == '/') {
          settingsFileUrlBase = settingsFileUrlBase.substring(0, settingsFileUrlBase.length - 1);
      }
      else if (lastPathValue == '/#') {
          settingsFileUrlBase = settingsFileUrlBase.substring(0, settingsFileUrlBase.length - 2);
      }

      var settingsFileUrl = settingsFileUrlBase.substring(0, settingsFileUrlBase.lastIndexOf('/'));

      //console.log('AppComponent.constructor - production environment, settingsFileUrl: ', settingsFileUrl);

      //console.log('AppComponent.constructor - window.location.origin: ', window.location.origin);

      self.http.get(settingsFileUrl + '/settings.json')
          .subscribe(
            function(settingsData) { 
              //console.log('AppComponent.constructor - settings.json, data: ', settingsData);
              self.stateService.executeAction(constants.SETTINGS_SET_DATA, { baseUrl: settingsData['baseUrl'], restServiceUrl: settingsData['restServiceUrl'], webSocketUrl: settingsData['webSocketUrl'] });

              self.dataService.loadDashboardData(self.dashboardId);

            },
            function(error) {
                console.error('AppComponent.constructor - error: ', error);
            }
          );    
    }
    else {
        //console.log('AppComponent.constructor - development environment, load settings from environment file');

        self.stateService.executeAction(constants.SETTINGS_SET_DATA, { baseUrl: environment.baseUrl, restServiceUrl: environment.restServiceUrl, webSocketUrl: environment.webSocketUrl });

        self.dataService.loadDashboardData(self.dashboardId);
    }

    self.sagofontoLogoUrl = './assets/logos/logo_sagofonto_48_fulllength.png';
    self.customerLogoUrl = './assets/logos/logo_customer_48.png';
    self.artogisLogoUrl = './assets/logos/logo_artogis_48.png';

  	window.addEventListener('resize', function() {
  		self.processResize();
  	});

  	

  	self.stateService.observableEvents.subscribe(function(state) {
      self.state = state;   

      if( self.state['dashboard'].hasOwnProperty('title') ) {
        self.title = self.state['dashboard']['title'];
      }
    });

  }

  ngOnInit() {
  	let self = this;

  }

  processResize() {
      let self = this;

      //console.log('AppComponent.processResize');

      let bodyWidth = document.body.scrollWidth;
      //console.log('AppComponent.processResize - bodyWidth: ', bodyWidth);
      let maxRowCount = Math.floor(bodyWidth / self.cardWidth);

      if(self.state['dashboard']['cards'].length < maxRowCount) {
        maxRowCount = self.state['dashboard']['cards'].length;
      }

      //console.log('AppComponent.processResize - bodyWidth: ', bodyWidth);
      //console.log('AppComponent.processResize - maxRowCount: ', maxRowCount);

      let cardTotalWidth = maxRowCount * self.cardWidth;

      //console.log('AppComponent.processResize - cardTotalWidth: ', cardTotalWidth);

      let leftMargin = Math.floor((bodyWidth - cardTotalWidth) / 2);

      //console.log('AppComponent.processResize - leftMargin: ', leftMargin);

      let cardContainer = document.getElementsByClassName('card-container')[0]['style']['cssText'] = 'margin-left:' + leftMargin + 'px;'

      if(bodyWidth <= 1280) {
        if(self.sagofontoLogoUrl != './assets/logos/logo_sagofonto_48.png') {
          self.sagofontoLogoUrl = './assets/logos/logo_sagofonto_48.png';
        }
      }
      else {
        if(self.sagofontoLogoUrl != './assets/logos/logo_sagofonto_48_fulllength.png') {
          self.sagofontoLogoUrl = './assets/logos/logo_sagofonto_48_fulllength.png';
        }
      }
    }
}
