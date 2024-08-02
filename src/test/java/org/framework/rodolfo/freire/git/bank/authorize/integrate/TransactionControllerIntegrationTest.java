package org.framework.rodolfo.freire.git.bank.authorize.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.framework.rodolfo.freire.git.bank.authorize.controller.TransactionController;
import org.framework.rodolfo.freire.git.bank.authorize.model.Transaction;
import org.framework.rodolfo.freire.git.bank.authorize.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link TransactionController}.
 * <p>
 * This class contains integration tests for the {@code TransactionController}, verifying its behavior with various scenarios
 * related to transaction authorization and handling fallback mechanisms. The tests use MockMvc to perform HTTP requests
 * and validate responses against expected outcomes.
 * </p>
 */
@WebMvcTest(TransactionController.class)
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * Tests the successful authorization of a transaction.
     * <p>
     * This test verifies that a transaction is successfully authorized when the service returns an "00" code for successful
     * authorization. It sends a POST request to the /api/transactions/authorize endpoint with a transaction payload and checks
     * that the response status is OK and the response code is "00".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testAuthorizeTransactionSuccess() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        when(transactionService.authorizeTransaction(any())).thenReturn("00");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("00"));
    }

    /**
     * Tests the successful authorization of a transaction with fallback.
     * <p>
     * This test verifies that a transaction is successfully authorized with fallback when the service returns an "00" code
     * for successful authorization with fallback. It sends a POST request to the /api/transactions/authorize-with-fallback
     * endpoint with a transaction payload and checks that the response status is OK and the response code is "00".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testAuthorizeTransactionWithFallbackSuccess() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        when(transactionService.authorizeTransactionWithFallback(any())).thenReturn("00");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/authorize-with-fallback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("00"));
    }

    /**
     * Tests the authorization of a transaction with insufficient balance.
     * <p>
     * This test verifies that the service handles insufficient balance scenarios correctly by returning a "51" code. It
     * sends a POST request to the /api/transactions/authorize endpoint with a transaction payload and checks that the
     * response status is OK and the response code is "51".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testAuthorizeTransactionInsufficientBalance() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        when(transactionService.authorizeTransaction(any())).thenReturn("51");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("51"));
    }

    /**
     * Tests the authorization of a transaction with fallback when there is insufficient cash.
     * <p>
     * This test verifies that the fallback mechanism handles cases where cash balance is insufficient by returning a "51"
     * code. It sends a POST request to the /api/transactions/authorize-with-fallback endpoint with a transaction payload and
     * checks that the response status is OK and the response code is "51".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testAuthorizeTransactionWithFallbackInsufficientCash() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        when(transactionService.authorizeTransactionWithFallback(any())).thenReturn("51");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/authorize-with-fallback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("51"));
    }

    /**
     * Tests the authorization of a transaction with fallback when no cash balance is found.
     * <p>
     * This test verifies that the fallback mechanism handles cases where no cash balance is available by returning a "07"
     * code. It sends a POST request to the /api/transactions/authorize-with-fallback endpoint with a transaction payload and
     * checks that the response status is OK and the response code is "07".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testAuthorizeTransactionWithFallbackNoCashBalance() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transaction.setTransactionAmount(50.0);
        transaction.setTransactionMcc("5411");

        when(transactionService.authorizeTransactionWithFallback(any())).thenReturn("07");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions/authorize-with-fallback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("07"));
    }

    /**
     * Tests the retrieval of a transaction that does not exist.
     * <p>
     * This test verifies that a "404 Not Found" status is returned when trying to retrieve a transaction that does not exist.
     * It sends a GET request to the /api/transactions/{transactionId} endpoint with a non-existent transaction ID and checks
     * that the response status is "Not Found".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetTransaction_NotFound() throws Exception {
        //given(transactionService.getTransaction(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/{transactionId}", 1L))
                .andExpect(status().isNotFound());
    }
}
