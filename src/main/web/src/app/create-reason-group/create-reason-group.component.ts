import {Component, OnInit} from "@angular/core";
import {ProblemService} from "../services/problem.service";
import {CreateReasonGroupRequest} from "../model/create-reason-group-request";
import {ReasonGroup} from "../model/reason-group";
import {isDefined} from "@angular/compiler/src/util";
import {ActivatedRoute, Router} from "@angular/router";
import {finalize} from "rxjs/operators";

@Component({
    selector: "app-create-reason-group",
    templateUrl: "./create-reason-group.component.html",
    styleUrls: ["./create-reason-group.component.css"],
})
export class CreateReasonGroupComponent implements OnInit {
    reasonsMapKeys: string[] = [];
    request = new CreateReasonGroupRequest();
    reasonGroups: ReasonGroup[] = [];
    creating: boolean = false;
    reasonsMap = new Map<string, InnerReason[]>();
    private id: number;

    constructor(
        private problemService: ProblemService,
        private router: Router,
        private route: ActivatedRoute,
    ) {
    }

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.id = isDefined(params.id) ? +params.id : undefined;
            if (isDefined(this.id)) {
                this.problemService.getReasonGroup(this.id).subscribe(reasonGroup => {
                    this.request.name = reasonGroup.name;
                    this.request.parentId = reasonGroup.parent ? reasonGroup.parent.id : undefined;
                    this.request.reasonId = reasonGroup.reasonId;
                    this.request.body = reasonGroup.body;
                });
            }
        });
        this.route.queryParams.subscribe(params => {
            this.request.parentId = params.parentId ? +params.parentId : undefined;
        });
        this.problemService.getReasons().subscribe(cityObjects => {
            for (const cityObject of cityObjects) {
                let groupName = cityObject.name;
                let reasonsInGroup: InnerReason[] = [];
                for (const category of cityObject.categories) {
                    for (const reason of category.reasons) {
                        reasonsInGroup.push(new InnerReason(reason.id, groupName, reason.name));
                    }
                }
                reasonsInGroup = reasonsInGroup.sort((a, b) => a.name.localeCompare(b.name));
                this.reasonsMap.set(groupName, reasonsInGroup);
            }
            this.reasonsMapKeys = Array.from(this.reasonsMap.keys()).sort();
        });
        this.problemService.getReasonGroups().subscribe(groups => this.reasonGroups = groups.filter(g => !isDefined(g.reasonId)));
    }

    save() {
        this.creating = true;
        let observable = isDefined(this.id) ?
            this.problemService.updateReasonGroup(this.id, this.request) :
            this.problemService.createReasonGroup(this.request);
        observable
            .pipe(
                finalize(() => this.creating = false))
            .subscribe(() => this.router.navigate(["/"],
                {queryParams: {"parentId": this.id ? this.id : this.request.parentId}}));
    }

    getSelectedReason(): InnerReason {
        let result: InnerReason = undefined;
        this.reasonsMap.forEach(reasons => {
            let foundValue = reasons.find(r => r.id === this.request.reasonId);
            if (isDefined(foundValue)) {
                result = foundValue;
            }
        });
        return result;
    }
}

class InnerReason {
    id: number;
    group: string;
    name: string;

    constructor(id: number, group: string, name: string) {
        this.id = id;
        this.group = group;
        this.name = name;
    }

}