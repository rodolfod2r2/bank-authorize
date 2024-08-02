package org.framework.rodolfo.freire.git.bank.authorize.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.framework.rodolfo.freire.git.bank.authorize.controller.ClientController;
import org.framework.rodolfo.freire.git.bank.authorize.model.Customer;
import org.framework.rodolfo.freire.git.bank.authorize.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link ClientController}.
 * <p>
 * This class contains integration tests for the {@code ClientController}, focusing on client retrieval and creation operations.
 * The tests use MockMvc to perform HTTP requests and validate responses to ensure that the controller behaves as expected.
 * </p>
 */
@WebMvcTest(ClientController.class)
public class CustomerControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService customerService;

    /**
     * Tests the retrieval of an existing customer.
     * <p>
     * This test verifies that a customer with a specific ID can be successfully retrieved. It mocks the customer service to return
     * a predefined customer object and then checks that the response status is OK and the response body contains the correct
     * customer details, including the customer's name.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("John Doe");
        given(customerService.getById(1L)).willReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    /**
     * Tests the creation and saving of a new customer.
     * <p>
     * This test verifies that a new customer can be successfully created and saved. It mocks the customer service to return a
     * predefined customer object when saving. The test sends a POST request with a customer object and expects a successful
     * response with status 200 OK and the customer details in the response body.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testSaveCustomer() throws Exception {
        Customer validCustomer = new Customer();
        validCustomer.setCustomerId(1L);
        validCustomer.setCustomerName("Jane Doe");
        validCustomer.setCustomerEmail("jane.doe@example.com");

        when(customerService.save(any(Customer.class))).thenReturn(validCustomer);

        String jsonContent = objectMapper.writeValueAsString(validCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    /**
     * Tests the retrieval of a customer that does not exist.
     * <p>
     * This test verifies that attempting to retrieve a non-existent customer results in a "404 Not Found" status. It mocks
     * the customer service to return an empty optional and then checks that the response status is "Not Found".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetCustomer_NotFound() throws Exception {
        given(customerService.getById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/{customerId}", 1L))
                .andExpect(status().isNotFound());
    }
}
