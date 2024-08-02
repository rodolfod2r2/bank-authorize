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
 * Represents an address with various components such as postal code, street information, and location details.
 * <p>
 * The {@code Address} class provides information about an address, including its unique identifier, type,
 * postal code, street details, neighborhood, city, state, and country. This class uses JPA annotations to
 * define its persistence behavior and Jackson annotations for JSON serialization.
 * </p>
 * <p>
 * The class utilizes the following annotations:
 * <ul>
 *   <li>{@code @Entity} - Marks the class as a JPA entity.</li>
 *   <li>{@code @Table} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} and {@code @GeneratedValue} - Define the primary key and its generation strategy.</li>
 *   <li>{@code @Schema} - Provides metadata for API documentation purposes.</li>
 *   <li>{@code @JsonProperty} and {@code @JsonIgnore} - Manage JSON serialization and deserialization behavior.</li>
 *   <li>{@code @Enumerated} - Specifies how an enumeration should be persisted.</li>
 * </ul>
 * </p>
 *
 * @see AddressType
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    /**
     * The unique identifier of the address.
     * <p>
     * This auto-generated value uniquely identifies an address record in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier of the address", hidden = true)
    @JsonProperty(value = "address_id", index = 1)
    @JsonIgnore
    @Column(name = "address_id")
    private long addressId;

    /**
     * The type of the address.
     * <p>
     * This field represents the type of address (e.g., home, office) and is stored as a string in the database.
     * </p>
     */
    @Schema(description = "Address Type")
    @JsonProperty(value = "type", index = 2)
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    /**
     * The postal code of the address.
     * <p>
     * This field contains the postal code used for mail delivery.
     * </p>
     * <p>
     * example 70672-255
     */
    @Schema(description = "Postal Code", example = "70672-255")
    @JsonProperty(value = "postal_code", index = 3)
    @Column(name = "address_postalcode")
    private String postalCode;

    /**
     * The name of the street for the address.
     * <p>
     * This field represents the primary street address line.
     * </p>
     * <p>
     * example Primeiro de Maio
     */
    @Schema(description = "Name of the street", example = "Primeiro de Maio")
    @JsonProperty(value = "streetLine1", index = 4)
    @Column(name = "address_streetLine1")
    private String streetLine1;

    /**
     * The house number or additional street information.
     * <p>
     * This field provides secondary street information such as the house number.
     * </p>
     * <p>
     * example 8701
     */
    @Schema(description = "House number", example = "8701")
    @JsonProperty(value = "streetLine2", index = 5)
    @Column(name = "streetLine2")
    private String streetLine2;

    /**
     * Additional address information or complement.
     * <p>
     * This field provides extra details for the address such as apartment or unit number.
     * </p>
     * <p>
     * example Lote 2 - Casa
     */
    @Schema(description = "Address complement", example = "Lote 2 - Casa")
    @JsonProperty(value = "streetLine3", index = 6)
    @Column(name = "address_streetLine3")
    private String streetLine3;

    /**
     * The neighborhood or district where the address is located.
     * <p>
     * This field represents the neighborhood or locality of the address.
     * </p>
     * <p>
     * example Industrial
     */
    @Schema(description = "Neighborhood", example = "Industrial")
    @JsonProperty(value = "streetLine4", index = 7)
    @Column(name = "address_streetLine4")
    private String streetLine4;

    /**
     * The name of the city where the address is located.
     * <p>
     * This field contains the city name of the address.
     * </p>
     * <p>
     * example João Pessoa
     */
    @Schema(description = "Name of the city", example = "João Pessoa")
    @JsonProperty(value = "city", index = 8)
    @Column(name = "address_city")
    private String city;

    /**
     * The name of the state or region where the address is located.
     * <p>
     * This field contains the state or regional name associated with the address.
     * </p>
     * <p>
     * example Paraiba
     */
    @Schema(description = "Name of the state", example = "Paraiba")
    @JsonProperty(value = "state", index = 9)
    @Column(name = "address_state")
    private String state;

    /**
     * The name of the country where the address is located.
     * <p>
     * This field contains the country name of the address.
     * </p>
     * <p>
     * example Brazil
     */
    @Schema(description = "Name of the country", example = "Brazil")
    @JsonProperty(value = "country", index = 10)
    @Column(name = "address_country")
    private String country;
}
