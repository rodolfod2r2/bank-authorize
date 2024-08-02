package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a customer associated with a specific account.
 * <p>
 * The {@code Customer} class is used to model a customer entity, which includes personal details and associated
 * entities such as phone, address, and account. This class maps to the "customer" table in the database and
 * uses JPA annotations for persistence and Jackson annotations for JSON serialization.
 * </p>
 * <p>
 * The class uses the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} - Manages JSON serialization and deserialization behavior.</li>
 *   <li>{@code @OneToOne} - Defines a one-to-one relationship with other entities (Phone, Address, Account).</li>
 * </ul>
 * </p>
 *
 * @see Phone
 * @see Address
 * @see Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    /**
     * The unique identifier of the customer.
     * <p>
     * This auto-generated value uniquely identifies a customer record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the customer", example = "123", hidden = true)
    @JsonProperty(value = "customer_id", index = 1)
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * The national identification number of the customer.
     * <p>
     * This field holds the customer's national ID, which is a unique identifier for the customer. It is mandatory and unique.
     * </p>
     * <p>
     * example 01152717400
     */
    @Schema(description = "Customer Id National", example = "01152717400")
    @JsonProperty(value = "id_national", index = 2)
    @Column(name = "customer_id_national", nullable = false, unique = true)
    private String customerIdNational;

    /**
     * The full name of the customer.
     * <p>
     * This field contains the customer's full name. It is mandatory and unique.
     * </p>
     * <p>
     * example Rodolfo Gonçalves de Luna Freire
     */
    @Schema(description = "Customer Name", example = "Rodolfo Gonçalves de Luna Freire")
    @JsonProperty(value = "name", index = 3)
    @Column(name = "customer_name", nullable = false, unique = true)
    private String customerName;

    /**
     * The email address of the customer.
     * <p>
     * This field holds the customer's email address, which must be unique and is used for communication.
     * </p>
     * <p>
     * example rodolfod2r2@gmail.com
     */
    @Schema(description = "Customer Email", example = "rodolfod2r2@gmail.com")
    @JsonProperty(value = "email", index = 4)
    @Column(name = "customer_email", nullable = false, unique = true)
    private String customerEmail;

    /**
     * The phone number of the customer.
     * <p>
     * This field represents a one-to-one relationship with the {@code Phone} entity, holding the customer's phone details.
     * </p>
     */
    @Schema(description = "Customer Phone")
    @JsonProperty(value = "phone", index = 5)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Phone phone;

    /**
     * The address of the customer.
     * <p>
     * This field represents a one-to-one relationship with the {@code Address} entity, holding the customer's address details.
     * </p>
     */
    @Schema(description = "Customer Address")
    @JsonProperty(value = "address", index = 6)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    /**
     * The account associated with the customer.
     * <p>
     * This field represents a one-to-one relationship with the {@code Account} entity, holding the account details associated with the customer.
     * </p>
     */
    @Schema(description = "Customer Account")
    @JsonProperty(value = "account", index = 7)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;
}

