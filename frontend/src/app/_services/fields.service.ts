import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../_models';

@Injectable({ providedIn: 'root' })
export class FieldsService {
  constructor() { }

  convertObjectTimestampz(s:any) {
    if (!s) {
      return null;
    }
    return s.year + "-" + this.zeroPad(s.month) + "-" + this.zeroPad(s.day);
    //return current_datetime.getFullYear() + "-" + (current_datetime.getMonth() + 1) + "-" + current_datetime.getDate() + " " + current_datetime.getHours() + ":" + current_datetime.getMinutes() + ":" + current_datetime.getSeconds();
  }


  rectifyFormat(s:string | undefined) {
    if (!s) {
      return null;
    }
    let b = s.split(/\D/);
    return new Date(b[0] + '-' + b[1] + '-' + b[2] + 'T00:00:00.000');
  }


  //formato da 2011-11-11T10:11:00.000Z a 2011-11-11T11:11
  //serve per le input
  convertTimestampzToLocal(s:string) {
    if (!s) {
      return s;
    }
    var current_datetime = this.rectifyFormat(s);
    if (!current_datetime) {
      return null;
    }
    var currentHours = current_datetime.getHours();
    var currentHours2 = ("0" + currentHours).slice(-2);
    var currentMinutes = current_datetime.getMinutes();
    var currentMinutes2 = ("0" + currentMinutes).slice(-2);
    var currentMonth = current_datetime.getMonth() + 1;
    var currentMonth2 = ("0" + currentMonth).slice(-2);
    var currentDay = current_datetime.getDate();
    var currentDay2 = ("0" + currentDay).slice(-2);
    return current_datetime.getFullYear() + "-" + currentMonth2 + "-" + currentDay2 + "T" + currentHours2 + ":" + currentMinutes2;
  }




  //formato da 2011-11-11T10:11:00.000Z a 2011-11-11T11:11
  //serve per le input
  convertTimestampzToObject(s:string) {
    if (!s) {
      return s;
    }
    var current_datetime = this.rectifyFormat(s);
    if (!current_datetime) {
      return null;
    }
    var currentMonth = current_datetime.getMonth() + 1;
    var currentDay = current_datetime.getDate();
    return { year: current_datetime.getFullYear(), month: currentMonth, day: currentDay };
  }
convertCurrentaDate() {
   
    var current_datetime = new Date();
    if (!current_datetime) {
      return null;
    }
    var currentMonth = current_datetime.getMonth() + 1;
    var currentDay = current_datetime.getDate();
    return { year: current_datetime.getFullYear(), month: currentMonth, day: currentDay };
  }

  //mandiamo al backend
  pgFormatDate(data_string:string) {
    if (!data_string) {
      return null;
    }
    var parsed = new Date(data_string);
    var offset = parsed.getTimezoneOffset();
    var timezone = offset / 60;
    return parsed.getUTCFullYear() + "-" + this.zeroPad(parsed.getMonth() + 1) + "-" + this.zeroPad(parsed.getDate()) + " " + this.zeroPad(parsed.getHours()) + ":" + this.zeroPad(parsed.getMinutes()) + ":" + this.zeroPad(parsed.getSeconds()) + ".000+0" + Math.abs(timezone);
  }

  zeroPad(num: number, size = 2) {
    let s = "";
    if (num) {
      s = num.toString() + "";
    }
    while (s.length < size) s = "0" + s;
    return s;
  }

  convertDate(date:string | undefined) {
    if (!date) {
      return "";
    }
    return date.slice(8,10)+"/"+date.slice(5,7)+"/"+date.slice(0,4);
  }

}

