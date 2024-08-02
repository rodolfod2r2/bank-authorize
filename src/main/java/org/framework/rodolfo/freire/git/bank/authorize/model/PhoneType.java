package org.framework.rodolfo.freire.git.bank.authorize.model;

/**
 * Enum representing the different types of phones available.
 * <p>
 * The {@code PhoneType} enum defines various categories of phone types used to classify phone numbers.
 * Each type represents a specific category of phone, which can be used for organizing and managing phone
 * records within the system.
 * </p>
 * <ul>
 *   <li>{@code RESIDENTIAL} - Represents a phone number associated with a residential setting.</li>
 *   <li>{@code COMMERCIAL} - Represents a phone number associated with a commercial or business setting.</li>
 *   <li>{@code OFFICE} - Represents a phone number associated with an office or workplace setting.</li>
 *   <li>{@code ROOM} - Represents a phone number associated with a specific room or internal extension.</li>
 * </ul>
 *
 * @see Phone
 */

public enum PhoneType {
    RESIDENTIAL, COMMERCIAL, OFFICE, ROOM
}