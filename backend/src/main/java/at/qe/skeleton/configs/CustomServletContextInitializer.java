package at.qe.skeleton.configs;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import at.qe.skeleton.Main;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for servlet context.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Configuration
public class CustomServletContextInitializer extends SpringBootServletInitializer {

    @Override
    public SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }
}
