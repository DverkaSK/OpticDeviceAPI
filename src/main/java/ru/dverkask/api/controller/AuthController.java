package ru.dverkask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Threshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.dverkask.api.service.auth.AuthService;
import ru.dverkask.api.service.auth.UserApiKey;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "Контроллер для создяния API ключей")
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("generate-key")
    @Operation(
            summary = "Создаёт API ключ",
            description = "Для создания API ключа в параметрах запроса " +
                    "необходимо указать username и опционально указать " +
                    "права (READ, WRITE, ADMIN), по умолчанию права READ"
    )
    public ResponseEntity<Map<String, UUID>> generateApiKey(@RequestParam(required = true) String username,
                                                            @RequestParam(required = false) UserApiKey.Permission permission) {
        if (permission == null)
            permission = UserApiKey.Permission.READ;
        UserApiKey apiKey = authService.generate(username, permission);

        return ResponseEntity.ok(Map.of("apiKey", apiKey.getUuid()));
    }
}
