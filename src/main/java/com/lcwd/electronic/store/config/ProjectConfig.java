package com.lcwd.electronic.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProjectConfig implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Password: "+passwordEncoder.encode("rahul"));
    }
}
