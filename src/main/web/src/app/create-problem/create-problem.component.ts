import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {ProblemService} from "../services/problem.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CreateProblemRequest} from "../model/create-problem-request";
import {GeoService} from "../services/geo.service";
import {MatSnackBar} from "@angular/material";
import {finalize} from "rxjs/operators";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
    selector: "app-create-problem",
    templateUrl: "./create-problem.component.html",
    styleUrls: ["./create-problem.component.css"],
})
export class CreateProblemComponent implements OnInit {
    creating = false;
    form: FormGroup = this.formBuilder.group({
        body: [""],
    })
    model = new CreateProblemRequest();
    files: SelectedFile[] = [];
    @ViewChild("fileInput")
    fileInput: ElementRef;

    constructor(
        private problemService: ProblemService,
        private router: Router,
        private route: ActivatedRoute,
        private geoService: GeoService,
        private snackBar: MatSnackBar,
        private formBuilder: FormBuilder,
    ) {
    }

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            this.model.reasonGroupId = +params.parentId;
            this.problemService.getReasonGroup(this.model.reasonGroupId).subscribe(reasonGroup => {
                this.form.controls.body.setValue(reasonGroup.body);
            });
        });
    }

    onFileSelect(event) {
        let files = event.target.files;
        if (files.length > 0) {
            for (let i = 0; i < files.length; i++) {
                let file = files[i];
                let selectedFile = new SelectedFile();
                selectedFile.file = file;
                this.files.push(selectedFile);

                let reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    return selectedFile.preview = reader.result;
                };
            }
        }
        this.fileInput.nativeElement.value = "";
    }

    deleteFile(file) {
        this.files.splice(this.files.indexOf(file), 1);
    }

    doSend() {
        let coords = this.geoService.getCoords();
        this.model.latitude = coords.latitude;
        this.model.longitude = coords.longitude;
        this.model.body = this.form.controls.body.value;
        this.model.files = [];
        for (const file of this.files) {
            this.model.files.push(file.file);
        }
        this.creating = true;
        this.problemService.createProblem(this.model).pipe(
            finalize(() => this.creating = false),
        ).subscribe(problem => {
                if (this.creating) {
                    this.doReset();
                }
                this.snackBar.open("Обращение " + problem.id + " создано", "Открыть")
                    .afterDismissed().subscribe((dismissReason) => {
                    if (dismissReason.dismissedByAction) {
                        window.open("https://gorod.gov.spb.ru/problems/" + problem.id, "_blank");
                    }
                });
            },
        );
    }

    doReset() {
        this.files = [];
        this.creating = false;
    }

}

class SelectedFile {
    file: File;
    preview: string | ArrayBuffer;
}