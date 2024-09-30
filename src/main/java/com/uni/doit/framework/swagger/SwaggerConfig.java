package com.uni.doit.framework.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class SwaggerConfig {
	@Bean
	OpenAPI openAPI() {
	    return new OpenAPI()
	            .components(new Components())
	            .info(apiInfo());
	}
 
    private Info apiInfo() {
        return new Info()
                .title("Doit Springdoc")
                .description("Doit.Server Swagger UI")
                .version("1.0.0");
    }
}