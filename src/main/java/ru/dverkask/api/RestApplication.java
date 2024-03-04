package ru.dverkask.api;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RestApplication {
    @Getter
    private static ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
        ctx = SpringApplication.run(RestApplication.class, args);
    }
}
