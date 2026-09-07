package com.luizbrito.ebanx_challenge.dto;

/**
 * DTO for an event request.
 * <p>
 * Represents an operation performed on one or more accounts.
 * Depending on the event type, the origin or destination account
 * may not be provided.
 * </p>
 *
 * Supported event types:
 * <ul>
 *     <li>DEPOSIT - Adds an amount to a destination account.</li>
 *     <li>WITHDRAW - Removes an amount from an origin account.</li>
 *     <li>TRANSFER - Transfers an amount from an origin account to a destination account.</li>
 * </ul>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public class EventRequest {

    /**
     * Type of event to be processed.
     * Possible values are DEPOSIT, WITHDRAW, and TRANSFER.
     */
    private EventType type;

    /**
     * Origin account identifier.
     * This field can be null for DEPOSIT events.
     */
    private String origin;

    /**
     * Destination account identifier.
     * This field can be null for WITHDRAW events.
     */
    private String destination;

    /**
     * Amount involved in the event.
     */
    private long amount;

    /**
     * Default constructor required for JSON deserialization.
     */
    public EventRequest() {
    }

    /**
     * Creates a new event request.
     *
     * @param type        the type of event
     * @param origin      the origin account identifier
     * @param destination the destination account identifier
     * @param amount      the amount involved in the event
     */
    public EventRequest(
            EventType type,
            String origin,
            String destination,
            long amount) {

        this.type = type;
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}