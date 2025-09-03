package com.eretailgoals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Spring Boot application class for BookKeeping Backend API
 * 
 * @author BookKeeping Team
 * @version 2.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class BookKeepingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookKeepingApplication.class, args);
    }
}