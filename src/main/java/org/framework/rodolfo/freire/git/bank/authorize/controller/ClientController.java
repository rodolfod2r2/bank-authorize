package org.framework.rodolfo.freire.git.bank.authorize.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.framework.rodolfo.freire.git.bank.authorize.model.Customer;
import org.framework.rodolfo.freire.git.bank.authorize.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Customer} entities.
 * <p>
 * Provides endpoints for CRUD operations on customer resources using {@link ClientService} to interact with the data layer.
 * </p>
 * <ul>
 *   <li>{@code GET /api/customers} - Retrieves a list of all customers.</li>
 *   <li>{@code GET /api/customers/{customerId}} - Retrieves a specific customer by its ID.</li>
 *   <li>{@code POST /api/customers} - Creates a new customer.</li>
 *   <li>{@code PUT /api/customers/{customerId}} - Updates an existing customer by its ID.</li>
 *   <li>{@code DELETE /api/customers/{customerId}} - Deletes a customer by its ID.</li>
 * </ul>
 *
 * @see ClientService
 * @see Customer
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Endpoints for CRUD operations on customer resources.")
public class ClientController {

    private final ClientService clientService;

    /**
     * Constructs a new {@link ClientController} with the specified {@link ClientService}.
     *
     * @param clientService Service for handling customer-related operations.
     */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Retrieves a list of all {@link Customer} entities.
     *
     * @return A {@link ResponseEntity} containing a list of all customers.
     */
    @Hidden
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customer entities.")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = clientService.getAll();
        return ResponseEntity.ok(customers);
    }

    /**
     * Retrieves a specific {@link Customer} by its ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return A {@link ResponseEntity} containing the customer if found, or a {@code 404 Not Found} response if not found.
     */
    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID", description = "Retrieves a customer entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Customer> getById(@PathVariable Long customerId) {
        Optional<Customer> customer = clientService.getById(customerId);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new {@link Customer}.
     *
     * @param customer The customer data to be created.
     * @return A {@link ResponseEntity} containing the created customer and status.
     */
    @PostMapping
    @Operation(summary = "Create new customer", description = "Creates a new customer entity.")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid customer details provided")
    public ResponseEntity<Customer> save(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = clientService.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body(createdCustomer);
    }

    /**
     * Updates an existing {@link Customer} specified by its ID.
     *
     * @param customerId The ID of the customer to be updated.
     * @param customer   The customer data with updates.
     * @return A {@link ResponseEntity} containing the updated customer if successful, or {@code 404 Not Found} if the customer does not exist.
     */
    @Hidden
    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer", description = "Updates an existing customer entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Customer> update(@PathVariable Long customerId, @Valid @RequestBody Customer customer) {
        customer.setCustomerId(customerId);
        Customer updatedCustomer = clientService.save(customer);
        return updatedCustomer != null ? ResponseEntity.ok(updatedCustomer) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a {@link Customer} specified by its ID.
     *
     * @param customerId The ID of the customer to be deleted.
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete customer", description = "Deletes a customer entity by its ID.")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Void> delete(@PathVariable Long customerId) {
        boolean deleted = clientService.delete(customerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
