import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Injectable } from '@angular/core';

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  parse(value: string): NgbDateStruct {
    if (value) {
      const dateParts = value.trim().split('/');
      if (dateParts.length === 1 && this.isNumber(+dateParts[0])) {
        return { day: parseInt(dateParts[0]), month: 0, year: 0 };
      } else if (dateParts.length === 2 && this.isNumber(dateParts[0]) && this.isNumber(dateParts[1])) {
        return { day: parseInt(dateParts[0]), month: parseInt(dateParts[1]), year: 0 };
      } else if (dateParts.length === 3 && this.isNumber(dateParts[0]) && this.isNumber(dateParts[1]) && this.isNumber(dateParts[2])) {
        return { day: parseInt(dateParts[0]), month: parseInt(dateParts[1]), year: parseInt(dateParts[2]) };
      }
    }
    return { day: 0, month: 0, year: 0 };
  }

  isNumber(val: any) {
    return val === +val;
  }

  padNumber(num: number, size = 2) {
    let s = "";
    if (num) {
      s = num.toString() + "";
    }
    while (s.length < size) s = "0" + s;
    return s;
  }


  format(date: NgbDateStruct): string {
    return date ?
      `${this.isNumber(date.day) ? this.padNumber(date.day) : ''}/${this.isNumber(date.month) ? this.padNumber(date.month) : ''}/${date.year}` :
      '';
  }
}
