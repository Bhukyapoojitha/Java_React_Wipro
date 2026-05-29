package com.wipro.mongodemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Banking Management System API")
                .description("MongoDB embedded document demo — Account + Transactions")
                .version("1.0.0"));
    }
}
