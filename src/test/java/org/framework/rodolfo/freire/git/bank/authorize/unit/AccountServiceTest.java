package org.framework.rodolfo.freire.git.bank.authorize.unit;

import org.framework.rodolfo.freire.git.bank.authorize.model.Account;
import org.framework.rodolfo.freire.git.bank.authorize.model.AccountType;
import org.framework.rodolfo.freire.git.bank.authorize.model.Balance;
import org.framework.rodolfo.freire.git.bank.authorize.repository.AccountRepository;
import org.framework.rodolfo.freire.git.bank.authorize.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AccountService} class.
 * <p>
 * This class tests various scenarios for the deposit functionality in the {@link AccountService}:
 * - Successful deposit into an existing account.
 * - Attempt to deposit into a non-existing account.
 * - Attempt to deposit a negative amount.
 * - Deposit into an account with an initial zero balance.
 * - Multiple deposit transactions.
 * </p>
 */
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    /**
     * Initializes mocks before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests a successful deposit into an existing account.
     * <p>
     * This test verifies that depositing a positive amount into an existing account updates
     * the account balance correctly and saves the updated account to the repository.
     * </p>
     */
    @Test
    void testDepositSuccess() {
        Account account = new Account();
        account.setAccountId(1L);
        Set<Balance> accountBalances = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(100.0);
        balance.setBalanceType(AccountType.CASH);
        accountBalances.add(balance);
        account.setBalances(accountBalances);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        boolean result = accountService.deposit(1L, 50.0, AccountType.CASH);

        assertTrue(result);
        assertEquals(150.0, account.getBalances().iterator().next().getBalanceAmount(), 0.01);
        verify(accountRepository).save(account);
    }

    /**
     * Tests the deposit operation when the account does not exist.
     * <p>
     * This test verifies that attempting to deposit into a non-existing account returns
     * false and does not attempt to save the account.
     * </p>
     */
    @Test
    void testDepositFailure() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = accountService.deposit(1L, 50.0, AccountType.CASH);

        assertFalse(result);
        verify(accountRepository, never()).save(any(Account.class));
    }

    /**
     * Tests the deposit operation with a negative amount.
     * <p>
     * This test verifies that attempting to deposit a negative amount returns false
     * and does not attempt to save the account.
     * </p>
     */
    @Test
    void testDepositNegativeAmount() {
        Account account = new Account();
        account.setAccountId(1L);
        Set<Balance> accountBalances = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(100.0);
        balance.setBalanceType(AccountType.CASH);
        accountBalances.add(balance);
        account.setBalances(accountBalances);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        boolean result = accountService.deposit(1L, -50.0, AccountType.CASH);

        assertFalse(result);
        verify(accountRepository, never()).save(any(Account.class));
    }

    /**
     * Tests the deposit operation into an account with an initial zero balance.
     * <p>
     * This test verifies that depositing a positive amount into an account with a zero
     * balance updates the balance correctly and saves the updated account to the repository.
     * </p>
     */
    @Test
    void testDepositZeroInitialBalance() {
        Account account = new Account();
        account.setAccountId(1L);
        Set<Balance> accountBalances = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(0.0);
        balance.setBalanceType(AccountType.CASH);
        accountBalances.add(balance);
        account.setBalances(accountBalances);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        boolean result = accountService.deposit(1L, 100.0, AccountType.CASH);

        assertTrue(result);
        assertEquals(100.0, account.getBalances().iterator().next().getBalanceAmount(), 0.01);
        verify(accountRepository).save(account);
    }

    /**
     * Tests multiple deposit transactions into an existing account.
     * <p>
     * This test verifies that performing multiple deposits into the same account
     * correctly updates the account balance and the repository save operation is
     * called the expected number of times.
     * </p>
     */
    @Test
    void testDepositMultipleTransactions() {
        Account account = new Account();
        account.setAccountId(1L);
        Set<Balance> accountBalances = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(100.0);
        balance.setBalanceType(AccountType.CASH);
        accountBalances.add(balance);
        account.setBalances(accountBalances);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.deposit(1L, 50.0, AccountType.CASH);
        accountService.deposit(1L, 30.0, AccountType.CASH);

        assertEquals(180.0, account.getBalances().iterator().next().getBalanceAmount(), 0.01);
        verify(accountRepository, times(2)).save(account);
    }
}