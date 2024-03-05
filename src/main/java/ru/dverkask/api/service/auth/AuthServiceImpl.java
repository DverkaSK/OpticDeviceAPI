package ru.dverkask.api.service.auth;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.dverkask.api.file.BasicYamlConfiguration;
import ru.dverkask.api.file.DefaultOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private static final List<UserApiKey> apiKeys = new ArrayList<>();
    @Override public UserApiKey generate(String username, UserApiKey.Permission permission) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username can't be null or empty");

        UserApiKey apiKey = UserApiKey.builder()
                    .uuid(UUID.randomUUID())
                    .username(username)
                    .created(LocalDateTime.now())
                    .permission(permission)
                .build();

        save(apiKey);

        return apiKey;
    }

    @Override public void save(@NonNull UserApiKey apiKey) {
        readAllKeys();
        AuthServiceImpl.apiKeys.add(apiKey);
        BasicYamlConfiguration.writeToYAML(DefaultOptions.DEFAULT_AUTH_PATH.getPath(), apiKeys);
    }

    @Override public UserApiKey findByKey(UUID apiKey) {
        readAllKeys();
        return apiKeys.stream().filter(key -> key.getUuid().equals(apiKey)).findFirst().orElse(null);
    }

    @Override public boolean isValid(@NonNull UUID apiKey) {
        readAllKeys();
        return apiKeys.stream().anyMatch(key -> key.getUuid().equals(apiKey));
    }

    @Override public void deleteByKey(UUID apiKey) {
        UserApiKey userApiKey = findByKey(apiKey);
        AuthServiceImpl.apiKeys.remove(userApiKey);
        BasicYamlConfiguration.writeToYAML(DefaultOptions.DEFAULT_AUTH_PATH.getPath(), apiKeys);
    }

    private void readAllKeys() {
        String path = DefaultOptions.DEFAULT_AUTH_PATH.getPath();

        if (!BasicYamlConfiguration.isValidYamlPath(path)) {
            throw new IllegalArgumentException("Invalid YAML path provided");
        }

        List<UserApiKey> loadedDevices = BasicYamlConfiguration.readFromYAML(path, UserApiKey.class);
        if (loadedDevices != null) {
            apiKeys.clear();
            apiKeys.addAll(loadedDevices);
        }
    }
}
