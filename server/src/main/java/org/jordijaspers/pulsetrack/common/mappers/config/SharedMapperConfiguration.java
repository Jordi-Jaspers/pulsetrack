package org.jordijaspers.pulsetrack.common.mappers.config;

import org.jordijaspers.pulsetrack.common.mappers.DateTimeMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * A shared mapper configuration for the MapStruct mappers.
 */
@MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = DateTimeMapper.class)
public interface SharedMapperConfiguration {
    // Just an empty interface for the mapper configuration.
}
