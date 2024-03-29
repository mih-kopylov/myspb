package ru.mihkopylov.myspb.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateProblemRequest {
    @NotNull(message = "Нужно указать причину обращения")
    private Long reasonGroupId;
    @NotNull(message = "Координаты: широта обязательна")
    private Double latitude;
    @NotNull(message = "Координаты: долгота обязательна")
    private Double longitude;
    @NotEmpty(message = "Нужно как минимум одно фото")
    private MultipartFile[] files;
    @NotEmpty(message = "Текст обращения обязателен")
    private String body;
}
