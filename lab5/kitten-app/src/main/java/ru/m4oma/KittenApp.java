package ru.m4oma;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "ru.m4oma",
})
public class KittenApp {
    public static void main(String[] args) {
        SpringApplication.run(KittenApp.class, args);

    }
}
