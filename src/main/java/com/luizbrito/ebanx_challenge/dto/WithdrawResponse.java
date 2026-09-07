package com.luizbrito.ebanx_challenge.dto;

/**
 * DTO representing the response of a withdraw event.
 * <p>
 * Contains the origin account after the withdrawal has been processed.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public class WithdrawResponse {

    /**
     * Origin account affected by the withdrawal.
     */
    private AccountResponse origin;

    /**
     * Default constructor.
     */
    public WithdrawResponse() {
    }

    /**
     * Creates a withdraw response.
     *
     * @param origin origin account after the withdrawal
     */
    public WithdrawResponse(AccountResponse origin) {
        this.origin = origin;
    }

    public AccountResponse getOrigin() {
        return origin;
    }

    public void setOrigin(AccountResponse origin) {
        this.origin = origin;
    }
}