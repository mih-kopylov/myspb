package ru.mihkopylov.myspb.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mihkopylov.myspb.Const;
import ru.mihkopylov.myspb.controller.dto.LoginRequest;
import ru.mihkopylov.myspb.interceptor.SessionContext;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.LoginService;

import javax.validation.Valid;

@RestController
@RequestMapping(Const.REST + "/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    @NonNull
    private final LoginService loginService;
    @NonNull
    private final SessionContext sessionContext;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody @Valid LoginRequest request) {
        String login = request.getLogin().trim();
        log.debug("login user: login={}", login);
        loginService.login(login, request.getPassword().trim());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
        log.debug("logout user: login={}", sessionContext.getUser().map(User::getLogin).orElse(null));
        sessionContext.clear();
    }
}
