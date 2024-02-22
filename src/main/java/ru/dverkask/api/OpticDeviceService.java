package ru.dverkask.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpticDeviceService {
    private static final ObjectMapper      mapper       = new ObjectMapper(new YAMLFactory());
    private static final String            PATH_TO_DATA = "src/main/resources/data.yaml";
    @Getter
    private static final List<OpticDevice> devices      = new ArrayList<>();

    @SneakyThrows public static void readFromYAML() {
        InputStream inputStream = new FileInputStream(PATH_TO_DATA);

        List<OpticDevice> loadedDevices = mapper
                .readValue(
                        inputStream,
                        new TypeReference<>() {}
                );

        if (loadedDevices != null) {
            devices.clear();
            devices.addAll(loadedDevices);
        }
    }

    @SneakyThrows public static void writeToYAML() {
        FileWriter fileWriter = new FileWriter(PATH_TO_DATA);
        mapper.writeValue(fileWriter, devices);
    }

    public static OpticDevice getDeviceByUUID(UUID uuid) {
        return OpticDeviceService.getDevices().stream()
                .filter(opticDevice -> opticDevice.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device not found"
                ));
    }
}
