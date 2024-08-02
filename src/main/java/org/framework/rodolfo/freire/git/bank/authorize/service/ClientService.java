package org.framework.rodolfo.freire.git.bank.authorize.service;

import org.framework.rodolfo.freire.git.bank.authorize.model.Customer;
import org.framework.rodolfo.freire.git.bank.authorize.repository.ClientRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to customers.
 * <p>
 * The {@code ClientService} class provides methods for managing customer entities. This includes operations for
 * retrieving, creating, updating, and deleting customers. It interacts with the {@code ClientRepository} to
 * perform these operations.
 * </p>
 * <p>
 * The service class is annotated with {@code @Service}, marking it as a Spring service component that should be
 * managed by the Spring container.
 * </p>
 *
 * @see Customer
 * @see ClientRepository
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Constructs a {@code ClientService} with the specified {@code ClientRepository}.
     * <p>
     * This constructor initializes the service with the repository needed for performing customer-related operations.
     * </p>
     *
     * @param clientRepository The repository used to access customer data.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Retrieves all customers from the repository.
     *
     * @return A list of all customers.
     */
    public List<Customer> getAll() {
        return clientRepository.findAll();
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return An {@code Optional} containing the customer if found, or an empty {@code Optional} if not found.
     */
    public Optional<Customer> getById(Long customerId) {
        return clientRepository.findById(customerId);
    }

    /**
     * Creates a new customer in the repository.
     *
     * @param customer The customer to create.
     * @return The created customer.
     */
    public Customer save(Customer customer) {
        return clientRepository.save(customer);
    }

    /**
     * Updates an existing customer in the repository.
     * <p>
     * The customer with the specified ID will be updated with the information from the provided customer object.
     * If the customer with the given ID does not exist, the method returns {@code null}.
     * </p>
     *
     * @param customerId The ID of the customer to update.
     * @param customer   The customer with updated information.
     * @return The updated customer, or {@code null} if the customer with the given ID does not exist.
     */
    public Customer update(Long customerId, Customer customer) {
        if (clientRepository.existsById(customerId)) {
            customer.setCustomerId(customerId);
            return clientRepository.save(customer);
        }
        return null;
    }

    /**
     * Removes a customer by their ID.
     *
     * @param customerId The ID of the customer to remove.
     */
    @Transactional
    public boolean delete(Long customerId) {
        if (!clientRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
        try {
            clientRepository.deleteById(customerId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
