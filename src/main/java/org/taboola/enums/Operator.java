package org.taboola.enums;

/**
 * Enumeration representing supported operators.
 * Each operator is associated with its corresponding string symbol.
 */
public enum Operator {
    ADD("+"),
    SUB("-"),
    MULTIPLE("*"),
    ASSIGN("="),
    ADD_ASSIGN("+="),
    /** Fallback for unknown or unsupported operators */
    UNKNOWN("");

    private final String value;

    /**
     * Constructs an Operator enum with the specified string representation.
     *
     * @param value the string representation of the operator
     */
    Operator(String value) {
        this.value = value;
    }

    /**
     * Returns the corresponding Operator enum constant for a given string value.
     * If the value does not match any known operator, {@code UNKNOWN} is returned.
     *
     * @param value the string representation of the operator
     * @return the corresponding Operator enum constant, or {@code UNKNOWN} if no match is found
     */
    public static Operator fromValue(String value) {
        for (Operator operator : Operator.values()) {
            if (operator.value.equals(value)) {
                return operator;
            }
        }
        return UNKNOWN;
    }
}
