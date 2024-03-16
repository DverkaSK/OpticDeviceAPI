package ru.dverkask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.dverkask.api.RestApplication;
import ru.dverkask.api.response.GenericResponse;
import ru.dverkask.api.service.auth.AuthService;
import ru.dverkask.api.service.auth.PermissionChecker;
import ru.dverkask.api.service.auth.UserApiKey;

import java.util.UUID;

@Controller
@Tag(name = "StopController", description = "Контроллер для остановки приложения")
public class StopController {
    private final AuthService authService;

    @Autowired
    public StopController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/shutdown")
    @Operation(
            summary = "Останавливает приложение",
            description = "Останавливает приложение, если у пользователя" +
                    "есть права ADMIN"
    )
    public ResponseEntity<GenericResponse<?>> shutdown(@RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        if (PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.ADMIN)) {
            RestApplication.getCtx().close();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new GenericResponse<>(200, "Application is shutdown", null)
            );
        }
        return handleUnauthorized();
    }

    private ResponseEntity<GenericResponse<?>> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new GenericResponse<>(401, "Invalid API key or insufficient permissions", null));
    }
}
