package org.taboola.evaluator;

import lombok.extern.slf4j.Slf4j;
import org.taboola.exceptions.UndefinedVariableException;

import java.util.Map;

/**
 * Represents a variable within an expression.
 */
@Slf4j
public class VariableExpression implements Evaluable {

    //region Members
    /**
     * The name of the variable (e.g., 'x').
     */
    final char name;

    /**
     * A reference to the map containing variable names and their corresponding integer values.
     */
    final Map<Character, Integer> variableToNumber;
    //endregion

    //region Constructors
    /**
     * Constructs a {@code VariableExpression} with the specified variable name and map of variable values.
     *
     * @param name              the name of the variable
     * @param variableToNumber  the map containing variable names and values
     */
    public VariableExpression(char name, Map<Character, Integer> variableToNumber) {
        this.name = name;
        this.variableToNumber = variableToNumber;
    }
    //endregion

    //region Public Methods
    /**
     * Evaluates the variable by looking up its value in the map.
     * If the variable is not defined, logs an error and throws an {@link UndefinedVariableException}.
     *
     * @return the current value of the variable
     * @throws UndefinedVariableException if the variable is not present in the map
     */
    @Override
    public int eval() {
        if (!this.variableToNumber.containsKey(this.name)) {
            log.error("The variable {} is undefined", this.name);
            throw new UndefinedVariableException();
        }
        return this.variableToNumber.get(this.name);
    }

    /**
     * Returns the name of the variable.
     *
     * @return the variable name
     */
    public char getName() {
        return this.name;
    }
    //endregion
}