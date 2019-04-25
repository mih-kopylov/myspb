import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class Api {
    private static BASE_URL: string = "http://localhost:8080/rest";
    public static LOGIN: string = Api.BASE_URL + "/auth/login";
    public static PROFILE: string = Api.BASE_URL + "/profile";
    public static REASON_GROUPS: string = Api.BASE_URL + "/reasonGroups";
}