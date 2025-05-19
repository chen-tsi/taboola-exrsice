package org.taboola.evaluator;

/**
 * Represents a numeric literal in an expression tree.
 * This class wraps an integer value and implements {@link Evaluable},
 * allowing it to be evaluated as a constant value.
 */
public class NumberExpression implements Evaluable {

    //region Members
    /**
     * The integer value of this numeric expression.
     */
    private final int value;
    //endregion

    //region Constructors
    /**
     * Constructs a new {@code NumberExpression} with the specified value.
     *
     * @param value the numeric value of the expression
     */
    public NumberExpression(int value) {
        this.value = value;
    }
    //endregion

    //region Public Methods
    /**
     * Evaluates the numeric expression.
     *
     * @return the numeric value of this expression
     */
    @Override
    public int eval() {
        return value;
    }
    //endregion
}
