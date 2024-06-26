import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private previousUrlKey = 'previousUrl';
  private authenticationKey = 'jhi-authenticationToken';
  private subjectKey = 'subjectToken';
  private localeKey = 'locale';

  storeUrl(url: string): void {
    sessionStorage.setItem(this.previousUrlKey, JSON.stringify(url));
  }

  getUrl(): string | null {
    const previousUrl = sessionStorage.getItem(this.previousUrlKey);
    return previousUrl ? (JSON.parse(previousUrl) as string | null) : previousUrl;
  }

  clearUrl(): void {
    sessionStorage.removeItem(this.previousUrlKey);
  }

  storeAuthenticationToken(authenticationToken: string, rememberMe: boolean): void {
    authenticationToken = JSON.stringify(authenticationToken);
    this.clearAuthenticationToken();
    if (rememberMe) {
      localStorage.setItem(this.authenticationKey, authenticationToken);
    } else {
      sessionStorage.setItem(this.authenticationKey, authenticationToken);
    }
  }

  getAuthenticationToken(): string | null {
    const authenticationToken = localStorage.getItem(this.authenticationKey) ?? sessionStorage.getItem(this.authenticationKey);
    return authenticationToken ? (JSON.parse(authenticationToken) as string | null) : authenticationToken;
  }

  clearAuthenticationToken(): void {
    sessionStorage.removeItem(this.authenticationKey);
    localStorage.removeItem(this.authenticationKey);
  }

  storeLocale(locale: string): void {
    sessionStorage.setItem(this.localeKey, locale);
  }

  getLocale(): string | null {
    return sessionStorage.getItem(this.localeKey);
  }

  storeSubjectToken(subjectToken: string, rememberMe: boolean): void {
    subjectToken = JSON.stringify(subjectToken);
    if (rememberMe) {
      localStorage.setItem(this.subjectKey, subjectToken);
    } else {
      sessionStorage.setItem(this.subjectKey, subjectToken);
    }
  }

  getSubjectKey(): string | null {
    const subjectKey = localStorage.getItem(this.subjectKey) ?? sessionStorage.getItem(this.subjectKey);
    return subjectKey ? (JSON.parse(subjectKey) as string | null) : subjectKey;
  }

  clearLocale(): void {
    sessionStorage.removeItem(this.localeKey);
  }
}
