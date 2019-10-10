package com.tcl;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;




@SpringBootApplication
public class JobRunner {

    public static void main(String[] args) {

        SpringApplication.run(JobRunner.class, args);
    }


}
