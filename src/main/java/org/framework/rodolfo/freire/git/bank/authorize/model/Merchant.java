package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a merchant, which can be associated with a specific MCC (Merchant Category Code).
 * <p>
 * The {@code Merchant} class models a merchant entity, including details such as the merchant's identifier,
 * Merchant Category Code (MCC), name, phone, and address. This class maps to the "merchant" table in the database
 * and utilizes JPA annotations for persistence and Jackson annotations for JSON serialization.
 * </p>
 * <p>
 * The class uses the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} - Manages JSON serialization and deserialization behavior.</li>
 *   <li>{@code @OneToOne} - Defines one-to-one relationships with other entities (Phone, Address).</li>
 * </ul>
 * </p>
 *
 * @see Phone
 * @see Address
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "merchant")
public class Merchant {

    /**
     * The unique identifier of the merchant.
     * <p>
     * This auto-generated value uniquely identifies a merchant record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the merchant", hidden = true)
    @JsonProperty(value = "merchant_id", index = 1)
    @Column(name = "merchant_id")
    private Long merchantId;

    /**
     * The Merchant Category Code (MCC) used to identify the type of goods or services provided by the merchant.
     * <p>
     * This field contains the MCC associated with the merchant. It helps in categorizing the merchant based on
     * their business activities.
     * </p>
     * <p>
     * example 1234
     */
    @Schema(description = "Merchant number identify", example = "1234")
    @JsonProperty(value = "mcc", index = 2)
    @Column(name = "merchant_mcc")
    private String merchantMcc;

    /**
     * The name of the merchant.
     * <p>
     * This field holds the full name of the merchant as used for business transactions and identification.
     * </p>
     * <p>
     * example UBER TRIP SAO PAULO BR
     */
    @Schema(description = "Merchant name", example = "UBER TRIP SAO PAULO BR")
    @JsonProperty(value = "name", index = 3)
    @Column(name = "merchant_name")
    private String merchantName;

    /**
     * The phone number of the merchant.
     * <p>
     * This field represents a one-to-one relationship with the {@code Phone} entity, containing the merchant's
     * contact phone number.
     * </p>
     */
    @Schema(description = "Merchant Phone")
    @JsonProperty(value = "phone", index = 4)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Phone merchantPhone;

    /**
     * The address of the merchant.
     * <p>
     * This field represents a one-to-one relationship with the {@code Address} entity, containing the merchant's
     * business address.
     * </p>
     */
    @Schema(description = "Merchant Address")
    @JsonProperty(value = "address", index = 5)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address merchantAddress;
}