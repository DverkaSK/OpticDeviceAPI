package ru.dverkask.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dverkask.api.service.opticdevice.OpticDeviceService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    private final OpticDeviceService opticDeviceService;
    @Autowired
    public InfoController(OpticDeviceService opticDeviceService) {
        this.opticDeviceService = opticDeviceService;
    }
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();

        info.put("name", "OpticDeviceAPI");
        info.put("version", "1.0");
        info.put("author", "DverkaSK");
        info.put("year", "2024");
        info.put("devicesCount", String.valueOf(opticDeviceService.findAll().size()));

        return ResponseEntity.ok(info);
    }
}
