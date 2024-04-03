import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { Location } from '@angular/common';

@Component({
  selector: 'breadcrumb-bar',
  standalone: true,
  imports: [SharedModule,],
  templateUrl: './breadcrumb-bar.component.html',
  styleUrl: './breadcrumb-bar.component.scss'
})
export class BreadcrumbBarComponent {

  constructor(private location: Location) {

  }

  goBack(): void {
    this.location.back();
  }

}
