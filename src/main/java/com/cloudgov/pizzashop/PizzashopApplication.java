package com.cloudgov.pizzashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cloudgov.pizzashop"})
public class PizzashopApplication {
    private static final Logger log = LoggerFactory.getLogger(PizzashopApplication.class);

    public static void main(String[] args) {
        log.info("Starting Pizza Shop Application");
        SpringApplication.run(PizzashopApplication.class, args);
    }
}
