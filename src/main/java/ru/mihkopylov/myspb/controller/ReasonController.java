package ru.mihkopylov.myspb.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.mihkopylov.myspb.Const;
import ru.mihkopylov.myspb.aspect.RefreshTokenIfRequired;
import ru.mihkopylov.myspb.service.ReasonService;
import ru.mihkopylov.myspb.service.dto.CityObjectResponse;

import java.util.List;

@RestController
@RequestMapping(Const.REST + "/reasons")
@AllArgsConstructor
@Slf4j
public class ReasonController {
    @NonNull
    private final ReasonService reasonService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RefreshTokenIfRequired
    public List<CityObjectResponse> getReasons() {
        log.debug( "get reasons" );
        return reasonService.getReasons();
    }
}
