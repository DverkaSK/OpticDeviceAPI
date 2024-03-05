package ru.dverkask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dverkask.api.RestApplication;

@Controller
@Tag(name = "StopController", description = "Контроллер для остановки приложения")
public class StopController {
    @PostMapping("/shutdown")
    @Operation(
            summary = "Останавливает приложение"
    )
    public ResponseEntity<String> shutdown() {
        RestApplication.getCtx().close();
        return ResponseEntity.ok("Application is shutting down");
    }
}
