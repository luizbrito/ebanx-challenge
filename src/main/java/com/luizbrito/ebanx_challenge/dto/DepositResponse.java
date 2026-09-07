package com.luizbrito.ebanx_challenge.dto;

/**
 * DTO representing the response of a deposit event.
 * <p>
 * Contains the destination account after the deposit has been processed.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public class DepositResponse {

    /**
     * Destination account affected by the deposit.
     */
    private AccountResponse destination;

    /**
     * Default constructor.
     */
    public DepositResponse() {
    }

    /**
     * Creates a deposit response.
     *
     * @param destination destination account after the deposit
     */
    public DepositResponse(AccountResponse destination) {
        this.destination = destination;
    }

    public AccountResponse getDestination() {
        return destination;
    }

    public void setDestination(AccountResponse destination) {
        this.destination = destination;
    }
}