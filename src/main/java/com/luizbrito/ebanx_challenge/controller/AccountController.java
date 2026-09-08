package com.luizbrito.ebanx_challenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizbrito.ebanx_challenge.dto.DepositResponse;
import com.luizbrito.ebanx_challenge.dto.EventRequest;
import com.luizbrito.ebanx_challenge.dto.WithdrawResponse;
import com.luizbrito.ebanx_challenge.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller responsible for exposing account operations through HTTP.
 * <p>
 * Provides endpoints for resetting the application state, retrieving account
 * balances, and processing deposit, withdraw, and transfer events.
 * </p>
 *
 * The controller is responsible only for the HTTP transport layer.
 * Business rules are delegated to {@link AccountService}.
 *
 * Created for the EBANX Software Engineer Take-home Challenge.
 *
 * @since 2026-09-07
 * @version 1.0
 * @author Luiz Brito da Rosa
 */
@RestController
@Tag(name = "Accounts", description = "Account operations for the EBANX Challenge")
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates the account controller.
     *
     * @param accountService service responsible for account operations
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Resets the application state by removing all accounts from memory.
     *
     * @return HTTP 200 when the reset is completed
     */
    @Operation(summary = "Reset application state")
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {

        accountService.reset();

        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the current balance of an account.
     *
     * @param accountId identifier of the account
     * @return HTTP 200 with the current balance if the account exists,
     *         otherwise HTTP 404 with body 0
     */
    @Operation(summary = "Get account balance")
    @GetMapping("/balance")
    public ResponseEntity<Long> getBalance(
            @RequestParam("account_id") String accountId) {

        return accountService
                .getBalance(accountId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(0L)
                );
    }

    /**
     * Processes an account event.
     *
     * Supported event types are:
     * <ul>
     *     <li>DEPOSIT</li>
     *     <li>WITHDRAW</li>
     *     <li>TRANSFER</li>
     * </ul>
     *
     * @param event event received from the client
     * @return HTTP response according to the event result
     */
    @Operation(summary = "Process an account event")
    @PostMapping("/event")
    public ResponseEntity<?> processEvent(
            @RequestBody EventRequest event) {

        return switch (event.getType()) {

            case DEPOSIT -> processDeposit(event);

            case WITHDRAW -> processWithdraw(event);

            case TRANSFER -> processTransfer(event);
        };
    }

    /**
     * Processes a deposit event.
     *
     * @param event deposit event
     * @return HTTP 201 with the destination account
     */
    private ResponseEntity<DepositResponse> processDeposit(
            EventRequest event) {

        var account = accountService.deposit(
                event.getDestination(),
                event.getAmount()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DepositResponse(account));
    }

    /**
     * Processes a withdraw event.
     *
     * @param event withdraw event
     * @return HTTP 201 with the origin account if it exists,
     *         otherwise HTTP 404 with body 0
     */
    private ResponseEntity<?> processWithdraw(
            EventRequest event) {

        return accountService
                .withdraw(
                        event.getOrigin(),
                        event.getAmount()
                )
                .<ResponseEntity<?>>map(account ->
                        ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new WithdrawResponse(account))
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(0)
                );
    }

    /**
     * Processes a transfer event.
     *
     * @param event transfer event
     * @return HTTP 201 with the origin and destination accounts if the
     *         origin exists, otherwise HTTP 404 with body 0
     */
    private ResponseEntity<?> processTransfer(
            EventRequest event) {

        return accountService
                .transfer(
                        event.getOrigin(),
                        event.getDestination(),
                        event.getAmount()
                )
                .<ResponseEntity<?>>map(transfer ->
                        ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(transfer)
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(0)
                );
    }
}