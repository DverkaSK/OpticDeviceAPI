package ru.dverkask.api.file;

import lombok.Getter;

@Getter
public enum DefaultOptions {
    DEFAULT_DATA_PATH("src/main/resources/data.yaml"),
    DEFAULT_AUTH_PATH("src/main/resources/auth.yaml");
    private final String path;
    DefaultOptions(String path) {
        this.path = path;
    }
}
