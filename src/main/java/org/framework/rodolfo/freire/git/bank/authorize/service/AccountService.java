package org.framework.rodolfo.freire.git.bank.authorize.service;

import org.framework.rodolfo.freire.git.bank.authorize.model.Account;
import org.framework.rodolfo.freire.git.bank.authorize.model.AccountType;
import org.framework.rodolfo.freire.git.bank.authorize.model.Balance;
import org.framework.rodolfo.freire.git.bank.authorize.repository.AccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service responsible for account management.
 */

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Gets all accounts.
     *
     * @return List of all accounts.
     */
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    /**
     * Gets one account per ID.
     *
     * @param accountId The account ID.
     * @return The account with the specified ID.
     */
    public Optional<Account> getById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    /**
     * Gets one account per ID.
     *
     * @param cardId The account ID.
     * @return The account with the specified ID.
     */
    public Optional<Account> getByCardId(Long cardId) {
        return accountRepository.getByCardCardId(cardId);
    }

    /**
     * Create a new account.
     *
     * @param account The account to be created.
     * @return The created account.
     */
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Updates an existing account.
     *
     * @param accountId The ID of the account to be updated.
     * @param account   The account with the updates.
     * @return The updated account.
     */
    public Account update(Long accountId, Account account) {
        if (accountRepository.existsById(accountId)) {
            account.setAccountId(accountId);
            return accountRepository.save(account);
        }
        return null;
    }

    /**
     * Deletes an {@link Account} by its ID.
     *
     * @param accountId The ID of the account to delete.
     * @return True if the account was deleted, false if it was not found.
     */
    @Transactional
    public boolean delete(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            return false;
        }
        try {
            accountRepository.deleteById(accountId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    /**
     * Make a deposit into an account.
     *
     * @param accountId The account ID.
     * @param amount    The amount to be deposited.
     * @param type      The type of account (FOOD, MEAL, CASH).
     * @return Whether the deposit was successful.
     */
    public boolean deposit(Long accountId, Double amount, AccountType type) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if ((accountOptional.isEmpty()) || amount < 0) {
            return false;
        }
        Account account = accountOptional.get();
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals(type)) {
                balance.setBalanceAmount(balance.getBalanceAmount() + amount);
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    /**
     * Make a withdrawal from an account.
     *
     * @param accountId The account ID.
     * @param amount    The amount to be withdrawn.
     * @param type      The type of account (FOOD, MEAL, CASH).
     * @return Whether the withdrawal was successful.
     */
    public boolean withdraw(Long accountId, Double amount, AccountType type) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if ((accountOptional.isEmpty()) || amount < 0) {
            return false;
        }
        Account account = accountOptional.get();
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals(type)) {
                if (balance.getBalanceAmount() >= amount) {
                    balance.setBalanceAmount(balance.getBalanceAmount() - amount);
                    accountRepository.save(account);
                    return true;
                } else {
                    return fallbackToCash(account, amount);
                }
            }
        }
        return false;
    }

    /**
     * Move balance between accounts.
     *
     * @param fromAccountId The source account ID.
     * @param toAccountId   The target account ID.
     * @param amount        The amount to be transferred.
     * @param type          The target account type.
     * @return Whether the transfer was successful.
     */
    public boolean transfer(Long fromAccountId, Long toAccountId, Double amount, AccountType type) {
        Optional<Account> fromAccountOptional = accountRepository.findById(fromAccountId);
        Optional<Account> toAccountOptional = accountRepository.findById(toAccountId);
        if (fromAccountOptional.isEmpty() || toAccountOptional.isEmpty() || amount < 0) {
            return false;
        }
        Account fromAccount = fromAccountOptional.get();
        Account toAccount = toAccountOptional.get();
        for (Balance fromBalance : fromAccount.getBalances()) {
            if (fromBalance.getBalanceType().equals(type)) {
                if (fromBalance.getBalanceAmount() >= amount) {
                    fromBalance.setBalanceAmount(fromBalance.getBalanceAmount() - amount);
                    boolean depositSuccess = deposit(toAccountId, amount, type);
                    if (depositSuccess) {
                        accountRepository.save(fromAccount);
                        accountRepository.save(toAccount);
                        return true;
                    }
                } else {
                    return fallbackToCash(fromAccount, amount);
                }
            }
        }
        return false;
    }

    /**
     * Move balance between account types.
     *
     * @param accountId The ID of the originating account.
     * @param amount    The amount to be transferred.
     * @param fromType  The source account type.
     * @param toType    The target account type.
     * @return Whether the transfer was successful.
     */
    public boolean moveBalance(Long accountId, Double amount, AccountType fromType, AccountType toType) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty() || amount < 0) {
            return false;
        }
        Account account = accountOptional.get();
        boolean isWithdrawalSuccessful = withdraw(accountId, amount, fromType);
        if (isWithdrawalSuccessful) {
            return deposit(accountId, amount, toType);
        }
        return false;
    }

    private boolean fallbackToCash(Account account, Double amount) {
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals("CASH") && balance.getBalanceAmount() >= amount) {
                balance.setBalanceAmount(balance.getBalanceAmount() - amount);
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether there is sufficient balance in the specified account type.
     * If there is none, use the CASH balance.
     *
     * @param id     The account ID.
     * @param amount The value to check.
     * @param type   The type of account to check.
     * @return Whether there is sufficient balance.
     */
    public boolean hasSufficientBalance(Long id, Double amount, AccountType type) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty() || amount < 0) {
            return false;
        }
        Account account = accountOptional.get();
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals(type)) {
                if (balance.getBalanceAmount() >= amount) {
                    return true;
                }
            }
        }
        for (Balance balance : account.getBalances()) {
            if (balance.getBalanceType().equals("CASH")) {
                if (balance.getBalanceAmount() >= amount) {
                    return true;
                }
            }
        }
        return false;
    }
}
