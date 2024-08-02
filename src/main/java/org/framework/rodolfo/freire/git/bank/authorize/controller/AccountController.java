package org.framework.rodolfo.freire.git.bank.authorize.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.framework.rodolfo.freire.git.bank.authorize.model.Account;
import org.framework.rodolfo.freire.git.bank.authorize.model.AccountType;
import org.framework.rodolfo.freire.git.bank.authorize.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing {@link Account} entities.
 * Provides RESTful endpoints for CRUD operations, as well as handling deposits, withdrawals, transfers, and balance movements.
 * Uses {@link AccountService} for interaction with the data layer.
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account", description = "Endpoints for CRUD operations, deposits, withdrawals, transfers, and balance movements.")
public class AccountController {

    private final AccountService accountService;

    /**
     * Constructor for initializing {@link AccountService}.
     *
     * @param accountService Service for managing account operations.
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Retrieves all {@link Account} entities.
     *
     * @return A list of all accounts.
     */
    @Hidden
    @GetMapping
    @Operation(summary = "Retrieve all accounts", description = "Fetches a list of all account entities.")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> accounts = accountService.getAll();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Retrieves an {@link Account} by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account entity if found, otherwise a 404 status.
     */
    @Hidden
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve account by ID", description = "Fetches an account entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        return accountService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new {@link Account}.
     *
     * @param account The account data to create.
     * @return The created account with status 201.
     */
    @Hidden
    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new account entity.")
    @ApiResponse(responseCode = "201", description = "Account created")
    @ApiResponse(responseCode = "400", description = "Invalid account data")
    public ResponseEntity<Account> save(@Valid @RequestBody Account account) {
        if (account.getBalances() == null || account.getBalances().isEmpty() ||
                account.getBalances().stream().anyMatch(balance -> balance.getBalanceAmount() <= 0)) {
            return ResponseEntity.badRequest().build();
        }

        Account savedAccount = accountService.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    /**
     * Updates an existing {@link Account} specified by its ID.
     *
     * @param id      The ID of the account to update.
     * @param account The account data with updates.
     * @return The updated account.
     */
    @Hidden
    @PutMapping("/{id}")
    @Operation(summary = "Update an account", description = "Updates an existing account entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Account updated")
    @ApiResponse(responseCode = "400", description = "Invalid account data")
    public ResponseEntity<Account> update(@PathVariable Long id, @Valid @RequestBody Account account) {
        account.setAccountId(id);
        Account updatedAccount = accountService.save(account);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Deletes an {@link Account} specified by its ID.
     *
     * @param accountId The ID of the account to delete.
     * @return A response indicating the deletion status.
     */
    @Hidden
    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete an account", description = "Deletes an account entity by its ID.")
    @ApiResponse(responseCode = "204", description = "Account deleted")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public ResponseEntity<Void> delete(@PathVariable Long accountId) {
        boolean deleted = accountService.delete(accountId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deposits a specified amount into the account.
     *
     * @param accountId The ID of the account where the deposit will be made.
     * @param amount    The amount to be deposited.
     * @param type      The type of account (e.g., FOOD, MEAL, CASH).
     * @return A response indicating whether the deposit was successful.
     */
    @PostMapping("/{accountId}/deposit")
    @Operation(summary = "Deposit into an account", description = "Deposits a specified amount into the given account.")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "400", description = "Invalid deposit amount or type")
    public ResponseEntity<String> deposit(@PathVariable Long accountId,
                                          @RequestParam Double amount,
                                          @RequestParam AccountType type) {
        if (amount <= 0 || type == null) {
            return ResponseEntity.badRequest().body("Invalid amount or account type.");
        }

        boolean success = accountService.deposit(accountId, amount, type);
        return success ? ResponseEntity.ok("Deposit successful") : ResponseEntity.badRequest().body("Deposit failed");
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param id     The ID of the account from which the withdrawal will be made.
     * @param amount The amount to be withdrawn.
     * @param type   The type of account (e.g., FOOD, MEAL, CASH).
     * @return A response indicating whether the withdrawal was successful.
     */
    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw from an account", description = "Withdraws a specified amount from the given account.")
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @ApiResponse(responseCode = "400", description = "Insufficient balance or invalid account type")
    public ResponseEntity<String> withdraw(@PathVariable Long id,
                                           @RequestParam Double amount,
                                           @RequestParam AccountType type) {
        boolean success = accountService.withdraw(id, amount, type);
        return success ? ResponseEntity.ok("Withdrawal successful") : ResponseEntity.badRequest().body("Withdrawal failed");
    }

    /**
     * Transfers a specified amount between two accounts.
     *
     * @param fromAccountId The ID of the source account.
     * @param toAccountId   The ID of the target account.
     * @param amount        The amount to be transferred.
     * @param type          The type of account (e.g., FOOD, MEAL, CASH).
     * @return A response indicating whether the transfer was successful.
     */
    @PostMapping("/transfer")
    @Operation(summary = "Transfer between accounts", description = "Transfers a specified amount between two accounts.")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @ApiResponse(responseCode = "400", description = "Invalid transfer details")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId,
                                           @RequestParam Long toAccountId,
                                           @RequestParam Double amount,
                                           @RequestParam AccountType type) {
        boolean success = accountService.transfer(fromAccountId, toAccountId, amount, type);
        return success ? ResponseEntity.ok("Transfer successful") : ResponseEntity.badRequest().body("Transfer failed");
    }

    /**
     * Moves balance between different types within the same account.
     *
     * @param id       The ID of the account.
     * @param amount   The amount to be moved.
     * @param fromType The source account type.
     * @param toType   The target account type.
     * @return A response indicating whether the balance move was successful.
     */
    @PostMapping("/{id}/moveBalance")
    @Operation(summary = "Move balance within an account", description = "Moves balance between different types within the same account.")
    @ApiResponse(responseCode = "200", description = "Balance moved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid balance move details")
    public ResponseEntity<String> moveBalance(@PathVariable Long id,
                                              @RequestParam Double amount,
                                              @RequestParam AccountType fromType,
                                              @RequestParam AccountType toType) {
        boolean success = accountService.moveBalance(id, amount, fromType, toType);
        return success ? ResponseEntity.ok("Balance moved successfully") : ResponseEntity.badRequest().body("Balance move failed");
    }

    /**
     * Checks if there is sufficient balance in the specified account type.
     * If not, it checks the CASH balance.
     *
     * @param id     The ID of the account.
     * @param amount The amount to check.
     * @param type   The type of account to check.
     * @return {@code true} if there is sufficient balance; {@code false} otherwise.
     */
    @Hidden
    @GetMapping("/{id}/has-sufficient-balance")
    @Operation(summary = "Check sufficient balance", description = "Checks if there is sufficient balance in the specified account type.")
    @ApiResponse(responseCode = "200", description = "Balance check successful")
    public ResponseEntity<Boolean> hasSufficientBalance(@PathVariable Long id,
                                                        @RequestParam Double amount,
                                                        @RequestParam AccountType type) {
        boolean hasSufficientBalance = accountService.hasSufficientBalance(id, amount, type);
        return ResponseEntity.ok(hasSufficientBalance);
    }
}
