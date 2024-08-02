package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model that represents a financial transaction.
 * <p>
 * The {@code Transaction} class models a transaction record, including details such as the transaction ID,
 * associated account ID, transaction amount, merchant, Merchant Category Code (MCC), date, and time. This class
 * is used to store and manage transaction information within the system.
 * </p>
 * <p>
 * The class uses JPA annotations for persistence and follows standard Java conventions for representing transaction data.
 * </p>
 *
 * @see Account
 * @see Merchant
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transaction")
public class Transaction {

    /**
     * The unique identifier of the transaction.
     * <p>
     * This auto-generated value uniquely identifies a transaction record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the phone", hidden = true)
    @JsonIgnore
    @JsonProperty(value = "id", index = 1)
    @Column(name = "transaction_id")
    private Long transactionId;

    /**
     * The identifier of the account associated with this transaction.
     * <p>
     * This field references the ID of the account that is involved in the transaction.
     * </p>
     */
    @Schema(description = "Account Id", hidden = true)
    @JsonProperty(value = "account_id", index = 2)
    @JsonIgnore
    @Column(name = "account_id")
    private Long accountId;

    /**
     * The identifier of the card number associated with this transaction.
     * <p>
     * This field references the CARD NUMBER of the account that is involved in the transaction.
     * </p>
     */
    @Schema(description = "Card number", example = "378282246310005")
    @JsonProperty(value = "card_number", index = 3)
    @Column(name = "card_number")
    private String cardNumber;

    /**
     * The amount of money involved in the transaction.
     * <p>
     * This field represents the monetary value of the transaction.
     * </p>
     */
    @Schema(description = "Transaction Amount", example = "100.00")
    @JsonProperty(value = "total_amount", index = 4)
    @Column(name = "total_amount")
    private Double transactionAmount;

    /**
     * The name of the merchant where the transaction occurred.
     * <p>
     * This field holds the name of the merchant or vendor involved in the transaction.
     * </p>
     */
    @Schema(description = "Merchant Name", example = "UBER EATS SAO PAULO BR")
    @JsonProperty(value = "merchant", index = 5)
    @Column(name = "transaction_merchant")
    private String transactionMerchant;

    /**
     * The Merchant Category Code (MCC) associated with the transaction.
     * <p>
     * This field represents the MCC that categorizes the type of merchant involved in the transaction.
     * </p>
     */
    @Schema(description = "Merchant Name", example = "0000")
    @JsonProperty(value = "mcc", index = 6)
    @Column(name = "transaction_mcc")
    private String transactionMcc;

    /**
     * The date when the transaction took place.
     * <p>
     * This field represents the date of the transaction in the {@code LocalDate} format.
     * </p>
     */
    @Schema(description = "Transaction Date")
    @JsonProperty(value = "date", index = 7)
    @JsonIgnore
    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    /**
     * The time when the transaction occurred.
     * <p>
     * This field represents the time of the transaction in the {@code LocalTime} format.
     * </p>
     */
    @Schema(description = "Transaction Time")
    @JsonProperty(value = "time", index = 8)
    @JsonIgnore
    @Column(name = "transaction_time")
    private LocalTime transactionTime;

}

