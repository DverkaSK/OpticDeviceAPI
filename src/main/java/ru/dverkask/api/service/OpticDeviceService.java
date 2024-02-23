package ru.dverkask.api.service;

import lombok.NonNull;
import ru.dverkask.api.OpticDevice;

import java.util.List;
import java.util.UUID;

public interface OpticDeviceService {
    void create(@NonNull OpticDevice device);
    OpticDevice read(UUID uuid);
    List<OpticDevice> readAll();
    void delete(UUID uuid);
    void zoomIn(UUID uuid, Integer increase);
    void zoomOut(UUID uuid, Integer decrease);
}
