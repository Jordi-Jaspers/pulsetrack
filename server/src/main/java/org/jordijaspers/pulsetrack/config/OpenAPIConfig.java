package org.jordijaspers.pulsetrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.jordijaspers.pulsetrack.config.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * The configuration for the OpenAPI documentation.
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI(final ApplicationProperties properties) {
        final Map<String, String> servers = Map.of(
                "local", "http://localhost:8080",
                "production", "https://pulsetrack.dev/"
        );

        final Contact contact = new Contact();
        contact.setName("SMC DEV Team");
        contact.setUrl("https://www.vodafoneziggo.nl");

        final Info info = new Info()
                .title(properties.getName())
                .version(properties.getVersion())
                .contact(contact)
                .description("A centralized monitoring system which checks the uptime of applications or ElasticSearch queries.");

        return new OpenAPI().info(info).servers(getAvailableServers(servers));
    }

    private List<Server> getAvailableServers(final Map<String, String> serverUrls) {
        return serverUrls.entrySet().stream()
                .map(entry -> {
                    final Server server = new Server();
                    server.setUrl(entry.getValue());
                    server.setDescription("Server URL in '" + entry.getKey() + "' environment");
                    return server;
                })
                .toList();
    }
}
