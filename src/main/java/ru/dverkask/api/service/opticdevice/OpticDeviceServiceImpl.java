package ru.dverkask.api.service.opticdevice;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.dverkask.api.OpticDevice;
import ru.dverkask.api.file.BasicYamlConfiguration;
import ru.dverkask.api.file.DefaultOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OpticDeviceServiceImpl implements OpticDeviceService {
    private static       String            PATH_TO_DATA;
    private static final List<OpticDevice> devices = new ArrayList<>();

    @SneakyThrows @Override public void save(@NonNull OpticDevice device) {
        OpticDeviceServiceImpl.devices.add(device);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @Override public OpticDevice find(UUID uuid) {
        init();
        return OpticDeviceServiceImpl.devices.stream()
                .filter(opticDevice -> opticDevice.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"
                ));
    }

    @Override public List<OpticDevice> findAll() {
        init();
        return Collections.unmodifiableList(devices);
    }

    @SneakyThrows @Override public void delete(UUID uuid) {
        OpticDevice device = find(uuid);
        OpticDeviceServiceImpl.devices.remove(device);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @SneakyThrows @Override public void zoomIn(UUID uuid,
                                 Integer increase) {
        OpticDevice device = find(uuid);
        if (increase != null)
            device.zoomIn(increase);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @SneakyThrows @Override public void zoomOut(UUID uuid,
                                  Integer decrease) {
        OpticDevice device = find(uuid);
        if (decrease != null)
            device.zoomOut(decrease);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @PostConstruct
    private void init() {
        try {
            List<OpticDevice> loadedDevices = BasicYamlConfiguration.readFromYAML(PATH_TO_DATA, OpticDevice.class);
            if (loadedDevices != null) {
                devices.clear();
                devices.addAll(loadedDevices);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${api.data.yaml.path}")
    private void setPathToData(String pathToData) {
        PATH_TO_DATA = BasicYamlConfiguration.isValidYamlPath(pathToData)
                ? pathToData
                : DefaultOptions.DEFAULT_DATA_PATH.getPath();
    }
}