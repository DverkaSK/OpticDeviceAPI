package ru.dverkask.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.dverkask.api.OpticDevice;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpticDeviceServiceImpl implements OpticDeviceService {
    private static final ObjectMapper      mapper  = new ObjectMapper(new YAMLFactory());
    private static       String            PATH_TO_DATA;
    private static final List<OpticDevice> devices = new ArrayList<>();

    @Override public void create(@NonNull OpticDevice device) {
        writeToYAML(device);
    }

    @Override public OpticDevice read(UUID uuid) {
        return OpticDeviceServiceImpl.devices.stream()
                .filter(opticDevice -> opticDevice.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"
                ));
    }

    @Override public List<OpticDevice> readAll() {
        return devices;
    }

    @Override public void delete(UUID uuid) {
        OpticDevice device = read(uuid);
        OpticDeviceServiceImpl.devices.remove(device);
        writeToYAML();
    }

    @Override public void zoomIn(UUID uuid,
                                 Integer increase) {
        OpticDevice device = read(uuid);
        if (increase != null)
            device.zoomIn(increase);
        writeToYAML();
    }

    @Override public void zoomOut(UUID uuid,
                                  Integer decrease) {
        OpticDevice device = read(uuid);
        if (decrease != null)
            device.zoomOut(decrease);
        writeToYAML();
    }

    @SneakyThrows private static void readFromYAML() {
        InputStream inputStream = new FileInputStream(PATH_TO_DATA);

        List<OpticDevice> loadedDevices = mapper
                .readValue(
                        inputStream,
                        new TypeReference<>() {
                        }
                );

        if (loadedDevices != null) {
            devices.clear();
            devices.addAll(loadedDevices);
        }
    }

    @SneakyThrows private static void writeToYAML() {
        FileWriter fileWriter = new FileWriter(PATH_TO_DATA);
        mapper.writeValue(fileWriter, devices);
    }

    @SneakyThrows private static void writeToYAML(@NonNull OpticDevice device) {
        OpticDeviceServiceImpl.devices.add(device);
        writeToYAML();
    }

    @PostConstruct
    private void init() {
        readFromYAML();
    }

    @Value("${api.data.yaml.path}")
    private void setPathToData(String pathToData) {
        PATH_TO_DATA = pathToData;
    }
}