package com.uni.doit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.uni.doit.DoitApplication;

@SpringBootApplication
public class DoitApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DoitApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(DoitApplication.class, args);
	}

}
