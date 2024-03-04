package ru.dverkask.api.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.experimental.UtilityClass;
import ru.dverkask.api.OpticDevice;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class BasicYamlConfiguration {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    public static boolean isValidYamlPath(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && Paths.get(path).toString().endsWith(".yaml");
    }

    public static List<OpticDevice> readFromYAML(String path) throws IOException {
        if (!BasicYamlConfiguration.isValidYamlPath(path)) {
            throw new IllegalArgumentException("Invalid YAML path provided");
        }

        try (InputStream inputStream = new FileInputStream(path)) {
            return mapper.readValue(inputStream, new TypeReference<>() {});
        }
    }

    public static void writeToYAML(String path, List<OpticDevice> devices) throws IOException {
        if (!BasicYamlConfiguration.isValidYamlPath(path)) {
            throw new IllegalArgumentException("Invalid YAML path provided");
        }

        try (FileWriter fileWriter = new FileWriter(path)) {
            mapper.writeValue(fileWriter, devices);
        }
    }
}
