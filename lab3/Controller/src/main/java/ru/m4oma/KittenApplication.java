package ru.m4oma;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = {
        "ru.m4oma",
})
public class KittenApplication {
    public static void main(String[] args) {
        SpringApplication.run(KittenApplication.class, args);
    }
}