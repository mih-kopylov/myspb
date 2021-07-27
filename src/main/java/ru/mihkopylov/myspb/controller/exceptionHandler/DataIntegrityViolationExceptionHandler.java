package ru.mihkopylov.myspb.controller.exceptionHandler;

import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mihkopylov.myspb.controller.dto.ErrorResponse;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class DataIntegrityViolationExceptionHandler {
    private static final Pattern PATTERN = Pattern.compile("Duplicate entry '.+' for key '(\\w+\\.\\w+)'");

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e) {
        String message = Optional.ofNullable(e.getRootCause()).or(() -> Optional.of(e)).map(
                Throwable::getMessage).flatMap(this::extractConstraintName).map(this::getConstraintMessage).orElse(
                "Попытка нарушить интеграционную целостность");
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.CONFLICT);
    }

    @NonNull
    private String getConstraintMessage(@NonNull String constraintName) {
        switch (constraintName) {
            case "reason_groups.ind_user_parent_name":
                return "Группа с таким названием уже существует";
            default:
                return "Неизвестный индекс: " + constraintName;
        }
    }

    @NonNull
    private Optional<String> extractConstraintName(@NonNull String message) {
//        "Duplicate entry '1-4-2' for key 'reason_groups.ind_user_parent_name'";
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }
}
