package ru.mihkopylov.myspb.interceptor;

import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.dto.Token;

import java.util.Optional;

@Component
@SessionScope
@Setter
public class SessionContext {
    @Nullable
    private Token token;
    @Nullable
    private User user;

    @NonNull
    public Optional<Token> getToken() {
        return Optional.ofNullable(token);
    }

    @NonNull
    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public void clear() {
        token = null;
        user = null;
    }
}
