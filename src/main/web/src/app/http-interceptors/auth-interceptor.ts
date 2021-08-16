import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,} from "@angular/common/http";
import {Router} from "@angular/router";
import {Observable, of, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private router: Router,
        private authService: AuthService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authRequest = request.clone({
            withCredentials: true,
        });
        return next.handle(authRequest).pipe(
            catchError(err => this.handleAuthError(err)));
    }

    private handleAuthError(err: HttpErrorResponse): Observable<any> {
        if ((err.status === 401) && (!this.router.isActive("/login", true))) {
            this.authService.logout().subscribe(() => {
                this.router.navigate(["/login"]);
            });
            return of(err.message);
        }
        return throwError(err);
    }
}