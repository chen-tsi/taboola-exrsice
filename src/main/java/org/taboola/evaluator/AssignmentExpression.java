package org.taboola.evaluator;

import java.util.Map;

/**
 * Represents an assignment expression in the form:
 *
 * variable = expression
 * or
 * variable += expression
 *
 * This class evaluates the right-hand side expression,
 * assigns the result to the specified variable, and updates
 * the variable-to-value map.
 */
public class AssignmentExpression implements Evaluable {
    //region Members
    private final VariableExpression variable;
    private final Evaluable expression;
    private final Map<Character, Integer> map;
    //endregion

    //region Constructors
    /**
     * Constructs a new AssignmentExpression.
     *
     * @param variable  The variable to assign a value to.
     * @param expression The expression whose evaluated value is to be assigned.
     * @param map       The map storing current variable-to-value mappings.
     */
    public AssignmentExpression(VariableExpression variable, Evaluable expression, Map<Character, Integer> map) {
        this.variable = variable;
        this.expression = expression;
        this.map = map;
    }
    //endregion

    //region Public Methods
    /**
     * Evaluates the right-hand side expression and assigns its value
     * to the specified variable in the map.
     *
     * @return The evaluated value of the expression.
     */
    @Override
    public int eval() {
        int value = expression.eval();
        map.put(variable.getName(), value);
        return value;
    }
    //endregion
}
