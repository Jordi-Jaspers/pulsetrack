package org.jordijaspers.pulsetrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

import static org.jordijaspers.pulsetrack.config.GlobalConfiguration.DEFAULT_TIMEZONE;

/**
 * The main class of the application.
 */
@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class PulseTrack extends SpringBootServletInitializer {

    /**
     * The main method to run the spring boot application.
     */
    public static void main(final String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        SpringApplication.run(PulseTrack.class, args);
    }
}
