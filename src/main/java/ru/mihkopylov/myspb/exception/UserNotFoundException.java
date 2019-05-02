package ru.mihkopylov.myspb.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotFoundException extends AbstractFormattedException {
    public UserNotFoundException() {
        super( "Can't find user by access token" );
    }

    public UserNotFoundException( @NonNull String message, @NonNull Object... params ) {
        super( message, params );
    }
}
