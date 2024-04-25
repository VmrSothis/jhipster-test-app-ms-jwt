import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Location } from '@angular/common';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'breadcrumb-bar',
  standalone: true, // Indica que el componente no depende de otros
  imports: [CommonModule, TranslateModule], // Módulos que el componente necesita
  templateUrl: './breadcrumb-bar.component.html',
  styleUrls: ['./breadcrumb-bar.component.scss']
})
export class BreadcrumbBarComponent implements OnInit {
  breadcrumbItems: { label: string, url: string }[] = [];
  isDashboard: boolean = false;

  constructor(private location: Location, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void { // Método del ciclo de vida que se ejecuta al inicializar el componente
    // Suscribe un observador a los eventos de navegación del router para actualizar las migas de pan
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.isDashboard = (this.router.url === "/");
      const urlSegments: string[] = this.router.url.split('/').filter(segment => segment !== ''); // Obtiene segmentos de la URL y elimina los vacíos
      this.breadcrumbItems = this.generateBreadcrumbs(urlSegments); // Genera las migas de pan basadas en los segmentos de la URL
    });

    // Genera las migas de pan por primera vez al inicializar el componente
    const urlSegments: string[] = this.router.url.split('/').filter(segment => segment !== '');
    this.breadcrumbItems = this.generateBreadcrumbs(urlSegments); // Genera las migas de pan
  }



  goBack(): void {
    this.location.back();
  }

  navigate(url: string):void {
    this.router.navigate([url]);
  }

  // Genera las migas de pan basadas en los segmentos de la URL
  private generateBreadcrumbs(urlSegments: string[]): { label: string, url: string }[] {
    const breadcrumbs: { label: string, url: string }[] = [];
  
    breadcrumbs.push({ label: 'breadcrumb.home', url: '' }); // Agrega "Inicio" como primera miga de pan
  
    let currentUrl: string = ''; // Declarar la variable currentUrl fuera del bucle
  
    for (let i = 0; i < urlSegments.length; i++) {
      console.error(currentUrl);
      // if (currentUrl === "/admin") { return breadcrumbs; }
      const currentUrlSegment: string = urlSegments[i].replace(/[0-9]/g, '').split('?')[0];
      currentUrl += currentUrlSegment ? `/${currentUrlSegment}` : ''; // Concatenar el segmento actual a la URL acumulativa
      
      if (!currentUrl) { return breadcrumbs; }
      const matchingBreadcrumb: { label: string; url: string; } | undefined = breadcrumbs.find(item => item.url.includes(currentUrlSegment)); // Busca si la URL acumulativa ya existe en las migas de pan
      const isAdminPathSegment: boolean = (currentUrl === "/admin");
      if (!matchingBreadcrumb && !isAdminPathSegment) {   // Si la sección actual no se encuentra ya dentro de los breadcrumbs
        breadcrumbs.push({ label: 'breadcrumb.' + currentUrlSegment, url: currentUrl }); // Agrega el segmento de la URL como nueva miga de pan
      }
      
    }
    return breadcrumbs;
  }

}
