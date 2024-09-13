package org.jordijaspers.pulsetrack.security.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * Converts a JWT into a Spring authentication token.
 * (by extracting the username and roles from the claims of the token, delegating to the {@link KeycloakGrantedAuthoritiesConverter})
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String PREFERRED_USERNAME_CLAIM = "preferred_username";

    private final Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter;

    /**
     * Constructor.
     */
    public KeycloakJwtAuthenticationConverter(final Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter) {
        this.grantedAuthoritiesConverter = grantedAuthoritiesConverter;
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt jwt) {
        final Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
        final String username = getUsernameFrom(jwt);
        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    /**
     * Retrieve the username from the JWT.
     */
    protected String getUsernameFrom(final Jwt jwt) {
        return jwt.hasClaim(PREFERRED_USERNAME_CLAIM)
                ? jwt.getClaimAsString(PREFERRED_USERNAME_CLAIM)
                : jwt.getSubject();
    }
}
