package com.wipro.myspringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.wipro")   
public class Myspringjdbc1Application {

    public static void main(String[] args) {
        SpringApplication.run(Myspringjdbc1Application.class, args);
    }
}
