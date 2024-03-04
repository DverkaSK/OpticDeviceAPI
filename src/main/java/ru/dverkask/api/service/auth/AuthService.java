package ru.dverkask.api.service.auth;

import java.util.UUID;

public interface AuthService {
    UserApiKey generate(String username, UserApiKey.Permission permission);
    void save(UserApiKey apiKey);
    UserApiKey findByKey(UUID apiKey);
    boolean isValid(UUID apiKey);
    void deleteByKey(UUID apiKey);
}
