package ru.dverkask.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dverkask.api.OpticDevice;
import ru.dverkask.api.response.GenericResponse;
import ru.dverkask.api.service.auth.AuthService;
import ru.dverkask.api.service.auth.PermissionChecker;
import ru.dverkask.api.service.auth.UserApiKey;
import ru.dverkask.api.service.opticdevice.OpticDeviceService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeviceController {
    private final OpticDeviceService service;
    private final AuthService        authService;

    @Autowired
    public DeviceController(OpticDeviceService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    private ResponseEntity<GenericResponse<?>> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new GenericResponse<>(401, "Invalid API key or insufficient permissions", null));
    }

    @GetMapping("/devices")
    public ResponseEntity<GenericResponse<?>> getDevices(@RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        return PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.READ, UserApiKey.Permission.ADMIN)
                ? ResponseEntity.ok().body(new GenericResponse<>(200, "Success", service.findAll()))
                : handleUnauthorized();
    }

    @GetMapping("/devices/{uuid}")
    public ResponseEntity<GenericResponse<?>> getDevice(@PathVariable UUID uuid,
                                                        @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        return PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.READ, UserApiKey.Permission.ADMIN)
                ? ResponseEntity.ok().body(new GenericResponse<>(200, "Success", service.find(uuid)))
                : handleUnauthorized();
    }

    @PostMapping("/devices")
    public ResponseEntity<GenericResponse<?>> createDevice(@RequestBody OpticDevice opticDevice,
                                                           @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        if (PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.WRITE, UserApiKey.Permission.ADMIN)) {
            service.save(opticDevice);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new GenericResponse<>(201, "Device created successfully", opticDevice)
            );
        }
        return handleUnauthorized();
    }

    @DeleteMapping("/devices/{uuid}")
    public ResponseEntity<GenericResponse<?>> deleteDevice(@PathVariable UUID uuid,
                                                           @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        if (PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.WRITE, UserApiKey.Permission.ADMIN)) {
            service.delete(uuid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new GenericResponse<>(204, "Device delete successfully", null)
            );
        }
        return handleUnauthorized();
    }

    @PatchMapping("/devices/{uuid}/zoomIn")
    public ResponseEntity<GenericResponse<?>> zoomIn(@PathVariable UUID uuid,
                                                     @RequestBody Map<String, Integer> zoom,
                                                     @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        if (PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.WRITE, UserApiKey.Permission.ADMIN)) {
            Integer increase = zoom.get("zoom");
            service.zoomIn(uuid, increase);

            return ResponseEntity.ok().body(
                    new GenericResponse<>(200, "Zoom level increased successfully!", null)
            );
        }
        return handleUnauthorized();
    }

    @PatchMapping("/devices/{uuid}/zoomOut")
    public ResponseEntity<GenericResponse<?>> zoomOut(@PathVariable UUID uuid,
                                                      @RequestBody Map<String, Integer> zoom,
                                                      @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        if (PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.WRITE, UserApiKey.Permission.ADMIN)) {
            Integer decrease = zoom.get("zoom");
            service.zoomOut(uuid, decrease);

            return ResponseEntity.ok().body(
                    new GenericResponse<>(200, "Zoom level decreased successfully!", null)
            );
        }
        return handleUnauthorized();
    }
}