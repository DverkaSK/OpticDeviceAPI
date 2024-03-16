package ru.dverkask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dverkask.api.service.opticdevice.OpticDeviceService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "InfoController", description = "Контроллер для вывода информации о приложении")
public class InfoController {
    private final OpticDeviceService opticDeviceService;
    @Autowired
    public InfoController(OpticDeviceService opticDeviceService) {
        this.opticDeviceService = opticDeviceService;
    }
    @GetMapping("/info")
    @Operation(
            summary = "Вывод информации о приложении",
            description = "Выводит полную информацию о приложении, включая" +
                    "название, версию, автора, год, количество устройств"
    )
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new LinkedHashMap<>();

        info.put("name", "OpticDeviceAPI");
        info.put("version", "1.0");
        info.put("javaVersion", "17");
        info.put("author", "Igor Dorokhin");
        info.put("year", "2024");
        info.put("devicesCount", String.valueOf(opticDeviceService.findAll().size()));

        return ResponseEntity.ok(info);
    }
}
