package org.framework.rodolfo.freire.git.bank.authorize.unit;

import org.framework.rodolfo.freire.git.bank.authorize.model.Customer;
import org.framework.rodolfo.freire.git.bank.authorize.repository.ClientRepository;
import org.framework.rodolfo.freire.git.bank.authorize.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ClientService} class.
 * <p>
 * This class tests various scenarios for the customer management functionality in the {@link ClientService}:
 * - Successful retrieval of an existing customer.
 * - Successful saving of a customer.
 * - Retrieval of a non-existing customer.
 * - Saving a customer with an existing ID.
 * - Behavior when attempting to save a null customer.
 * - Behavior when attempting to retrieve a customer with a null ID.
 * - Updating an existing customer.
 * - Deleting an existing customer.
 * - Attempting to delete a non-existing customer.
 * </p>
 */
public class CustomerServiceTest {

    @InjectMocks
    private ClientService customerService;

    @Mock
    private ClientRepository customerRepository;

    /**
     * Initializes mocks before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests successful retrieval of an existing customer by its ID.
     * <p>
     * This test verifies that retrieving a customer by its ID returns the correct customer and its name.
     * </p>
     */
    @Test
    void testGetCustomerSuccess() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("John Doe");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getById(1L);

        assertTrue(result.isPresent(), "The customer should be present.");
        assertEquals("John Doe", result.get().getCustomerName(), "The customer name is incorrect.");
    }

    /**
     * Tests successful saving of a customer.
     * <p>
     * This test verifies that saving a customer returns the saved customer with the correct name, and that
     * the customerRepository's save method is called.
     * </p>
     */
    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Jane Doe");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertEquals("Jane Doe", result.getCustomerName(), "The saved customer name is incorrect.");
        verify(customerRepository).save(customer);
    }

    /**
     * Tests retrieval of a customer that does not exist.
     * <p>
     * This test verifies that retrieving a customer by a non-existing ID returns an empty result.
     * </p>
     */
    @Test
    void testGetCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getById(1L);

        assertFalse(result.isPresent(), "The customer should not be present.");
    }

    /**
     * Tests saving a customer with an existing ID.
     * <p>
     * This test verifies that saving a customer with an ID that already exists updates the customer correctly,
     * and that the customerRepository's save method is called with the new customer details.
     * </p>
     */
    @Test
    void testSaveCustomerWithExistingId() {
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setCustomerName("John Doe");

        Customer newCustomer = new Customer();
        newCustomer.setCustomerId(1L);
        newCustomer.setCustomerName("Jane Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        Customer result = customerService.save(newCustomer);

        assertEquals("Jane Doe", result.getCustomerName(), "The saved customer name is incorrect.");
        assertEquals(1L, result.getCustomerId(), "The saved customer ID is incorrect.");
        verify(customerRepository).save(newCustomer);
    }

    /**
     * Tests the behavior when attempting to save a null customer.
     * <p>
     * This test verifies that attempting to save a null customer returns null and does not call the
     * customerRepository's save method.
     * </p>
     */
    @Test
    void testSaveNullCustomer() {
        Customer result = customerService.save(null);

        assertNull(result, "The result of saving a null customer should be null.");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    /**
     * Tests the behavior when attempting to retrieve a customer with a null ID.
     * <p>
     * This test verifies that retrieving a customer with a null ID returns an empty result.
     * </p>
     */
    @Test
    void testGetCustomerWithNullId() {
        when(customerRepository.findById(null)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getById(null);

        assertFalse(result.isPresent(), "Retrieving a customer with a null ID should return empty.");
    }

    /**
     * Tests updating an existing customer.
     * <p>
     * This test verifies that updating a customer correctly updates the customer's details and that the
     * customerRepository's save method is called with the updated customer.
     * </p>
     */
    @Test
    void testUpdateCustomer() {
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setCustomerName("John Doe");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(1L);
        updatedCustomer.setCustomerName("John Smith");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.save(updatedCustomer);

        assertEquals("John Smith", result.getCustomerName(), "The updated customer name is incorrect.");
        verify(customerRepository).save(updatedCustomer);
    }

    /**
     * Tests deleting an existing customer.
     * <p>
     * This test verifies that deleting a customer correctly invokes the delete method on the customerRepository.
     * </p>
     */
    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        customerService.delete(1L);

        verify(customerRepository).deleteById(1L);
    }

    /**
     * Tests attempting to delete a customer that does not exist.
     * <p>
     * This test verifies that attempting to delete a non-existing customer throws a runtime exception and that
     * the customerRepository's delete method is not called.
     * </p>
     */
    @Test
    void testDeleteCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.delete(1L),
                "Should throw an exception when attempting to delete a non-existing customer.");

        verify(customerRepository, never()).deleteById(any(Long.class));
    }
}
