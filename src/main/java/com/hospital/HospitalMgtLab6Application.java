package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HospitalMgtLab6Application {

    public static void main(String[] args) {
        SpringApplication.run(HospitalMgtLab6Application.class, args);
    }

}
