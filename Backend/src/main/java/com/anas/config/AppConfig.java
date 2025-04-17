package com.anas.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("/WEB-INF/frontController-servlet.xml")  // Import your existing XML config
public class AppConfig {
    // Additional Java-based configuration can go here
}

