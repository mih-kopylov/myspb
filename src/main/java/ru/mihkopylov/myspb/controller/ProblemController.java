package ru.mihkopylov.myspb.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mihkopylov.myspb.Const;
import ru.mihkopylov.myspb.aspect.RefreshTokenIfRequired;
import ru.mihkopylov.myspb.controller.dto.CreateProblemRequest;
import ru.mihkopylov.myspb.service.ProblemsService;
import ru.mihkopylov.myspb.service.dto.ProblemResponse;
import ru.mihkopylov.myspb.service.dto.ShortProblemResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Const.REST + "/problems")
@AllArgsConstructor
@Slf4j
public class ProblemController {
    @NonNull
    private final ProblemsService problemsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RefreshTokenIfRequired
    public List<ShortProblemResponse> getProblems() {
        log.debug("get problems");
        return problemsService.getProblems();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @RefreshTokenIfRequired
    public ProblemResponse getProblem(@PathVariable("id") Long id) {
        log.debug("get problem: id={}", id);
        return problemsService.getProblem(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @RefreshTokenIfRequired
    public ProblemResponse createProblem(@ModelAttribute @Valid CreateProblemRequest request) {
        log.debug("create problem");
        return problemsService.createProblem(request.getReasonGroupId(), request.getLatitude(),
                request.getLongitude(), request.getFiles(), request.getBody());
    }
}
