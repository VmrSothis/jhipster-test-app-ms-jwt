import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import { ApplicationConfigService } from '../config/application-config.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private stateStorageService: StateStorageService,
    private applicationConfigService: ApplicationConfigService,
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const serverApiUrl = this.applicationConfigService.getEndpointFor('');
    if (!request.url || (request.url.startsWith('http') && !(serverApiUrl && request.url.startsWith(serverApiUrl)))) {
      return next.handle(request);
    }

    const token: string | null = this.stateStorageService.getAuthenticationToken();
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    const SubjectToken: string | null = this.stateStorageService.getSubjectKey();
    if (SubjectToken) {
      request = request.clone({
        setHeaders: {
          SubjectToken: SubjectToken,
        },
      });
    }
    return next.handle(request).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          // Access headers here
          const headers = event.headers;
          console.warn('Response Headers:', headers);

          const xSubjectKey = headers.get('x-powered-by');
          if (xSubjectKey) {
            // Do something with the 'x-subject-key' value
            console.log('x-subject-key:', xSubjectKey);
            this.stateStorageService.storeSubjectKey(xSubjectKey);
          }
        }
      })
    );
  }
}
