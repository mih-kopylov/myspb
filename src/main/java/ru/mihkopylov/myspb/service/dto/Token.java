package ru.mihkopylov.myspb.service.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Token {
    private static final String SEPARATOR = ":";
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;

    @NonNull
    public static Token fromResponse(@NonNull TokenResponse tokenResponse) {
        return new Token(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    @NonNull
    public static Optional<Token> fromString(@NonNull String value) {
        if (isBlank(value)) {
            log.debug("can't parse tokens from header '{}'", value);
            return Optional.empty();
        }
        String[] parts = value.split(Token.SEPARATOR);
        if (parts.length != 2) {
            log.debug("can't parse tokens from header '{}'", value);
            return Optional.empty();
        }
        return Optional.of(new Token(parts[0], parts[1]));
    }

    @Override
    public String toString() {
        return accessToken + SEPARATOR + refreshToken;
    }
}
