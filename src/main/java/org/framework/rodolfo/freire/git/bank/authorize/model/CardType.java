package org.framework.rodolfo.freire.git.bank.authorize.model;

/**
 * Represents the different types of cards.
 * <p>
 * The {@code CardType} enum defines a set of constants representing various types of cards that can be associated
 * with an account. This categorization helps in distinguishing the different functionalities or characteristics
 * associated with each card type.
 * </p>
 * <ul>
 *   <li>{@code MULTIPLE} - Represents a card type that may have multiple uses or purposes, not restricted to a single category.</li>
 *   <li>{@code CREDIT} - Represents a credit card, which allows the cardholder to borrow funds from the credit card issuer up to a certain limit.</li>
 *   <li>{@code DEBIT} - Represents a debit card, which allows the cardholder to access funds directly from their bank account.</li>
 * </ul>
 *
 * @see Card
 */

public enum CardType {
    MULTIPLE, CREDIT, DEBIT
}
