package org.framework.rodolfo.freire.git.bank.authorize.service;

import lombok.extern.slf4j.Slf4j;
import org.framework.rodolfo.freire.git.bank.authorize.model.*;
import org.framework.rodolfo.freire.git.bank.authorize.repository.AccountRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.CardRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.MerchantRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Service class responsible for authorizing and processing transactions.
 * <p>
 * The {@code TransactionService} class provides methods for authorizing transactions based on the Merchant Category Code (MCC)
 * and processing transactions by debiting the appropriate account balance. It interacts with {@code TransactionRepository},
 * {@code AccountRepository}, and {@code MerchantRepository} to perform these operations.
 * </p>
 * <p>
 * The service class is annotated with {@code @Service}, indicating that it is a Spring service component managed by the Spring container.
 * </p>
 *
 * @see Transaction
 * @see TransactionRepository
 * @see Account
 * @see AccountRepository
 * @see Merchant
 * @see MerchantRepository
 * @see CardRepository
 */

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MerchantRepository merchantRepository;
    private final CardRepository cardRepository;

    /**
     * Constructs a {@code TransactionService} with the specified repositories.
     * <p>
     * This constructor initializes the service with the repositories needed for performing transaction-related operations.
     * </p>
     *  @param transactionRepository The repository used to access transaction data.
     * @param accountRepository     The repository used to access account data.
     * @param merchantRepository    The repository used to access merchant data.
     * @param cardRepository   The repository used to access card data.
     */
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, MerchantRepository merchantRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.merchantRepository = merchantRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Authorizes a transaction using the Merchant Category Code (MCC) to map the benefit category.
     * <p>
     * If the balance of the mapped category is sufficient, the transaction is approved and the balance is decreased. Otherwise,
     * the transaction is denied.
     * </p>
     *
     * @param transaction The transaction to be authorized.
     * @return A message indicating the result of the authorization attempt.
     */
    public String authorizeTransaction(Transaction transaction) {
        AccountType category = determineCategory(transaction);
        return processTransaction(transaction, category);
    }

    /**
     * Authorizes a transaction with a fallback to the CASH category if the MCC cannot be mapped
     * or if the balance of the mapped category is not sufficient.
     * <p>
     * The fallback mechanism will attempt to authorize the transaction using the CASH category if the initial
     * authorization attempt fails.
     * </p>
     *
     * @param transaction The transaction to be authorized.
     * @return A message indicating the result of the authorization attempt.
     */
    public String authorizeTransactionWithFallback(Transaction transaction) {
        AccountType category = determineCategory(transaction);
        log.info("Category determined: {}", category);

        String result = processTransaction(transaction, category);
        if ("00".equals(result)) {
            return result;
        }

        log.info("Category {} authorization failed, attempting fallback to CASH", category);
        return fallbackToCash(transaction);
    }

    /**
     * Determines the transaction category based on the MCC code (Merchant Category Code) and the merchant name.
     * <p>
     * The method checks if the merchant name is available. If so, it determines the category based on
     * in the name of the merchant. If the merchant name is not available, the method attempts to determine the category
     * using the MCC code of the transaction. If both the MCC code and merchant name are null, the method
     * returns the default CASH category.
     * </p>
     *
     * @param transaction The transaction for which the category is to be determined. The transaction must contain the MCC code
     * and/or the name of the merchant so that the category can be identified.
     * @return The transaction category determined based on the MCC code and/or merchant name. If neither of the two
     * is available, returns the default CASH category.
     */
    private AccountType determineCategory(Transaction transaction) {
        if (transaction.getTransactionMcc() == null && transaction.getTransactionMerchant() == null) {
            log.info("Transaction MCC and Merchant are null, defaulting to CASH");
            return AccountType.CASH;
        }

        if (transaction.getTransactionMerchant() != null) {
            AccountType category = getCategoryByMerchantName(transaction.getTransactionMerchant());
            log.info("Merchant name indicates category: {}", category);
            return category;
        }

        return getCategoryByMcc(transaction.getTransactionMcc());
    }

    /**
     * Determines category based on merchant name.
     *
     * @param merchantName The name of the merchant.
     * @return The corresponding category.
     */
    private AccountType getCategoryByMerchantName(String merchantName) {
        if (merchantName.contains("EATS") || merchantName.contains("FOOD")) {
            return AccountType.FOOD;
        } else if (merchantName.contains("PAG")) {
            return AccountType.MEAL;
        } else {
            return AccountType.CASH;
        }
    }

    /**
     * Determines category based on MCC code.
     *
     * @param mcc The MCC code.
     * @return The corresponding category.
     */
    private AccountType getCategoryByMcc(String mcc) {
        if (mcc == null) {
            log.info("MCC is null, defaulting to CASH");
            return AccountType.CASH;
        }

        Optional<Merchant> merchantOptional = merchantRepository.findByMerchantMcc(mcc);
        if (merchantOptional.isPresent()) {
            Merchant merchant = merchantOptional.get();
            String merchantName = merchant.getMerchantName();

            if (merchantName != null) {
                log.info("Merchant found for MCC {}: {}", mcc, merchantName);
                return getCategoryByMerchantName(merchantName);
            } else {
                log.info("Merchant name is null for MCC {}, defaulting to CASH", mcc);
            }
        } else {
            log.info("No merchant found for MCC {}", mcc);
        }


        switch (mcc) {
            case "5411":
            case "5412":
                return AccountType.FOOD;
            case "5811":
            case "5812":
                return AccountType.MEAL;
            default:
                return AccountType.CASH;
        }
    }

    /**
     * Processes the transaction by debiting the balance of the given category.
     * <p>
     * The transaction amount is debited from the balance of the specified category in the account. The transaction is
     * saved if the balance is sufficient. Otherwise, an appropriate message is returned.
     * </p>
     *
     * @param transaction The transaction to be processed.
     * @param category    The benefit category to use for debiting the balance.
     * @return A message indicating the result of the transaction processing.
     */
    private String processTransaction(Transaction transaction, AccountType category) {
        log.info("Processing transaction ID: {} with category: {}", transaction.getTransactionId(), category);

        Optional<Card> cardOptional = cardRepository.getByCardNumber(transaction.getCardNumber());
        if (cardOptional.isEmpty()) {
            log.error("Card not found for card number: {}", transaction.getCardNumber());
            return "07";
        }

        Card card = cardOptional.get();
        Optional<Account> accountOptional = accountRepository.getByCardCardId(card.getCardId());
        if (accountOptional.isEmpty()) {
            log.error("Account not found for Card ID: {}", card.getCardId());
            return "07";
        }

        Account account = accountOptional.get();
        boolean categoryFound = false;

        for (Balance balance : account.getBalances()) {
            log.info("Checking balance type: {}, amount: {}", balance.getBalanceType(), balance.getBalanceAmount());
            if (balance.getBalanceType().equals(category)) {
                categoryFound = true;
                if (balance.getBalanceAmount() >= transaction.getTransactionAmount()) {
                    balance.setBalanceAmount(balance.getBalanceAmount() - transaction.getTransactionAmount());
                    accountRepository.save(account);

                    transaction.setTransactionDate(LocalDate.now());
                    transaction.setTransactionTime(LocalTime.now());
                    transactionRepository.save(transaction);

                    log.info("Transaction approved for category: {}", category);
                    return "00"; // Approved
                } else {
                    log.warn("Insufficient balance for category: {}, required: {}, available: {}", category, transaction.getTransactionAmount(), balance.getBalanceAmount());
                    return "51"; // Insufficient balance
                }
            }
        }

        if (!categoryFound) {
            log.info("Category {} not found, attempting fallback to CASH", category);
            return fallbackToCash(transaction);
        }

        return "07"; // Categoria não encontrada
    }


    /**
     * Fallback to CASH category if MCC cannot be mapped or if the balance of the mapped category is not sufficient.
     * <p>
     * The transaction amount is debited from the CASH balance if available. If the CASH balance is insufficient or not found,
     * an appropriate message is returned.
     * </p>
     *
     * @param transaction The transaction to be processed.
     * @return A message indicating the result of the fallback transaction processing.
     */
    private String fallbackToCash(Transaction transaction) {
        log.info("Fallback to CASH for transaction ID: {}", transaction.getTransactionId());

        Optional<Account> accountOptional = accountRepository.findById(transaction.getAccountId());
        if (accountOptional.isEmpty()) {
            log.error("Account not found for ID: {}", transaction.getAccountId());
            return "07"; // Account not found
        }

        Account account = accountOptional.get();
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals(AccountType.CASH)) {
                if (balance.getBalanceAmount() >= transaction.getTransactionAmount()) {
                    balance.setBalanceAmount(balance.getBalanceAmount() - transaction.getTransactionAmount());
                    accountRepository.save(account);

                    transaction.setTransactionDate(LocalDate.now());
                    transaction.setTransactionTime(LocalTime.now());
                    transactionRepository.save(transaction);

                    log.info("Transaction approved using CASH fallback");
                    return "00"; // Approved using CASH fallback
                } else {
                    log.warn("Insufficient CASH balance, required: {}, available: {}", transaction.getTransactionAmount(), balance.getBalanceAmount());
                    return "51"; // Insufficient CASH balance
                }
            }
        }

        log.warn("CASH balance not found");
        return "07"; // CASH balance not found
    }

}

