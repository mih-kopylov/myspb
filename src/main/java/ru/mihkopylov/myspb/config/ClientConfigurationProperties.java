package ru.mihkopylov.myspb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@ConfigurationProperties("app.client")
@Validated
public class ClientConfigurationProperties {
    @NotNull
    private String id;
    @NotNull
    private String secret;
}
