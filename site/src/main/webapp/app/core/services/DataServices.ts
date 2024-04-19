import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class DataService {
  constructor(private http: HttpClient) { }

  getPopulationData(): Observable<{ arg: number, val: number }[]> {
    return this.http.get<{ arg: number, val: number }[]>('http://localhost:3000/PopulationData');
  }
  getCorporationInfo(): Observable<{ company: string; y2005: number; y2004: number;}[]> {
    return this.http.get<{company: string; y2005: number; y2004: number;}[]>('http://localhost:3000/Corporation');
  }
};
