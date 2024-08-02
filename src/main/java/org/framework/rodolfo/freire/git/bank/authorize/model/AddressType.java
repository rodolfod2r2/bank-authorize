package org.framework.rodolfo.freire.git.bank.authorize.model;

/**
 * Represents the different types of addresses available.
 * <p>
 * The {@code AddressType} enum defines a set of constants that categorize various types of addresses.
 * These types are used to distinguish between different address purposes or categories in an address management system.
 * </p>
 * <ul>
 *   <li>{@code RESIDENTIAL} - Represents a residential address, typically used for personal or home purposes.</li>
 *   <li>{@code COMMERCIAL} - Represents a commercial address, typically used for business or office purposes.</li>
 *   <li>{@code ROOM} - Represents an address for a specific room, which may be used in contexts such as hotels or shared accommodations.</li>
 * </ul>
 *
 * @see Address
 */

public enum AddressType {
    RESIDENTIAL, COMMERCIAL, ROOM
}