package com.kplian.msaccess.api.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycle {

    private static final Logger LOGGER = Logger.getLogger(AppLifecycle.class);

    @ConfigProperty(name = "quarkus.http.port")
    Integer port;

    @ConfigProperty(name = "quarkus.http.root-path", defaultValue = "/")
    String rootPath;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("==================================================");
        LOGGER.info(" ms-access is starting on port: " + port);
        LOGGER.info(" API Root Path: " + rootPath);
        LOGGER.info("==================================================");
    }
}
