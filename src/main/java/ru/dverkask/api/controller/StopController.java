package ru.dverkask.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dverkask.api.RestApplication;

@Controller
public class StopController {
    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdown() {
        RestApplication.getCtx().close();
        return ResponseEntity.ok("Application is shutting down");
    }
}
