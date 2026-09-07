package com.luizbrito.ebanx_challenge.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.luizbrito.ebanx_challenge.dto.AccountResponse;
import com.luizbrito.ebanx_challenge.dto.TransferResponse;

/**
 * Service responsible for managing account operations.
 * <p>
 * This service stores account balances in memory and provides
 * business operations for balance retrieval, deposits, withdrawals,
 * transfers, and application state reset.
 * </p>
 *
 * No persistence mechanism is used because durability is not
 * required by the EBANX challenge specification.
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
@Service
public class AccountService {

    /**
     * In-memory storage for account balances.
     * <p>
     * The key represents the account identifier and the value
     * represents the current account balance.
     * </p>
     */
    private final Map<String, Long> accounts = new HashMap<>();

    /**
     * Removes all accounts from the in-memory storage.
     */
    public synchronized void reset() {
        accounts.clear();
    }

    /**
     * Retrieves the current balance of an account.
     *
     * @param accountId the account identifier
     * @return an Optional containing the account balance if the account exists,
     *         otherwise an empty Optional
     */
    public synchronized Optional<Long> getBalance(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    /**
     * Deposits an amount into a destination account.
     * <p>
     * If the account does not exist, it is automatically created
     * with the deposited amount as its initial balance.
     * </p>
     *
     * @param destination the destination account identifier
     * @param amount      the amount to deposit
     * @return the destination account with its updated balance
     */
    public synchronized AccountResponse deposit(
            String destination,
            long amount) {

        long currentBalance = accounts.getOrDefault(destination, 0L);
        long newBalance = currentBalance + amount;

        accounts.put(destination, newBalance);

        return new AccountResponse(destination, newBalance);
    }

    /**
     * Withdraws an amount from an origin account.
     *
     * @param origin the origin account identifier
     * @param amount the amount to withdraw
     * @return an Optional containing the origin account with its updated balance
     *         if the account exists, otherwise an empty Optional
     */
    public synchronized Optional<AccountResponse> withdraw(
            String origin,
            long amount) {

        Long currentBalance = accounts.get(origin);

        if (currentBalance == null) {
            return Optional.empty();
        }

        long newBalance = currentBalance - amount;

        accounts.put(origin, newBalance);

        return Optional.of(
                new AccountResponse(origin, newBalance)
        );
    }

    /**
     * Transfers an amount from an origin account to a destination account.
     * <p>
     * The transfer is only performed if the origin account exists.
     * If the destination account does not exist, it is automatically created.
     * </p>
     *
     * @param origin      the origin account identifier
     * @param destination the destination account identifier
     * @param amount      the amount to transfer
     * @return an Optional containing the updated origin and destination accounts
     *         if the origin account exists, otherwise an empty Optional
     */
    public synchronized Optional<TransferResponse> transfer(
            String origin,
            String destination,
            long amount) {

        Long originBalance = accounts.get(origin);

        if (originBalance == null) {
            return Optional.empty();
        }

        long newOriginBalance = originBalance - amount;

        long destinationBalance =
                accounts.getOrDefault(destination, 0L);

        long newDestinationBalance =
                destinationBalance + amount;

        accounts.put(origin, newOriginBalance);
        accounts.put(destination, newDestinationBalance);

        AccountResponse originAccount =
                new AccountResponse(origin, newOriginBalance);

        AccountResponse destinationAccount =
                new AccountResponse(destination, newDestinationBalance);

        return Optional.of(
                new TransferResponse(
                        originAccount,
                        destinationAccount
                )
        );
    }
}