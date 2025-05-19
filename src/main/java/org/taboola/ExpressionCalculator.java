package org.taboola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.taboola.enums.Operator;
import org.taboola.evaluator.*;
import org.taboola.exceptions.InvalidExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Calculator class that parses, validates, and evaluates numeric expressions.
 *
 * Supports variables, assignments, pre/post increments, and basic arithmetic operators.
 * Maintains a map of variable names to their current integer values.
 */
@Component
public class ExpressionCalculator {

    //region Members

    /**
     * Stores variable names and their corresponding integer values.
     */
    private final Map<Character, Integer> variableToNumber;

    /**
     * Validator instance used to verify the correctness of expressions before evaluation.
     */
    @Autowired
    private final ExpressionValidator validator;
    //endregion

    //region Constructors

    /**
     * Constructs a new {@code ExpressionCalculator} with the provided expression validator.
     *
     * @param validator the expression validator to validate expressions before evaluation
     */
    public ExpressionCalculator(ExpressionValidator validator) {
        this.validator = validator;
        this.variableToNumber = new HashMap<>();
    }
    //endregion

    //region Public Methods

    /**
     * Calculates the result of the given numeric expression string.
     *
     * This method validates the expression, parses it into an evaluable structure,
     * and then computes the result.
     *
     * @param expression the expression string to evaluate
     * @return the integer result of evaluating the expression
     * @throws InvalidExpression if the expression is invalid according to the validator
     */
    public int calculate(String expression) {
        if (!validator.isValidExpression(expression)) {
            throw new InvalidExpression();
        }

        expression = expression.replace("(", " ( ").replace(")", " ) ");
        Scanner scanner = new Scanner(expression);
        Evaluable expressionEvaluation = buildExpression(scanner);
        return expressionEvaluation.eval();
    }

    /**
     * Returns a copy of the current map of variables and their values.
     *
     * @return a new map containing all variable names and their corresponding values
     */
    public Map<Character, Integer> getVariables() {
        return new HashMap<>(this.variableToNumber);
    }

    /**
     * Returns a string representation of all variables and their values,
     * formatted as a comma-separated list enclosed in parentheses (e.g., "(a=1,b=2)").
     *
     * @return a formatted string of variables and their values
     */
    public String getVariablesAsString() {
        return this.variableToNumber.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * Clears all variables and their values from the internal state.
     */
    public void reset() {
        this.variableToNumber.clear();
    }
    //endregion

    //region Private Methods

    /**
     * Builds an {@link Evaluable} expression tree from the tokens provided by the scanner.
     *
     * @param scanner the scanner that provides tokens of the expression
     * @return an evaluable expression representing the parsed expression
     */
    private Evaluable buildExpression(Scanner scanner) {
        Evaluable left = getEvaluator(scanner);
        return parseOperation(left, scanner);
    }

    /**
     * Parses an operation (e.g., +, -, *, =) with the given left operand and remaining tokens.
     * Recursively builds and returns the combined evaluable expression.
     *
     * @param left    the left operand expression
     * @param scanner the scanner that provides tokens of the expression
     * @return the combined evaluable expression after parsing the operator and right operand
     */
    private Evaluable parseOperation(Evaluable left, Scanner scanner) {
        if (scanner.hasNext()) {
            String token = scanner.next();

            Operator operator = Operator.fromValue(token);
            switch (operator) {
                case ADD, SUB:
                    return new Expression(left, operator, buildExpression(scanner));
                case MULTIPLE:
                    Evaluable innerExpr = new Expression(left, operator, getEvaluator(scanner));
                    if (scanner.hasNext()) {
                        return parseOperation(innerExpr, scanner);
                    }
                    return innerExpr;
                case ASSIGN:
                    return new AssignmentExpression((VariableExpression) left, buildExpression(scanner), this.variableToNumber);
                case ADD_ASSIGN:
                    VariableExpression LeftAsVariable = (VariableExpression) left;
                    Expression addToVarExpression = new Expression(LeftAsVariable, Operator.ADD, buildExpression(scanner));
                    return new AssignmentExpression(LeftAsVariable, addToVarExpression, this.variableToNumber);
            }
        }
        return left;
    }

    /**
     * Returns an {@link Evaluable} representing the next token from the scanner.
     *
     * Recognizes numbers, variables, pre/post increment expressions, and parentheses.
     *
     * @param scanner the scanner that provides tokens of the expression
     * @return an evaluable expression for the next token or {@code null} if unrecognized
     */
    private Evaluable getEvaluator(Scanner scanner) {
        String token = scanner.next();

        if (ExpressionUtils.isNumber(token))
            return new NumberExpression(Integer.parseInt(token));

        if (ExpressionUtils.isVariable(token))
            return new VariableExpression(token.charAt(0), this.variableToNumber);

        if (ExpressionUtils.isPostIncrement(token))
            return new PostIncrementVariableExpression(token.charAt(0), this.variableToNumber);

        if (ExpressionUtils.isPreIncrement(token)) {
            VariableExpression variableExpression = new VariableExpression(token.charAt(2), variableToNumber);
            Expression IncreasingExpression = new Expression(new NumberExpression(1), Operator.ADD, variableExpression);
            return new AssignmentExpression(variableExpression, IncreasingExpression, this.variableToNumber);
        }

        if (ExpressionUtils.isOpeningParenthesis(token)) {
            return buildExpression(scanner);
        }
        return null;
    }
    //endregion
}
