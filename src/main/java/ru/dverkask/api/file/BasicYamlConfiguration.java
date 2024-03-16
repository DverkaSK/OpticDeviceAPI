package ru.dverkask.api.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class BasicYamlConfiguration {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static boolean isValidYamlPath(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && Paths.get(path).toString().endsWith(".yaml");
    }

    @SneakyThrows public static <T> List<T> readFromYAML(String path, Class<T> clazz) {
        if (!BasicYamlConfiguration.isValidYamlPath(path)) {
            throw new IllegalArgumentException("Invalid YAML path provided");
        }

        try (InputStream inputStream = new FileInputStream(path)) {
            return mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        }
    }

    @SneakyThrows public static void writeToYAML(String path, Object data) {
        if (!BasicYamlConfiguration.isValidYamlPath(path)) {
            throw new IllegalArgumentException("Invalid YAML path provided");
        }

        try (FileWriter fileWriter = new FileWriter(path)) {
            mapper.writeValue(fileWriter, data);
        }
    }
}