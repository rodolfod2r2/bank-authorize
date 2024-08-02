package org.framework.rodolfo.freire.git.bank.authorize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a phone associated with a customer, merchant, or other entities.
 * <p>
 * The {@code Phone} class models a phone entity, including details such as the phone's identifier, type, and number.
 * This class maps to the "phone" table in the database and uses JPA annotations for persistence and Jackson annotations
 * for JSON serialization.
 * </p>
 * <p>
 * The class uses the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} - Manages JSON serialization and deserialization behavior.</li>
 *   <li>{@code @Enumerated} - Specifies how to persist enum values.</li>
 * </ul>
 * </p>
 *
 * @see PhoneType
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {

    /**
     * The unique identifier of the phone.
     * <p>
     * This auto-generated value uniquely identifies a phone record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the phone", hidden = true)
    @JsonIgnore
    @JsonProperty(value = "phone_id", index = 1)
    @Column(name = "phone_id")
    private long phoneId;

    /**
     * The type of the phone.
     * <p>
     * This field contains the type of phone, represented by the {@code PhoneType} enum. It helps in categorizing the phone
     * based on its usage or category (e.g., mobile, home, office, room).
     * </p>
     */
    @Schema(description = "Phone Type")
    @JsonProperty(value = "type", index = 2)
    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;

    /**
     * The phone number.
     * <p>
     * This field holds the actual phone number in string format. It is used for communication purposes.
     * </p>
     * <p>
     * example 83988761253
     */
    @Schema(description = "Phone Number", example = "83988761253")
    @JsonProperty(value = "number", index = 3)
    @Column(name = "phone_number")
    private String phoneNumber;

}
