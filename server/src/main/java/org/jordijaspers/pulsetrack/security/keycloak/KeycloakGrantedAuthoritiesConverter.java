package org.jordijaspers.pulsetrack.security.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Allows to extract granted authorities from a given JWT.
 * The authorities are determined by combining the realm (overarching) and client (application-specific)
 * roles, and normalizing them (configure them to the default format).
 */
@SuppressWarnings({"ReturnCount", "unchecked"})
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLES_CLAIM = "roles";

    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";

    private static final String REALM_ACCESS_CLAIM = "realm_access";

    private static final Converter<Jwt, Collection<GrantedAuthority>> JWT_SCOPE_GRANTED_AUTHORITIES_CONVERTER =
            new JwtGrantedAuthoritiesConverter();

    private final GrantedAuthoritiesMapper authoritiesMapper;

    @Value("${keycloak.clientId}")
    private String clientId;

    /**
     * Constructor.
     */
    public KeycloakGrantedAuthoritiesConverter(final GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    /**
     * {inheritDoc}.
     *
     * @param jwt the source object to convert, which must be an instance of {@code S} (never {@code null})
     */
    @Override
    public Collection<GrantedAuthority> convert(@NonNull final Jwt jwt) {

        final Collection<GrantedAuthority> authorities = mapKeycloakRolesToAuthorities(
                getRealmRolesFrom(jwt),
                getClientRolesFrom(jwt, clientId)
        );

        final Collection<GrantedAuthority> scopeAuthorities = JWT_SCOPE_GRANTED_AUTHORITIES_CONVERTER.convert(jwt);
        if (!CollectionUtils.isEmpty(scopeAuthorities)) {
            authorities.addAll(scopeAuthorities);
        }

        return authorities;
    }

    /**
     * Map the keycloak roles to Spring authorities.
     */
    protected Collection<GrantedAuthority> mapKeycloakRolesToAuthorities(final Set<String> realmRoles, final Set<String> clientRoles) {
        final List<GrantedAuthority> combinedAuthorities = new ArrayList<>();

        combinedAuthorities.addAll(authoritiesMapper.mapAuthorities(realmRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));

        combinedAuthorities.addAll(authoritiesMapper.mapAuthorities(clientRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));

        return combinedAuthorities;
    }

    /**
     * Retrieve the realm roles from the given JWT.
     */
    protected Set<String> getRealmRolesFrom(final Jwt jwt) {
        final Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        if (CollectionUtils.isEmpty(realmAccess)) {
            return Collections.emptySet();
        }

        final Collection<String> realmRoles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
        if (CollectionUtils.isEmpty(realmRoles)) {
            return Collections.emptySet();
        }

        return new HashSet<>(realmRoles);
    }

    /**
     * Retrieve the client roles from the given JWT.
     */
    protected Set<String> getClientRolesFrom(final Jwt jwt, final String clientId) {
        final Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM);
        if (CollectionUtils.isEmpty(resourceAccess)) {
            return Collections.emptySet();
        }

        final Map<String, List<String>> clientAccess = (Map<String, List<String>>) resourceAccess.get(clientId);
        if (CollectionUtils.isEmpty(clientAccess)) {
            return Collections.emptySet();
        }

        final List<String> clientRoles = clientAccess.get(ROLES_CLAIM);
        if (CollectionUtils.isEmpty(clientRoles)) {
            return Collections.emptySet();
        }

        return new HashSet<>(clientRoles);
    }
}
