package org.jordijaspers.pulsetrack.config;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.pulsetrack.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * A configuration class that provisions data when the application context is started.
 */
@Component
@RequiredArgsConstructor
public class DataProvisioningConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProvisioningConfiguration.class);

    private final ApplicationProperties applicationProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void loadData() {
        LOGGER.info("Application context for '{} ({})' has been started, provisioning data...",
                applicationProperties.getName(),
                applicationProperties.getVersion());

        LOGGER.info("Data provisioning completed successfully.");
    }
}
