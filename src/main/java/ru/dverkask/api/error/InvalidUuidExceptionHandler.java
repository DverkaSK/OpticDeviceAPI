package ru.dverkask.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class InvalidUuidExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException)ex;

        Map<String, String> errorResponse = new HashMap<>();
        if (isCausedByInvalidUUIDFormat(exception)) {
            errorResponse.put("message", "Invalid UUID format. Please provide a correct UUID.");
        } else {
            errorResponse.put("message", ex.getMessage());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private boolean isCausedByInvalidUUIDFormat(MethodArgumentTypeMismatchException exception) {
        return UUID.class.equals(exception.getRequiredType());
    }
}
