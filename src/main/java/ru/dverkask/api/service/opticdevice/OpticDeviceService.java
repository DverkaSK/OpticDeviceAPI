package ru.dverkask.api.service.opticdevice;

import lombok.NonNull;
import ru.dverkask.api.OpticDevice;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface OpticDeviceService {
    void save(@NonNull OpticDevice device);
    OpticDevice find(UUID uuid);
    List<OpticDevice> findAll();
    double calculatePercentile(List<OpticDevice> devices, double percentileValue);
    void delete(UUID uuid);
    void zoomIn(UUID uuid, Integer increase);
    void zoomOut(UUID uuid, Integer decrease);
}
