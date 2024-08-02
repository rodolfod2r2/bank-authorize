package org.framework.rodolfo.freire.git.bank.authorize.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.framework.rodolfo.freire.git.bank.authorize.model.Transaction;
import org.framework.rodolfo.freire.git.bank.authorize.response.TransactionResponse;
import org.framework.rodolfo.freire.git.bank.authorize.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling transaction-related operations.
 * <p>
 * The {@code TransactionController} class manages incoming HTTP requests related to transactions.
 * It uses the {@code TransactionService} to perform authorization and processing of transactions.
 * The controller exposes endpoints for authorizing transactions and handling fallback scenarios when necessary.
 * </p>
 * <p>
 * This controller provides the following endpoints:
 * <ul>
 *   <li>{@code POST /transactions/authorize} - Authorizes a transaction using the MCC to map the benefit category.</li>
 *   <li>{@code POST /transactions/authorize-with-fallback} - Authorizes a transaction with fallback to the CASH category if the MCC cannot be mapped or if the balance of the mapped category is insufficient.</li>
 * </ul>
 * </p>
 *
 * @see TransactionService
 * @see Transaction
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Exposes endpoints for authorizing transactions and handling fallback scenarios when necessary.")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructs a {@code TransactionController} with the specified {@code TransactionService}.
     *
     * @param transactionService The service used to handle transaction operations.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Authorizes a transaction using the MCC to map the benefit category.
     * <p>
     * This endpoint processes the transaction by checking the balance in the specified benefit category
     * based on the MCC (Merchant Category Code). If the balance is sufficient, the transaction is approved
     * and the balance is decreased. The result of the authorization is returned as a message.
     * </p>
     *
     * @param transaction The transaction to be authorized. The request body should contain transaction details including MCC, amount, and account ID.
     * @return A message indicating the result of the authorization. Possible responses include success or failure reasons.
     */
    @PostMapping("/authorize")
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody Transaction transaction) {
        String result = transactionService.authorizeTransaction(transaction);
        return ResponseEntity.ok(new TransactionResponse(result));
    }

    /**
     * Authorizes a transaction with fallback to the CASH category if the MCC cannot be mapped
     * or if the balance of the mapped category is not sufficient.
     * <p>
     * This endpoint first attempts to authorize the transaction using the benefit category mapped by the MCC.
     * If the MCC cannot be mapped or the balance in the category is insufficient, the transaction is authorized
     * using the CASH category as a fallback.
     * </p>
     *
     * @param transaction The transaction to be authorized. The request body should contain transaction details including MCC, amount, and account ID.
     * @return A message indicating the result of the authorization. Possible responses include success or failure reasons, including fallback to CASH.
     */
    @PostMapping("/authorize-with-fallback")
    public ResponseEntity<TransactionResponse> authorizeTransactionWithFallback(@RequestBody Transaction transaction) {
        String result = transactionService.authorizeTransactionWithFallback(transaction);
        return ResponseEntity.ok(new TransactionResponse(result));
    }

}


