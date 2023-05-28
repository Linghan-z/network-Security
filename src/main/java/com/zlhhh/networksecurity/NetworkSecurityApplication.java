package com.zlhhh.networksecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class NetworkSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkSecurityApplication.class, args);
    }

}
