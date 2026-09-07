package com.luizbrito.ebanx_challenge.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the supported event types for account operations.
 * <p>
 * Each enum value is mapped to the corresponding value expected
 * by the EBANX API contract.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public enum EventType {

    /**
     * Adds funds to a destination account.
     */
    DEPOSIT("deposit"),

    /**
     * Removes funds from an origin account.
     */
    WITHDRAW("withdraw"),

    /**
     * Transfers funds from an origin account to a destination account.
     */
    TRANSFER("transfer");

    /**
     * JSON representation of the event type.
     */
    private final String value;

    /**
     * Creates an event type with its API representation.
     *
     * @param value value used in the JSON payload
     */
    EventType(String value) {
        this.value = value;
    }

    /**
     * Returns the value used when serializing the enum to JSON.
     *
     * @return JSON representation of the event type
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Converts a JSON event type value into the corresponding enum.
     *
     * @param value event type received in the request
     * @return corresponding EventType
     * @throws IllegalArgumentException if the event type is not supported
     */
    @JsonCreator
    public static EventType fromValue(String value) {

        for (EventType type : EventType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Invalid event type: " + value
        );
    }
}