package com.luizbrito.ebanx_challenge.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the account API.
 * <p>
 * These tests validate the HTTP contract required by the
 * EBANX Software Engineer Take-home Challenge, including
 * balance retrieval, deposits, withdrawals, transfers,
 * and application state reset.
 * </p>
 *
 * Each test starts with an empty application state to ensure
 * independence between test scenarios.
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    /**
     * Mock HTTP client used to execute requests against
     * the Spring MVC application without starting a real server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Resets the in-memory application state before each test.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @BeforeEach
    void setUp() throws Exception {

        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that resetting the application returns HTTP 200.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldResetApplicationState() throws Exception {

        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that requesting the balance of a non-existing
     * account returns HTTP 404 and body 0.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldReturnNotFoundForNonExistingAccount() throws Exception {

        mockMvc.perform(
                        get("/balance")
                                .param("account_id", "1234")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    /**
     * Verifies that a deposit creates a new account with
     * the deposited amount as its initial balance.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldCreateAccountWithInitialBalance() throws Exception {

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "deposit",
                                            "destination": "100",
                                            "amount": 10
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.id").value("100"))
                .andExpect(jsonPath("$.destination.balance").value(10));
    }

    /**
     * Verifies that a deposit into an existing account
     * increases its current balance.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldDepositIntoExistingAccount() throws Exception {

        deposit("100", 10);

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "deposit",
                                            "destination": "100",
                                            "amount": 10
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.id").value("100"))
                .andExpect(jsonPath("$.destination.balance").value(20));
    }

    /**
     * Verifies that the balance endpoint returns the current
     * balance of an existing account.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldReturnBalanceForExistingAccount() throws Exception {

        deposit("100", 20);

        mockMvc.perform(
                        get("/balance")
                                .param("account_id", "100")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    /**
     * Verifies that withdrawing from a non-existing account
     * returns HTTP 404 and body 0.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldReturnNotFoundWhenWithdrawingFromNonExistingAccount()
            throws Exception {

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "withdraw",
                                            "origin": "200",
                                            "amount": 10
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    /**
     * Verifies that withdrawing from an existing account
     * updates and returns its balance.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldWithdrawFromExistingAccount() throws Exception {

        deposit("100", 20);

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "withdraw",
                                            "origin": "100",
                                            "amount": 5
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(15));
    }

    /**
     * Verifies that a transfer between accounts updates
     * both origin and destination balances.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldTransferBetweenAccounts() throws Exception {

        deposit("100", 15);

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "transfer",
                                            "origin": "100",
                                            "amount": 15,
                                            "destination": "300"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(0))
                .andExpect(jsonPath("$.destination.id").value("300"))
                .andExpect(jsonPath("$.destination.balance").value(15));
    }

    /**
     * Verifies that transferring from a non-existing account
     * returns HTTP 404 and body 0.
     *
     * @throws Exception if the HTTP request cannot be executed
     */
    @Test
    void shouldReturnNotFoundWhenTransferringFromNonExistingAccount()
            throws Exception {

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "transfer",
                                            "origin": "200",
                                            "amount": 15,
                                            "destination": "300"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    /**
     * Creates or updates an account through the deposit API.
     * <p>
     * This helper method performs the setup through the HTTP
     * endpoint instead of manipulating the service directly,
     * keeping the integration tests close to real API usage.
     * </p>
     *
     * @param accountId destination account identifier
     * @param amount    amount to deposit
     * @throws Exception if the HTTP request cannot be executed
     */
    private void deposit(String accountId, long amount) throws Exception {

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "type": "deposit",
                                            "destination": "%s",
                                            "amount": %d
                                        }
                                        """.formatted(accountId, amount))
                )
                .andExpect(status().isCreated());
    }
}