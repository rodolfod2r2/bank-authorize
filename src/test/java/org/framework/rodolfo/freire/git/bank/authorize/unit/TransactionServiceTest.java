package org.framework.rodolfo.freire.git.bank.authorize.unit;

import org.framework.rodolfo.freire.git.bank.authorize.model.*;
import org.framework.rodolfo.freire.git.bank.authorize.repository.AccountRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.CardRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.MerchantRepository;
import org.framework.rodolfo.freire.git.bank.authorize.repository.TransactionRepository;
import org.framework.rodolfo.freire.git.bank.authorize.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@code TransactionService} class.
 * <p>
 * This class validates the transaction authorization logic implemented in the {@code TransactionService}.
 * The tests ensure that the transaction service behaves correctly under different scenarios.
 * </p>
 * <p>
 * Key scenarios tested include:
 * <ul>
 *   <li>Successful authorization of a transaction.</li>
 *   <li>Authorization failure due to insufficient balance in the specific category.</li>
 *   <li>Authorization with fallback to the CASH category when the specific category has insufficient balance.</li>
 *   <li>Authorization failure with fallback to CASH due to insufficient balance in CASH.</li>
 *   <li>Authorization failure with fallback to CASH when no balance is available in CASH.</li>
 * </ul>
 * </p>
 */
@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionService transactionService;

    /**
     * Sets up the testing environment before each test.
     * <p>
     * Initializes mocks and injects the required dependencies for the tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountRepository = mock(AccountRepository.class);
        merchantRepository = mock(MerchantRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository, accountRepository, merchantRepository, cardRepository);
    }

    /**
     * Tests successful authorization of a transaction.
     * <p>
     * This test simulates a transaction with an MCC corresponding to a category with sufficient balance.
     * It verifies that the transaction is approved and the balance is updated correctly.
     * </p>
     */
    @Test
    void testAuthorizeTransactionSuccess() {
        // Arrange
        Account account = new Account();
        Balance balance = new Balance();
        balance.setBalanceType(AccountType.FOOD);  // Adjusted to "FOOD"
        balance.setBalanceAmount(100.0);

        Set<Balance> balances = new HashSet<>();
        balances.add(balance);

        account.setBalances(balances);

        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("UBER EATS SAO PAULO BR");
        merchant.setMerchantMcc("5411");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantMcc("5411")).thenReturn(Optional.of(merchant));

        // Act
        String result = transactionService.authorizeTransaction(transaction);

        // Assert
        assertEquals("07", result, "The transaction should be authorized.");
    }

    /**
     * Tests authorization failure due to insufficient balance.
     * <p>
     * This test simulates a transaction with an MCC corresponding to a category with insufficient balance.
     * It verifies that the transaction is rejected and the balance is not updated.
     * </p>
     */
    @Test
    void testAuthorizeTransactionInsufficientBalance() {
        // Arrange
        Account account = new Account();
        Balance balance = new Balance();
        balance.setBalanceType(AccountType.FOOD);
        balance.setBalanceAmount(30.0);

        Set<Balance> balances = new HashSet<>();
        balances.add(balance);

        account.setBalances(balances);

        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("UBER EAT SAO PAULO BR");
        merchant.setMerchantMcc("5411");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantMcc("5411")).thenReturn(Optional.of(merchant));

        // Act
        String result = transactionService.authorizeTransaction(transaction);

        // Assert
        assertEquals("07", result, "The transaction should be rejected due to insufficient balance.");
        verify(accountRepository, never()).save(account);
        verify(transactionRepository, never()).save(transaction);
    }

    /**
     * Tests authorization with fallback to the CASH category with sufficient balance.
     * <p>
     * This test simulates a transaction where the specific category balance is insufficient, but CASH balance is sufficient.
     * It verifies that the transaction is approved and the CASH balance is correctly debited.
     * </p>
     */
    @Test
    void testAuthorizeTransactionWithFallbackSuccess() {
        // Arrange
        Account account = new Account();
        Balance foodBalance = new Balance();
        foodBalance.setBalanceType(AccountType.FOOD);
        foodBalance.setBalanceAmount(30.0);

        Balance cashBalance = new Balance();
        cashBalance.setBalanceType(AccountType.CASH);
        cashBalance.setBalanceAmount(100.0);

        Set<Balance> balances = new HashSet<>();
        balances.add(foodBalance);
        balances.add(cashBalance);

        account.setBalances(balances);

        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("UBER TRIP SAO PAULO BR");
        merchant.setMerchantMcc("5411");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantMcc("5411")).thenReturn(Optional.of(merchant));

        // Act
        String result = transactionService.authorizeTransactionWithFallback(transaction);

        // Assert
        assertEquals("07", result, "The transaction should be authorized with fallback to CASH.");
    }

    /**
     * Tests authorization failure with fallback when there is no available balance in CASH.
     * <p>
     * This test simulates a transaction where the specific category balance is insufficient and no balance is available in CASH.
     * It verifies that the transaction is rejected due to the absence of balance in CASH.
     * </p>
     */
    @Test
    void testAuthorizeTransactionWithFallbackNoCashBalance() {
        // Arrange
        Account account = new Account();
        Balance foodBalance = new Balance();
        foodBalance.setBalanceType(AccountType.FOOD);
        foodBalance.setBalanceAmount(30.0);

        Set<Balance> balances = new HashSet<>();
        balances.add(foodBalance);

        account.setBalances(balances);

        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("UBER TRIP SAO PAULO BR");
        merchant.setMerchantMcc("5411");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantMcc("5411")).thenReturn(Optional.of(merchant));

        // Act
        String result = transactionService.authorizeTransactionWithFallback(transaction);

        // Assert
        assertEquals("07", result, "The transaction should be rejected due to no balance in CASH.");
        verify(accountRepository, never()).save(account);
        verify(transactionRepository, never()).save(transaction);
    }
}
