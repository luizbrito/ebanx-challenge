package com.luizbrito.ebanx_challenge.dto;

/**
 * DTO representing the response of a transfer event.
 * <p>
 * Contains both the origin and destination accounts after
 * the transfer has been processed.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public class TransferResponse {

    /**
     * Origin account after the transfer.
     */
    private AccountResponse origin;

    /**
     * Destination account after the transfer.
     */
    private AccountResponse destination;

    /**
     * Default constructor.
     */
    public TransferResponse() {
    }

    /**
     * Creates a transfer response.
     *
     * @param origin      origin account after the transfer
     * @param destination destination account after the transfer
     */
    public TransferResponse(
            AccountResponse origin,
            AccountResponse destination) {

        this.origin = origin;
        this.destination = destination;
    }

    public AccountResponse getOrigin() {
        return origin;
    }

    public void setOrigin(AccountResponse origin) {
        this.origin = origin;
    }

    public AccountResponse getDestination() {
        return destination;
    }

    public void setDestination(AccountResponse destination) {
        this.destination = destination;
    }
}