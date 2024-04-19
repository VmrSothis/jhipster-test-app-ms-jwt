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

  constructor(private location: Location, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() { // Método del ciclo de vida que se ejecuta al inicializar el componente
    // Suscribe un observador a los eventos de navegación del router para actualizar las migas de pan
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const urlSegments = this.router.url.split('/').filter(segment => segment !== ''); // Obtiene segmentos de la URL y elimina los vacíos
      this.breadcrumbItems = this.generateBreadcrumbs(urlSegments); // Genera las migas de pan basadas en los segmentos de la URL
    });

    // Genera las migas de pan por primera vez al inicializar el componente
    const urlSegments = this.router.url.split('/').filter(segment => segment !== '');
    this.breadcrumbItems = this.generateBreadcrumbs(urlSegments); // Genera las migas de pan
  }

  private generateBreadcrumbs(urlSegments: string[]): { label: string, url: string }[] {
    const breadcrumbs: { label: string, url: string }[] = [];

    breadcrumbs.push({ label: 'breadcrumb.home', url: '' });

    for (let i = 0; i < urlSegments.length; i++) {
      const currentUrl = `/${urlSegments.slice(0, i + 1).join('/')}`;
      const matchingBreadcrumb = this.breadcrumbItems.find(item => item.url === currentUrl);
      // Mantener la lógica anterior y agregar la condición para omitir "admin"
      if (!matchingBreadcrumb && urlSegments[i] !== 'admin') {
        breadcrumbs.push({ label: 'breadcrumb.' + urlSegments[i], url: currentUrl });
      }
    }
    return breadcrumbs;
  }

  // Retrocede en la historia del navegador
  goBack(): void {
    this.location.back();
  }

  // Método para redireccionar a la URL especificada
  navigateTo(url: string): void {
    this.router.navigateByUrl(url);
  }
}
