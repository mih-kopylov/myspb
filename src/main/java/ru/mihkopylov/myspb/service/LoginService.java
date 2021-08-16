package ru.mihkopylov.myspb.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.mihkopylov.myspb.Api;
import ru.mihkopylov.myspb.config.ClientConfigurationProperties;
import ru.mihkopylov.myspb.exception.UserNotFoundException;
import ru.mihkopylov.myspb.interceptor.SessionContext;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.dto.Token;
import ru.mihkopylov.myspb.service.dto.TokenResponse;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginService {
    @NonNull
    private final HttpService httpService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final SessionContext sessionContext;
    @NonNull
    private final ClientConfigurationProperties clientConfigurationProperties;

    public void login(@NonNull String login, @NonNull String password) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("username", login);
        body.add("password", password);
        body.add("grant_type", "password");
        body.add("client_id", clientConfigurationProperties.getId());
        body.add("client_secret", clientConfigurationProperties.getSecret());

        TokenResponse response = httpService.post(Api.TOKEN, body, TokenResponse.class);
        User user = userService.createUserIfNotExists(login);
        sessionContext.setToken(Token.fromResponse(response));
        sessionContext.setUser(user);
    }

    @NonNull
    public TokenResponse refreshToken() {
        User user = sessionContext.getUser().orElseThrow(UserNotFoundException::new);
        Optional<Token> token = sessionContext.getToken();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        token.ifPresent(value -> body.add("refresh_token", value.getRefreshToken()));
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientConfigurationProperties.getId());
        body.add("client_secret", clientConfigurationProperties.getSecret());

        TokenResponse response = httpService.post(Api.TOKEN, body, TokenResponse.class);
        userService.createUserIfNotExists(user.getLogin());
        return response;
    }
}
