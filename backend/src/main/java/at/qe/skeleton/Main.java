package at.qe.skeleton;

import at.qe.skeleton.configs.WebSecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot application. Execute maven with <code>mvn spring-boot:run</code>
 * to start this web application.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootApplication
/*
 * Prevent spring from trying to autowire the websocket-infrastructure: Exclude
 * the at.qe.skeleton.ui.websockets package from component scan.
 *
 * NOTE: Do not add any components to this package which should be managed by
 * spring. It is reserved for the CDI-injection-mechanisms (Weld). Only add
 * CDI-managed components.
 */
public class Main extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{Main.class,  WebSecurityConfig.class});
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
