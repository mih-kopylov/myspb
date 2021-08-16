import {Component} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.css"],
})
export class LoginComponent {
    model = new LoginForm();

    constructor(
        private authService: AuthService,
        private router: Router,
    ) {
    }

    doLogin() {
        this.authService.login(this.model.login, this.model.password).subscribe(() => {
            this.router.navigate(["/"]);
        });
    }
}


class LoginForm {
    login: string;
    password: string;
}