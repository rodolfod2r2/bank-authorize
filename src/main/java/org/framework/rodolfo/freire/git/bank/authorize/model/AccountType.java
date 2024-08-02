package org.framework.rodolfo.freire.git.bank.authorize.model;

/**
 * Represents the different types of accounts available.
 * <p>
 * The {@code AccountType} enum defines a set of constants that represent various types of accounts.
 * These types can be used to categorize accounts for different purposes, such as budgeting or expense tracking.
 * </p>
 * <ul>
 *   <li>{@code FOOD} - Represents an account type dedicated to food-related expenses.</li>
 *   <li>{@code MEAL} - Represents an account type for meal-related expenses.</li>
 *   <li>{@code CASH} - Represents an account type for cash transactions or expenses.</li>
 * </ul>
 *
 * @see Account
 */

public enum AccountType {
    FOOD, MEAL, CASH
}