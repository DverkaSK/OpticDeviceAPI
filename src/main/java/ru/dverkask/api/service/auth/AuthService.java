package ru.dverkask.api.service.auth;

import java.util.UUID;

public interface AuthService {
    UserApiKey generate(String username, UserApiKey.Permission permission);
    UserApiKey save(String path);
    UserApiKey findByKey(UUID apiKey);
    boolean isValid(UUID apiKey);
    void deleteByKey(UUID apiKey);
}
