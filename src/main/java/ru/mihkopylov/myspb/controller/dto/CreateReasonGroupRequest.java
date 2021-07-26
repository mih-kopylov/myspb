package ru.mihkopylov.myspb.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReasonGroupRequest {
    @NotBlank(message = "Название обязательно")
    private String name;
    private Long parentId;
    private Long reasonId;
    private String body;
}
