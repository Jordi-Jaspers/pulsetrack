package org.jordijaspers.pulsetrack.common.exception;

import org.hawaiiframework.exception.ApiError;

import java.util.Objects;

/**
 * Defines an error code and reason for any exception handling.
 */
public enum ApiErrorCode implements ApiError {
    INTERNAL_SERVER_ERROR(
            "TRACK-0001",
            "Uncaught Exception: You think I know what went wrong here? If I did, I would've caught this exception no?"),
    DATABASE_ERROR(
            "TRACK-0002",
            "Could not perform the requested action on the database. Contact administrator.");

    /**
     * The error code.
     */
    private final String errorCode;

    /**
     * The reason.
     */
    private final String reason;

    /**
     * Construct an instance with error code and reason.
     *
     * @param errorCode the error code
     * @param reason    the reason
     */
    ApiErrorCode(final String errorCode, final String reason) {
        this.errorCode = Objects.requireNonNull(errorCode);
        this.reason = Objects.requireNonNull(reason);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String getReason() {
        return reason;
    }
}
