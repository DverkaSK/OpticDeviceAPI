package ru.dverkask.api.service.auth;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserApiKey {
    private UUID uuid;
    private String username;
    private LocalDateTime created;
    private Permission permission;

    private UserApiKey() {
    }

    public enum Permission {
        WRITE,
        READ,
        ADMIN
        ;
    }

    @Override
    public String toString() {
        return "UserApiKey{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", created=" + created +
                ", permission=" + permission +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private String username;
        private LocalDateTime created;
        private Permission permission;

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public Builder permission(Permission permission) {
            this.permission = permission;
            return this;
        }

        public UserApiKey build() {
            UserApiKey userApiKey = new UserApiKey();
            userApiKey.uuid = this.uuid;
            userApiKey.username = this.username;
            userApiKey.created = this.created;
            userApiKey.permission = this.permission;
            return userApiKey;
        }
    }
}
