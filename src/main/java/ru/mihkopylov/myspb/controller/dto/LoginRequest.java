package ru.mihkopylov.myspb.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Логин обязателен")
    private String login;
    @NotBlank(message = "Пароль обязателен")
    private String password;
}
