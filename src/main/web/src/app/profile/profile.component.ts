import {Component, OnInit} from "@angular/core";
import {ProfileService} from "../services/profile.service";
import {Profile} from "../model/profile";
import {isDefined} from "@angular/compiler/src/util";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {MatDialog, MatSnackBar} from "@angular/material";
import {ProblemService} from "../services/problem.service";
import {ImportReasonGroupsDialogComponent} from "./import-reason-groups-dialog/import-reason-groups-dialog.component";
import {ImportReasonGroupsRequest} from "../model/import-reason-groups-request";

@Component({
    selector: "profile",
    templateUrl: "./profile.component.html",
    styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit {
    private profile: Profile;

    constructor(
        private profileService: ProfileService,
        private problemService: ProblemService,
        private authService: AuthService,
        private dialog: MatDialog,
        private snackBar: MatSnackBar,
        private router: Router) {
    }

    ngOnInit() {
        this.updateProfile();
        this.authService.authEvents.subscribe(() => this.updateProfile());
    }


    isLoggedIn() {
        return isDefined(this.profile);
    }

    getProfileName() {
        return this.profile.first_name + " " + this.profile.last_name + " (" + this.profile.email + ")";
    }

    logout() {
        this.profile = undefined;
        this.authService.logout().subscribe(() => {
            this.router.navigate(["/login"]);
        })
    }

    importFromUser() {
        let dialogRef = this.dialog.open(ImportReasonGroupsDialogComponent, {
            data: new ImportReasonGroupsRequest(),

        });
        dialogRef.afterClosed().subscribe(result => {
            this.problemService.importReasonGroups(result.login).subscribe(() => {
                this.snackBar.open("Шаблоны импортированы");
            });
        });
    }

    private updateProfile() {
        this.profileService.getProfile().subscribe(profile => this.profile = profile);
    }
}
