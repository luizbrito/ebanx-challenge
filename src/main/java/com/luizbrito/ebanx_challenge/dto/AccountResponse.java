package com.luizbrito.ebanx_challenge.dto;

/**
 * DTO representing an account returned by the API.
 * <p>
 * Contains the account identifier and its current balance.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
public class AccountResponse {

    /**
     * Unique identifier of the account.
     */
    private String id;

    /**
     * Current account balance.
     */
    private long balance;

    /**
     * Default constructor required for JSON serialization and deserialization.
     */
    public AccountResponse() {
    }

    /**
     * Creates an account response.
     *
     * @param id      the account identifier
     * @param balance the current account balance
     */
    public AccountResponse(String id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}