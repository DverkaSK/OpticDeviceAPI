package ru.dverkask.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dverkask.api.OpticDevice;
import ru.dverkask.api.service.OpticDeviceService;

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
        return service.readAll();
    }

    @GetMapping("/devices/{uuid}")
    public OpticDevice getDevice(@PathVariable UUID uuid) {
        return service.read(uuid);
    }

    @PostMapping("/devices")
    public ResponseEntity<OpticDevice> createDevice(@RequestBody OpticDevice opticDevice) {
        service.create(opticDevice);
        return new ResponseEntity<>(opticDevice, HttpStatus.CREATED);
    }

    @DeleteMapping("/devices/{uuid}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/devices/{uuid}/zoomIn")
    public void zoomIn(@PathVariable UUID uuid,
                       @RequestBody Map<String, Integer> zoom) {
        Integer increase = zoom.get("zoom");
        service.zoomIn(uuid, increase);
    }

    @PatchMapping("/devices/{uuid}/zoomOut")
    public void zoomOut(@PathVariable UUID uuid,
                        @RequestBody Map<String, Integer> zoom) {
        Integer decrease = zoom.get("zoom");
        service.zoomOut(uuid, decrease);
    }
}