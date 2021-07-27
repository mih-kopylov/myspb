package ru.mihkopylov.myspb.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportReqsonGroupsRequest {
    /**
     * Login of the user whose reason groups will be imported to current user
     */
    @NotBlank(message = "Логин обязателен")
    private String login;
}
