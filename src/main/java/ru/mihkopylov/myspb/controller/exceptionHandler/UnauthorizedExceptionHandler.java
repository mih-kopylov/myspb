package ru.mihkopylov.myspb.controller.exceptionHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.mihkopylov.myspb.controller.dto.ErrorResponse;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class UnauthorizedExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(@NonNull HttpClientErrorException.Unauthorized exception) {
        log.info("failed to authorize in base service", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Unauthorized"));
    }
}
