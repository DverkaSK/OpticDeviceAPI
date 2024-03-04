package ru.dverkask.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dverkask.api.OpticDevice;
import ru.dverkask.api.service.opticdevice.OpticDeviceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeviceController {
    private final OpticDeviceService service;

    @Autowired
    public DeviceController(OpticDeviceService service) {
        this.service = service;
    }

    @GetMapping("/devices")
    public List<OpticDevice> getDevices() {
        return service.findAll();
    }

    @GetMapping("/devices/{uuid}")
    public OpticDevice getDevice(@PathVariable UUID uuid) {
        return service.find(uuid);
    }

    @PostMapping("/devices")
    public ResponseEntity<OpticDevice> createDevice(@RequestBody OpticDevice opticDevice) {
        service.save(opticDevice);
        return new ResponseEntity<>(opticDevice, HttpStatus.CREATED);
    }

    @DeleteMapping("/devices/{uuid}")
    public ResponseEntity<String> deleteDevice(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/devices/{uuid}/zoomIn")
    public ResponseEntity<Map<String, String>> zoomIn(@PathVariable UUID uuid,
                                                      @RequestBody Map<String, Integer> zoom) {
        Integer increase = zoom.get("zoom");
        service.zoomIn(uuid, increase);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Zoom level increased successfully!");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/devices/{uuid}/zoomOut")
    public ResponseEntity<Map<String, String>> zoomOut(@PathVariable UUID uuid,
                                                       @RequestBody Map<String, Integer> zoom) {
        Integer decrease = zoom.get("zoom");
        service.zoomOut(uuid, decrease);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Zoom level decreased successfully!");

        return ResponseEntity.ok(response);
    }
}