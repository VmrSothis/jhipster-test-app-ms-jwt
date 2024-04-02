import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';


@Component({
  selector: 'breadcrumb-bar',
  standalone: true,
  imports: [SharedModule,],
  templateUrl: './breadcrumb-bar.component.html',
  styleUrl: './breadcrumb-bar.component.scss'
})
export class BreadcrumbBarComponent {

}
