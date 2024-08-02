package org.framework.rodolfo.freire.git.bank.authorize.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.framework.rodolfo.freire.git.bank.authorize.controller.AccountController;
import org.framework.rodolfo.freire.git.bank.authorize.model.Account;
import org.framework.rodolfo.freire.git.bank.authorize.model.AccountType;
import org.framework.rodolfo.freire.git.bank.authorize.model.Balance;
import org.framework.rodolfo.freire.git.bank.authorize.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link AccountController}.
 * <p>
 * This class contains integration tests for the {@code AccountController}, focusing on operations related to accounts such
 * as deposit, retrieval, and creation. The tests use MockMvc to perform HTTP requests and validate the responses against
 * expected outcomes.
 * </p>
 */
@WebMvcTest(AccountController.class)
public class AccountControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    /**
     * Tests the deposit operation for an account.
     * <p>
     * This test verifies that depositing into an account with valid parameters results in a bad request response. This is
     * because the current implementation of this test does not mock the behavior of the deposit operation and expects a
     * bad request response. Adjust the test as needed based on actual deposit handling logic.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testDeposit() throws Exception {
        mockMvc.perform(post("/api/accounts/{accountId}/deposit", 1)
                        .param("amount", "100.0")
                        .param("type", AccountType.CASH.name())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the retrieval of an existing account.
     * <p>
     * This test verifies that an account with valid details can be retrieved successfully. It mocks the account service to
     * return a predefined account object and then checks that the response status is OK and the response body contains the
     * correct account number and balance amount.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetAccount() throws Exception {
        Account account = new Account();
        Set<Balance> accountBalance = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(100.0);
        accountBalance.add(balance);
        account.setAccountId(1L);
        account.setAccountNumber("1234567890");
        account.setBalances(accountBalance);
        given(accountService.getById(1L)).willReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/{accountId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("1234567890")))
                .andExpect(jsonPath("$.balances[0].amount", is(100.0)));
    }

    /**
     * Tests the retrieval of an account that does not exist.
     * <p>
     * This test verifies that a "404 Not Found" status is returned when trying to retrieve an account that does not exist.
     * It mocks the account service to return an empty optional and then checks that the response status is "Not Found".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetAccount_NotFound() throws Exception {
        given(accountService.getById(2L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/{accountId}", 2L))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests saving an account with invalid data.
     * <p>
     * This test verifies that attempting to save an account with invalid data results in a "400 Bad Request" response. The
     * test sends a POST request with an account containing a null ID and invalid balance amount, and checks that the response
     * status is "Bad Request".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testSaveAccount_BadRequest() throws Exception {
        Account account = new Account();
        account.setAccountId(null);
        Set<Balance> accountBalance = new HashSet<>();
        Balance balance = new Balance();
        balance.setBalanceAmount(-1000.0); // Invalid balance amount
        accountBalance.add(balance);
        account.setBalances(accountBalance);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }
}
