package com.hospital.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Hospital Management System API", version = "1.0", description = """
        REST API for managing hospital operations including patients,
        doctors, appointments, prescriptions, and medications.

        Built using Spring Boot, Spring JDBC, and OpenAPI documentation.
        """

))
public class OpenApiConfig {
}
