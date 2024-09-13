package org.jordijaspers.pulsetrack.common.util.keycloak;

/**
 * This class contains all the Keycloak authorities that are used in the application.
 */
public final class KeycloakAuthorities {

    /* ------------------------------- BASIC AUTHORITIES ------------------------------- */

    public static final String OFFLINE_ACCESS_ROLE = "offline_access";
    public static final String UMA_AUTHORIZATION_ROLE = "uma_authorization";

    /* ------------------------------- AUTHORITIES ------------------------------- */

    /* ------------------------------- END ------------------------------- */

    private KeycloakAuthorities() {
        // Utility class
    }
}
