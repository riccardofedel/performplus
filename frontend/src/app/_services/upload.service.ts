
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private http: HttpClient) { }

  env = environment;

  getTables() {
    return this.http.get<any>(this.env.API_URL + 'upload/tables');
  }

  
  upload(data: any) {
     //console.log("----onSubmit upload:", data);
    return this.http.post<any>(this.env.API_URL + 'upload', data);
  }

}
