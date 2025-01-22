package com.project01.reactspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReactSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactSpringApplication.class, args);
    }

}
