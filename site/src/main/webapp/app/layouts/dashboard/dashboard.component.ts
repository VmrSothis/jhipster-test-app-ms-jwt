import { Component, OnInit } from '@angular/core';
import { DxChartModule, DxSelectBoxModule } from 'devextreme-angular';
import { DataService } from '../../core/services/DataServices';
import SharedModule from 'app/shared/shared.module';

interface PopulationData {
  arg: number;
  val: number;
}

interface СorporationInfo {
  company: string;
  y2005: number;
  y2004: number;
}

@Component({
  selector: 'dashboard',
  standalone: true,
  imports: [DxChartModule, DxSelectBoxModule, SharedModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  providers: [DataService] // Agrega el servicio aquí
})
export class DashboardComponent implements OnInit {
  populationData: PopulationData[] = [];
  corporationsInfo: СorporationInfo[] = [];

  types: string[] = ['splinearea', 'stackedsplinearea', 'fullstackedsplinearea'];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getPopulationData().subscribe((data: PopulationData[]) => {
      this.populationData = data;
      console.log(this.populationData);
    });

    this.dataService.getCorporationInfo().subscribe((data: СorporationInfo[]) => {
      this.corporationsInfo = data;
      console.log(this.corporationsInfo);
    });
  }
}
