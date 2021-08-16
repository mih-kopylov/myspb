import {EventEmitter, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Api} from "./api";
import {map} from "rxjs/operators";

@Injectable({
    providedIn: "root",
})
export class AuthService {
    authEvents = new EventEmitter();

    constructor(
        private httpClient: HttpClient,
    ) {
    }

    logout(): Observable<any> {
        return this.httpClient.post(Api.LOGOUT, undefined).pipe(map(() => this.authEvents.emit("logout")));
    }

    login(login: string, password: string): Observable<any> {
        const body = {login, password};
        return this.httpClient.post(Api.LOGIN, body).pipe(map(() => this.authEvents.emit("login")));
    }

}
