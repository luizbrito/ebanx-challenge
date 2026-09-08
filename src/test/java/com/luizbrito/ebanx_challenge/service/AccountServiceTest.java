package com.luizbrito.ebanx_challenge.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.luizbrito.ebanx_challenge.dto.AccountResponse;
import com.luizbrito.ebanx_challenge.dto.TransferResponse;

/**
 * Unit tests for {@link AccountService}.
 * <p>
 * These tests validate the business rules independently
 * from the HTTP transport layer and Spring context.
 * </p>
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-08
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
class AccountServiceTest {

    private AccountService accountService;

    /**
     * Creates a fresh service instance before each test,
     * ensuring an empty in-memory account state.
     */
    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    /**
     * Verifies that a non-existing account has no balance.
     */
    @Test
    void shouldReturnEmptyBalanceForNonExistingAccount() {

        Optional<Long> balance =
                accountService.getBalance("1234");

        assertTrue(balance.isEmpty());
    }

    /**
     * Verifies that a deposit creates a new account.
     */
    @Test
    void shouldCreateAccountWithDeposit() {

        AccountResponse account =
                accountService.deposit("100", 10);

        assertEquals("100", account.getId());
        assertEquals(10, account.getBalance());
    }

    /**
     * Verifies that multiple deposits accumulate
     * the account balance.
     */
    @Test
    void shouldDepositIntoExistingAccount() {

        accountService.deposit("100", 10);

        AccountResponse account =
                accountService.deposit("100", 10);

        assertEquals("100", account.getId());
        assertEquals(20, account.getBalance());
    }

    /**
     * Verifies that the current balance can be retrieved
     * after an account has been created.
     */
    @Test
    void shouldReturnBalanceForExistingAccount() {

        accountService.deposit("100", 20);

        Optional<Long> balance =
                accountService.getBalance("100");

        assertTrue(balance.isPresent());
        assertEquals(20, balance.get());
    }

    /**
     * Verifies that withdrawing from a non-existing
     * account returns an empty result.
     */
    @Test
    void shouldNotWithdrawFromNonExistingAccount() {

        Optional<AccountResponse> result =
                accountService.withdraw("200", 10);

        assertTrue(result.isEmpty());
    }

    /**
     * Verifies that a withdrawal updates the
     * balance of an existing account.
     */
    @Test
    void shouldWithdrawFromExistingAccount() {

        accountService.deposit("100", 20);

        Optional<AccountResponse> result =
                accountService.withdraw("100", 5);

        assertTrue(result.isPresent());

        AccountResponse account = result.get();

        assertEquals("100", account.getId());
        assertEquals(15, account.getBalance());
    }

    /**
     * Verifies that a transfer updates both
     * origin and destination accounts.
     */
    @Test
    void shouldTransferBetweenAccounts() {

        accountService.deposit("100", 15);

        Optional<TransferResponse> result =
                accountService.transfer(
                        "100",
                        "300",
                        15
                );

        assertTrue(result.isPresent());

        TransferResponse transfer = result.get();

        assertEquals("100", transfer.getOrigin().getId());
        assertEquals(0, transfer.getOrigin().getBalance());

        assertEquals("300", transfer.getDestination().getId());
        assertEquals(15, transfer.getDestination().getBalance());
    }

    /**
     * Verifies that a transfer is not performed
     * when the origin account does not exist.
     */
    @Test
    void shouldNotTransferFromNonExistingAccount() {

        Optional<TransferResponse> result =
                accountService.transfer(
                        "200",
                        "300",
                        15
                );

        assertTrue(result.isEmpty());

        assertTrue(
                accountService
                        .getBalance("300")
                        .isEmpty()
        );
    }

    /**
     * Verifies that resetting the service removes
     * all accounts stored in memory.
     */
    @Test
    void shouldResetAccounts() {

        accountService.deposit("100", 10);
        accountService.deposit("300", 20);

        accountService.reset();

        assertTrue(
                accountService
                        .getBalance("100")
                        .isEmpty()
        );

        assertTrue(
                accountService
                        .getBalance("300")
                        .isEmpty()
        );
    }
}