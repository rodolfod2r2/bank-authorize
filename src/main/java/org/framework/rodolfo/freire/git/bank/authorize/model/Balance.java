package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the balance of a specific type of account.
 * <p>
 * The {@code Balance} class is used to model the financial balance associated with a particular type of account.
 * This class includes fields for a unique identifier, the type of account, and the current balance amount.
 * It uses JPA annotations for entity mapping and Jackson annotations for JSON serialization.
 * </p>
 * <p>
 * The class uses the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Enumerated} - Specifies that an enumeration should be stored as a string in the database.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} - Manages JSON serialization and deserialization behavior.</li>
 * </ul>
 * </p>
 *
 * @see AccountType
 * @see Account
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {

    /**
     * The unique identifier of the balance.
     * <p>
     * This auto-generated value uniquely identifies a balance record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the balance", hidden = true)
    @JsonProperty("balance_id")
    @Column(name = "balance_id")
    private Long balanceId;

    /**
     * The type of account associated with this balance.
     * <p>
     * This field represents the type of account for which this balance is maintained. It is stored as a string in the database.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    @Column(name = "balance_type")
    private AccountType balanceType;

    /**
     * The amount of the balance.
     * <p>
     * This field represents the current balance amount for the account type. It is a numerical value stored in the database.
     * </p>
     *
     * @example 0.00
     */
    @Schema(description = "Balance amount", example = "0.00", type = "number", format = "double")
    @JsonProperty("amount")
    @Column(name = "balance_amount")
    private double balanceAmount;

}