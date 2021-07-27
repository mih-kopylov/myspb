package ru.mihkopylov.myspb.controller.exceptionHandler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mihkopylov.myspb.controller.dto.ErrorResponse;
import ru.mihkopylov.myspb.exception.UserNotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserNotFoundExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle() {
        return new ResponseEntity<>(new ErrorResponse("Unauthorized"), HttpStatus.UNAUTHORIZED);
    }
}
