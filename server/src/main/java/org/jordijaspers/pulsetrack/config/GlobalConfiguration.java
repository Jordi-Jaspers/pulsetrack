package org.jordijaspers.pulsetrack.config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * A global configuration class with default settings / beans.
 */
public final class GlobalConfiguration {

    /**
     * The global serial version for the application.
     */
    public static final long SERIAL_VERSION_UID = 0L;

    /**
     * The default date time formatter for {@link java.time.ZonedDateTime}.
     */
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * The default timezone to use.
     */
    public static final String DEFAULT_TIMEZONE = "UTC";

    /**
     * The default timezone.
     */
    public static final ZoneId EUROPE_AMSTERDAM = ZoneId.of("Europe/Amsterdam");

    /**
     * No-args constructor.
     */
    private GlobalConfiguration() {
        // Private constructor for utility class
    }
}
