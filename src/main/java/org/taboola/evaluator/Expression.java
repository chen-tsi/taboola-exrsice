package org.taboola.evaluator;

import lombok.Builder;
import org.taboola.enums.Operator;

/**
 * Represents a binary expression composed of a left operand, an operator, and a right operand.
 * This class implements {@link Evaluable}, allowing recursive evaluation of expression trees.
 */
@Builder
public class Expression implements Evaluable {

    //region Members
    /**
     * The left-hand side operand of the expression.
     */
    private final Evaluable left;

    /**
     * The operator to apply between the operands.
     */
    private final Operator op;

    /**
     * The right-hand side operand of the expression.
     */
    private final Evaluable right;
    //endregion

    //region Constructors
    /**
     * Constructs a new Expression with the given left operand, operator, and right operand.
     *
     * @param left  the left operand
     * @param op    the operator
     * @param right the right operand
     */
    public Expression(Evaluable left, Operator op, Evaluable right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }
    //endregion

    //region Public Methods
    /**
     * Evaluates the expression by recursively evaluating its operands and applying the operator.
     *
     * @return the result of applying the operator to the evaluated operands
     */
    @Override
    public int eval() {
        int leftValue = left.eval();
        int rightValue = right.eval();

        return switch (op) {
            case ADD -> leftValue + rightValue;
            case SUB -> leftValue - rightValue;
            case MULTIPLE -> leftValue * rightValue;
            default -> 0; // todo chen- consider to throw an exception
        };
    }
    //endRegion
}
