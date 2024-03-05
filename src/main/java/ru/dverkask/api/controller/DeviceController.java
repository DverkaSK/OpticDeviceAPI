package ru.dverkask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "DeviceController", description = "Контроллер реализации REST API")
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
    @Operation(
            summary = "Возвращает список всех объектов",
            description = "Возвращает список объектов OpticDevice, если API ключ имеет право ADMIN или READ"
    )
    public ResponseEntity<GenericResponse<?>> getDevices(@RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        return PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.READ, UserApiKey.Permission.ADMIN)
                ? ResponseEntity.ok().body(new GenericResponse<>(200, "Success", service.findAll()))
                : handleUnauthorized();
    }

    @GetMapping("/devices/{uuid}")
    @Operation(
            summary = "Возвращает конкретный объект по UUID",
            description = "Возвращает объект OpticDevice по UUID," +
                    " если пользователь указал API ключ с правами READ или ADMIN"
    )
    public ResponseEntity<GenericResponse<?>> getDevice(@PathVariable UUID uuid,
                                                        @RequestHeader("Authorization") UUID apiKey) {
        UserApiKey userApiKey = authService.findByKey(apiKey);
        return PermissionChecker.checkPermission(userApiKey, UserApiKey.Permission.READ, UserApiKey.Permission.ADMIN)
                ? ResponseEntity.ok().body(new GenericResponse<>(200, "Success", service.find(uuid)))
                : handleUnauthorized();
    }

    @PostMapping("/devices")
    @Operation(
            summary = "Создаёт новый объект",
            description = "Создаёт новый объект, для этого требуется в тело" +
                    " запроса задать параметр opticPower. Для запроса необходим API ключ с " +
                    "правами WRITE или ADMIN"
    )
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
    @Operation(
            summary = "Удаляет объект",
            description = "Удаляет объект по UUID, нужен API ключ " +
                    "с правами WRITE или ADMIN"
    )
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
    @Operation(
            summary = "Реализует метод приближения",
            description = "Требуется API ключ с правами " +
                    "WRITE или ADMIN, приближает объект с помощью " +
                    "умножения opticPower на zoom"
    )
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
    @Operation(
            summary = "Реализует метод отдаления",
            description = "Требуется API ключ с правами " +
                    "WRITE или ADMIN, приближает объект с помощью " +
                    "деления opticPower на zoom"
    )
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