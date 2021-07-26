package ru.mihkopylov.myspb.controller.exceptionHandler;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mihkopylov.myspb.controller.dto.ErrorResponse;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return createValidationResponse(ex, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return createValidationResponse(ex.getBindingResult(), status);
    }

    @NonNull
    private String getErrorsValidationMessage(@NonNull Errors errors) {
        Stream<String> fieldStream = errors.getFieldErrors().stream().map(this::getErrorMessage);
        Stream<String> globalStream = errors.getGlobalErrors().stream().map(this::getErrorMessage);
        return Stream.concat(fieldStream, globalStream).collect(Collectors.joining("\n"));
    }

    @NonNull
    private String getErrorMessage(@NonNull ObjectError objectError) {
        return Optional.ofNullable(objectError.getDefaultMessage()).orElse("Ошибка валидации");
    }

    @NonNull
    private ResponseEntity<Object> createValidationResponse(@NonNull Errors errors, @NonNull HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(getErrorsValidationMessage(errors)), status);
    }

}
