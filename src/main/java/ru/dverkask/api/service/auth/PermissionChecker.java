package ru.dverkask.api.service.auth;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class PermissionChecker {
    public static boolean checkPermission(UserApiKey apiKey,
                                          UserApiKey.Permission... requiredPermission) {
        return apiKey != null && Arrays.asList(requiredPermission).contains(apiKey.getPermission());
    }
}
