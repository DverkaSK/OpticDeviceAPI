package ru.dverkask.api.file;

import lombok.Getter;

@Getter
public enum DefaultOptions {
    DEFAULT_DATA_PATH("src/main/resources/data.yaml");
    private final String path;
    DefaultOptions(String path) {
        this.path = path;
    }
}
