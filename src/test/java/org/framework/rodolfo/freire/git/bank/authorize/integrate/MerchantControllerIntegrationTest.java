package org.framework.rodolfo.freire.git.bank.authorize.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.framework.rodolfo.freire.git.bank.authorize.controller.MerchantController;
import org.framework.rodolfo.freire.git.bank.authorize.model.Merchant;
import org.framework.rodolfo.freire.git.bank.authorize.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link MerchantController}.
 * <p>
 * This class contains integration tests for the {@code MerchantController}, focusing on merchant retrieval and creation operations.
 * The tests use MockMvc to perform HTTP requests and validate responses to ensure that the controller behaves as expected.
 * </p>
 */
@WebMvcTest(MerchantController.class)
public class MerchantControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MerchantService merchantService;

    /**
     * Tests the retrieval of an existing merchant.
     * <p>
     * This test verifies that a merchant with a specific ID can be successfully retrieved. It mocks the merchant service to return
     * a predefined merchant object and then checks that the response status is OK and the response body contains the correct
     * merchant details, including the merchant ID and name.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetMerchant() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("Merchant A");
        given(merchantService.getById(1L)).willReturn(Optional.of(merchant));

        mockMvc.perform(get("/api/merchants/{merchantId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchant_id", is(1)))
                .andExpect(jsonPath("$.name", is("Merchant A")));
    }

    /**
     * Tests the retrieval of a merchant that does not exist.
     * <p>
     * This test verifies that attempting to retrieve a non-existent merchant results in a "404 Not Found" status. It mocks
     * the merchant service to return an empty optional and then checks that the response status is "Not Found".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetMerchant_NotFound() throws Exception {
        given(merchantService.getById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/merchants/{merchantId}", 1L))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests saving a merchant with invalid data.
     * <p>
     * This test verifies that attempting to save a merchant with invalid data results in a "400 Bad Request" status. It mocks
     * the POST request with a merchant object that has an invalid name and expects the response status to indicate a bad request.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testSaveMerchant_BadRequest() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(null);
        merchant.setMerchantName(""); // nome inválido

        mockMvc.perform(post("/api/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchant)))
                .andExpect(status().isBadRequest());
    }
}
