package org.taboola.evaluator;

import java.util.Map;

/**
 * Represents a variable expression that performs a post increment operation (e.g., x++).
 */
public class PostIncrementVariableExpression extends VariableExpression {

    //region Constructors
    /**
     * Constructs a {@code PostIncrementVariableExpression} with the specified variable name
     * and a reference to the variable-to-value map.
     *
     * @param name              the name of the variable (e.g., 'x')
     * @param variableToNumber  the map storing variable values
     */
    public PostIncrementVariableExpression(char name, Map<Character, Integer> variableToNumber) {
        super(name, variableToNumber);
    }
    //endregion

    //region Public Methods
    /**
     * Evaluates the variable by returning its current value, and then increments
     * the variable's value by 1 in the underlying variable map.
     *
     * @return the current value of the variable before incrementing
     */
    @Override
    public int eval() {
        int result = super.eval();
        this.variableToNumber.put(this.name, result + 1);
        return result;
    }
    //endregion
}
