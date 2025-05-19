package org.taboola.evaluator;

import java.util.Map;

public class AssignmentExpression implements Evaluable {
    //region Members
    private final VariableExpression variable;
    private final Evaluable expression;
    private final Map<Character, Integer> map;
    //endregion

    //region Constructors
    public AssignmentExpression(VariableExpression variable, Evaluable expression, Map<Character, Integer> map) {
        this.variable = variable;
        this.expression = expression;
        this.map = map;
    }
    //endregion

    //region Public Methods
    @Override
    public int eval() {
        int value = expression.eval();
        map.put(variable.getName(), value);
        return value;
    }
    //endregion
}
