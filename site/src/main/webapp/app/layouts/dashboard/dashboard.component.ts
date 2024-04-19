import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { DxChartModule, DxSelectBoxModule } from 'devextreme-angular';
import { VisualRange } from 'devextreme/common/charts';

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
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

  populationData = [{
    arg: 1960,
    val: 3032019978,
  }, {
    arg: 1965,
    val: 3683676306,
  }, {
    arg: 1970,
    val: 4434021975,
  }, {
    arg: 1975,
    val: 5281340078,
  }, {
    arg: 1980,
    val: 6115108363,
  }, {
    arg: 1985,
    val: 6922947261,
  }, {
    arg: 1990,
    val: 7795000000,
  }, {
    arg: 1995,
    val: 7795000000,
  }
    , {
    arg: 2000,
    val: 7795000000,
  }
    , {
    arg: 2005,
    val: 7795000000,
  }
    , {
    arg: 2010,
    val: 7795000000,
  }
  ];

  corporationsInfo: СorporationInfo[] = [{
    company: 'ExxonMobil',
    y2005: 377.28,
    y2004: 270.25,
  }, {
    company: 'GeneralElectric',
    y2005: 353.49,
    y2004: 311.43,
  }, {
    company: 'Microsoft',
    y2005: 269.86,
    y2004: 273.32,
  }, {
    company: 'Citigroup',
    y2005: 252.95,
    y2004: 280.50,
  }, {
    company: 'Royal Dutch Shell plc',
    y2005: 197.67,
    y2004: 165.89,
  }, {
    company: 'Procted & Gamble',
    y2005: 184.72,
    y2004: 130.52,
  }];

  types: string[] = ['splinearea', 'stackedsplinearea', 'fullstackedsplinearea'];

  visualRange: VisualRange = {
    startValue: new Date(1960),
    length: {
      weeks: 2,
    },
  };

  constructor(private cd: ChangeDetectorRef) {
  }

  done(): void {
  }
}
