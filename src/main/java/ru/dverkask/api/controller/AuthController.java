package ru.dverkask.api.controller;

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
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("generate-key")
    public ResponseEntity<Map<String, UUID>> generateApiKey(@RequestParam(required = true) String username,
                                                            @RequestParam(required = false) UserApiKey.Permission permission) {
        if (permission == null)
            permission = UserApiKey.Permission.READ;
        UserApiKey apiKey = authService.generate(username, permission);

        return ResponseEntity.ok(Map.of("apiKey", apiKey.getUuid()));
    }
}
