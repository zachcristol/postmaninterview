package com.postman.planetexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Profiles;

/** Spring Boot entry point for the Planet Express pre-assessment service. */
@SpringBootApplication
public class PlanetExpressApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PlanetExpressApplication.class, args);
        // The CLI is one-shot: propagate its exit code. The web service keeps running.
        if (context.getEnvironment().acceptsProfiles(Profiles.of("cli"))) {
            System.exit(SpringApplication.exit(context));
        }
    }
}
