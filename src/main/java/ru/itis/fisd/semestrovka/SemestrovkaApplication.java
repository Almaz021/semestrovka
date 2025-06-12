package ru.itis.fisd.semestrovka;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "FISD API",
                version = "1.0",
                description = "Документация API проекта Semestrovka"
        )
)
@SpringBootApplication
public class SemestrovkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemestrovkaApplication.class, args);
    }

}
