package ru.dverkask.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.dverkask.api.OpticDeviceService;
import ru.dverkask.api.OpticDevice;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeviceController {
    @GetMapping("/devices")
    public List<OpticDevice> getDevices() {
        OpticDeviceService.readFromYAML();
        List<OpticDevice> devices = OpticDeviceService.getDevices();

        if (devices == null || devices.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No devices found"
            );
        }

        return devices;
    }

    @GetMapping("/devices/{uuid}")
    public OpticDevice getDevice(@PathVariable UUID uuid) {
        OpticDeviceService.readFromYAML();
        return OpticDeviceService.getDeviceByUUID(uuid);
    }

    @PostMapping("/devices")
    public OpticDevice createDevice(@RequestBody OpticDevice opticDevice) {
        OpticDeviceService.readFromYAML();
        OpticDeviceService.getDevices().add(opticDevice);
        OpticDeviceService.writeToYAML();

        return opticDevice;
    }

    @PatchMapping("/devices/{uuid}/zoomIn")
    public void zoomIn(@PathVariable UUID uuid,
                       @RequestBody Map<String, Integer> zoom) {
        OpticDevice device = OpticDeviceService.getDeviceByUUID(uuid);
        Integer increase = zoom.get("zoom");
        if (increase != null)
            device.zoomIn(increase);

        OpticDeviceService.writeToYAML();
    }

    @PatchMapping("/devices/{uuid}/zoomOut")
    public void zoomOut(@PathVariable UUID uuid,
                        @RequestBody Map<String, Integer> zoom) {
        OpticDevice device = OpticDeviceService.getDeviceByUUID(uuid);
        Integer decrease = zoom.get("zoom");
        if (decrease != null)
            device.zoomOut(decrease);

        OpticDeviceService.writeToYAML();
    }
}
