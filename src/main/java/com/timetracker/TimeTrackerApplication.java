package com.timetracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Time Tracker Api",
                description = "System for recording the time spent on work", version = "1.0.0",
                contact = @Contact(
                        name = "Patapovich Kseniya",
                        email = "ksyu.potapovich@bk.ru",
                        url = "https://github.com/Kseniya-Patapovich"
                )
        )
)
@SpringBootApplication
public class TimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackerApplication.class, args);
    }

}
