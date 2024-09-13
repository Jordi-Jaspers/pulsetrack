package org.jordijaspers.pulsetrack.security.keycloak;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Configures JWT handling (decoder and validator).
 */
@Configuration
class JwtSecurityConfig {

    /**
     * Configures a decoder with the specified validators (validation key fetched from JWKS endpoint).
     */
    @Bean
    public JwtDecoder jwtDecoder(final List<OAuth2TokenValidator<Jwt>> validators, final OAuth2ResourceServerProperties properties) {
        final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(properties.getJwt().getJwkSetUri())
                .jwsAlgorithms(algs -> algs.addAll(Set.of(SignatureAlgorithm.RS256, SignatureAlgorithm.ES256)))
                .build();

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));
        return jwtDecoder;
    }

    /**
     * Configures the token validator.
     * Specifies two additional validation constraints:
     * <p>
     * * Timestamp on the token is still valid
     * * The issuer is the expected entity
     *
     * @param properties JWT resource specification
     * @return token validator
     */
    @Bean
    public OAuth2TokenValidator<Jwt> defaultTokenValidator(final OAuth2ResourceServerProperties properties) {
        final List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(new JwtTimestampValidator());
        validators.add(new JwtIssuerValidator(properties.getJwt().getIssuerUri()));
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    /**
     * Configures the keycloak JWT Authentication Converter.
     */
    @Bean
    public KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter(
            final Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter) {
        return new KeycloakJwtAuthenticationConverter(authoritiesConverter);
    }

    /**
     * Configures the keycloak granted authorities converter.
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter() {
        return new KeycloakGrantedAuthoritiesConverter(new SimpleAuthorityMapper());
    }
}
