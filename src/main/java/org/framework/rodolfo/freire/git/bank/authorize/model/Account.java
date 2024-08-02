package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a bank account that has multiple balances for different account types and an associated card.
 * <p>
 * The {@code Account} class contains information about the account's unique identifier, account number,
 * a set of balances for different types of expenses, and an associated card.
 * </p>
 * <p>
 * The class uses annotations from various libraries to define its persistence and serialization behavior:
 * <ul>
 *   <li>{@code @Entity} - Marks this class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} and {@code @JsonIgnore} - Define JSON serialization and deserialization behavior.</li>
 *   <li>{@code @OneToMany} and {@code @OneToOne} - Specify relationships between entities.</li>
 * </ul>
 * </p>
 *
 * @see Balance
 * @see Card
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    /**
     * The unique identifier of the account.
     * <p>
     * This is an auto-generated value used to uniquely identify an account in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the account", hidden = true)
    @JsonIgnore
    @JsonProperty(value = "account_id", index = 1)
    @Column(name = "account_id")
    private Long accountId;

    /**
     * The account number of this bank account.
     * <p>
     * This is a string representing the account number. It is used for identifying the account in transactions.
     * </p>
     */
    @Schema(description = "Account Number", example = "1234567890")
    @JsonProperty(value = "number", index = 2)
    @Column(name = "account_number")
    private String accountNumber;

    /**
     * A set of balances associated with different types of expenses for this account.
     * <p>
     * This collection contains {@link Balance} objects, each representing a different type of balance (e.g., FOOD, MEAL, CASH).
     * </p>
     */
    @Schema(description = "Account Balances Type", example = "[{\"type\": \"FOOD\", \"amount\": 0.00}, {\"type\": \"MEAL\", \"amount\": 0.00}, {\"type\": \"CASH\", \"amount\": 0.00}]")
    @JsonProperty(value = "balances", index = 3)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Balance> balances = new HashSet<>();

    /**
     * The card associated with this bank account.
     * <p>
     * This represents a single {@link Card} object related to the account, including details such as card number, expiration date, etc.
     * </p>
     */
    @Schema(description = "Card Multiple")
    @JsonProperty(value = "card", index = 4)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Card card;


}
