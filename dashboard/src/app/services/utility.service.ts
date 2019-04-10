import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  	constructor() { }

  	generateUUID() {
  		let d = new Date().getTime();
	    if (typeof performance !== 'undefined' && typeof performance.now === 'function'){
	        d += performance.now(); //use high-precision timer if available
	    }
	    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
	        let r = (d + Math.random() * 16) % 16 | 0;
	        d = Math.floor(d / 16);
	        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	    });
	}

	timeConversion(millisec: number) {

    var seconds = (millisec / 1000).toFixed(0);

    var minutes = (millisec / (1000 * 60)).toFixed(0);

    var hours = (millisec / (1000 * 60 * 60)).toFixed(0);

    var days = (millisec / (1000 * 60 * 60 * 24)).toFixed(0);

    if (Number(seconds) <= 60) {
        return seconds + " second" + ((Number(seconds) > 1) ? 's' : '');
    } else if (Number(minutes) <= 60) {
        return minutes + " minute" + ((Number(minutes) > 1) ? 's' : '');
    } else if (Number(hours) <= 24) {
        return hours + " hour" + ((Number(hours) > 1) ? 's' : '');
    } else {
        return days + " days"
    }
  }

  dateConversionTimeDay(epochTime) {
    var self = this;
    var date = new Date(epochTime);
    var timeString = self.zeroFill(date.getHours() + '') + ':' + self.zeroFill(date.getMinutes() + '') + ':' + self.zeroFill(date.getSeconds() + '');

    var dateString = self.zeroFill(date.getDate() + '') + '/' + self.zeroFill((date.getMonth() + 1) + '') + '/' + date.getFullYear()
    return timeString + ', ' + dateString;
  }
    
  dateConversionDayTime(epochTime) {
    var self = this;
    var date = new Date(epochTime);
    var timeString = self.zeroFill(date.getHours() + '') + ':' + self.zeroFill(date.getMinutes() + '') + ':' + self.zeroFill(date.getSeconds() + '');

    var dateString = self.zeroFill(date.getDate() + '') + '/' + self.zeroFill((date.getMonth() + 1) + '') + '/' + date.getFullYear()
    return dateString + ', ' + timeString;
  }
  
  zeroFill(number) {
    if (number.length == 1) {
        number = '0' + number;
    }
    return number;
  }

  getNumberWithSetDecimals(numberToProcess, maxNumberOfDecimals: number) {
    let self = this;

    //console.log('UtilityService.getNumberWithSetDecimals - numberToProcess: ' + numberToProcess);
    //console.log('UtilityService.getNumberWithSetDecimals - maxNumberOfDecimals: ' + maxNumberOfDecimals);

    let numberResult = null;

    if(numberToProcess != null && isNaN(numberToProcess) == false) {
      let numberString = numberToProcess.toString();
      //console.log('UtilityService.getNumberWithSetDecimals - numberString: ' + numberString);

      let indexOfDecimalDot:number = numberString.indexOf('.');
      //console.log('UtilityService.getNumberWithSetDecimals - indexOfDecimalDot: ' + indexOfDecimalDot);

      if(indexOfDecimalDot > -1) {
        let preNumber = numberString.substring(0, indexOfDecimalDot);
        let postNumber = numberString.substring(indexOfDecimalDot + 1, indexOfDecimalDot + maxNumberOfDecimals + 1);

        //console.log('UtilityService.getNumberWithSetDecimals - preNumber: ' + preNumber);
        //console.log('UtilityService.getNumberWithSetDecimals - postNumber: ' + postNumber);

        numberString = preNumber + '.' + postNumber;
        //console.log('UtilityService.getNumberWithSetDecimals - numberString: ' + numberString);
      }

      numberResult = Number(numberString);
    }

    //console.log('UtilityService.getNumberWithSetDecimals - numberResult: ', numberResult);

    return numberResult;
  }
}