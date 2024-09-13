package org.jordijaspers.pulsetrack.common.util.keycloak;

import org.hawaiiframework.web.exception.UnauthorizedRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static java.util.Objects.isNull;
import static org.jordijaspers.pulsetrack.common.util.keycloak.JwtClaimNames.EMAIL;
import static org.jordijaspers.pulsetrack.common.util.keycloak.JwtClaimNames.SUBJECT;

/**
 * Temporary helper class solution for user identification details until we upgraded to spring boot 3.0+.
 * Test are failing because the @WithMockUser creates a SpringSecurityContext with a UsernamePasswordAuthenticationToken
 * instead of a KeycloakAuthenticationToken. The fix was way to much work to implement for a temporary solution, because of keycloak
 * adapters which are disappearing in the future.
 * <p>
 * This class will retrieve requested use details from a KeycloakAuthenticationToken which is always available in the SecurityContext
 * on runtime of the application. This class has fallback method whenever the user is not logged in or the authentication object is not
 * a KeycloakAuthenticationToken, but a Spring Security context object instead.
 */
public final class JwtAuthenticationUtil {

    private JwtAuthenticationUtil() {
        // No-args constructor to prevent instantiation.
    }

    /**
     * Retrieve the logged-in user from the security context.
     */
    public static String getLoggedInUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getEmailAddress(authentication);
    }

    /**
     * @return the email address of the user that is currently logged in. if there is no email address available, the username is returned.
     * unless the user is not logged in, then an exception is thrown.
     */
    public static String getEmailAddress(final Authentication authentication) {
        validateAuthentication(authentication);
        return getStringClaim(authentication, EMAIL);
    }

    /**
     * @return the username of the user that is currently logged in. unless the user is not logged in, then an exception is thrown.
     */
    public static String getSubject(final Authentication authentication) {
        validateAuthentication(authentication);
        return getStringClaim(authentication, SUBJECT);
    }

    /**
     * @return the information of the provided claim located in the JWT token of the logged-in user.
     * unless the user is not logged in, then an exception is thrown.
     */
    public static String getStringClaim(final Authentication authentication, final String claim) {
        if (authentication instanceof final JwtAuthenticationToken jwtAuthentication) {
            return jwtAuthentication.getToken().getClaimAsString(claim);
        } else {
            throw new UnauthorizedRequestException("Unsupported authentication object");
        }
    }

    private static void validateAuthentication(final Authentication authentication) {
        if (isNull(authentication) || !authentication.isAuthenticated()) {
            throw new UnauthorizedRequestException("Invalid authentication object: User is not logged in");
        }
    }
}
