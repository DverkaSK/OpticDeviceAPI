package ru.dverkask.api.service.opticdevice;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.dverkask.api.OpticDevice;
import ru.dverkask.api.file.BasicYamlConfiguration;
import ru.dverkask.api.file.DefaultOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OpticDeviceServiceImpl implements OpticDeviceService {
    private static       String            PATH_TO_DATA;
    private static final List<OpticDevice> devices = new ArrayList<>();

    @Override public void save(@NonNull OpticDevice device) {
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

    @Override public void delete(UUID uuid) {
        OpticDevice device = find(uuid);
        OpticDeviceServiceImpl.devices.remove(device);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @Override public void zoomIn(UUID uuid,
                                 Integer increase) {
        OpticDevice device = find(uuid);
        if (increase != null)
            device.zoomIn(increase);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @Override public void zoomOut(UUID uuid,
                                  Integer decrease) {
        OpticDevice device = find(uuid);
        if (decrease != null)
            device.zoomOut(decrease);
        BasicYamlConfiguration.writeToYAML(PATH_TO_DATA, devices);
    }

    @Override public double calculatePercentile(List<OpticDevice> devices, double percentileValue) {
        double[] opticPowers = devices.stream()
                .mapToDouble(OpticDevice::getOpticPower)
                .sorted()
                .toArray();

        Percentile percentile = new Percentile();
        return percentile.evaluate(opticPowers, percentileValue);
    }

    @PostConstruct
    private void init() {
        List<OpticDevice> loadedDevices = BasicYamlConfiguration.readFromYAML(PATH_TO_DATA, OpticDevice.class);
        if (loadedDevices != null) {
            devices.clear();
            devices.addAll(loadedDevices);
        }
    }

    @Value("${api.data.yaml.path}")
    private void setPathToData(String pathToData) {
        PATH_TO_DATA = BasicYamlConfiguration.isValidYamlPath(pathToData)
                ? pathToData
                : DefaultOptions.DEFAULT_DATA_PATH.getPath();
    }
}