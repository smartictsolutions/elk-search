/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

@EnableScheduling
@EntityScan({
    "com.smartict"
})
@ComponentScan(basePackages = {
    "com.smartict"
})
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
