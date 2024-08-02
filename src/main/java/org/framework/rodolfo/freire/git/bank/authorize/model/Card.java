package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a card associated with an account.
 * <p>
 * The {@code Card} class is used to model a card associated with a financial account. It includes fields
 * for a unique identifier, card number, and card type. The class uses JPA annotations to define its persistence
 * behavior and Jackson annotations for JSON serialization.
 * </p>
 * <p>
 * The class uses the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} - Manages JSON serialization and deserialization behavior.</li>
 *   <li>{@code @Enumerated} - Specifies that an enumeration should be stored as a string in the database.</li>
 * </ul>
 * </p>
 *
 * @see CardType
 * @see Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    /**
     * The unique identifier of the card.
     * <p>
     * This auto-generated value uniquely identifies a card record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the card", hidden = true)
    @JsonProperty("card_id")
    @Column(name = "card_id")
    private Long cardId;

    /**
     * The number of the card.
     * <p>
     * This field contains the card number used for transactions. It is a string to accommodate different card formats.
     * </p>
     * <p>
     * example 378282246310005
     */
    @Schema(description = "Card number", example = "378282246310005")
    @JsonProperty("number")
    @Column(name = "card_number")
    private String cardNumber;

    /**
     * The type of card.
     * <p>
     * This field represents the type of card (e.g., CREDIT, DEBIT) and is stored as a string in the database.
     * </p>
     */
    @Schema(description = "Card type")
    @JsonProperty("type")
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

}

