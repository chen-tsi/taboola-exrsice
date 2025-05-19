package org.taboola;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.taboola.enums.Operator;

import java.util.Scanner;

/**
 * Validates the syntax and structure of numeric expressions.
 * This includes checks for:
 * - Valid characters
 * - Balanced parentheses
 *  - assigment expression
 * - Proper structure of operands and operators
 */
@Slf4j
@Component
public class ExpressionValidator {
    //region Members

    /**
     * Regex pattern to validate that the expression contains only allowed characters:
     * letters, digits, operators, parentheses, and whitespace.
     */
    private static final String VALID_EXPRESSION_CHARACTER_PATTERN = "[a-zA-Z0-9+\\-*=()\\s]+";

    //endregion

    //region Public Methods

    /**
     * Validates the given expression string for allowed characters, balanced parentheses,
     * and proper syntactical structure.
     *
     * @param expression the numeric expression to validate
     * @return true if the expression is valid, otherwise false
     */
    public boolean isValidExpression(String expression) {
        if (!hasValidCharacters(expression)) {
            log.error("The expression {} has invalid character.", expression);
            return false;
        }

        if (!isBalancedParentheses(expression)) {
            log.error("The expression {} has invalid parentheses", expression);
            return false;
        }

        if (!isAssignmentExpression(expression)) {
            log.error("The expression is not an assignment expression");
            return false;
        }

        return isValidStructure(expression);
    }

    //endregion

    //region Private Methods

    /**
     * Checks if the expression only contains valid characters (letters, digits, operators, parentheses, whitespace).
     *
     * @param expression the expression to check
     * @return true if valid, otherwise false
     */
    private boolean hasValidCharacters(String expression) {
        return expression.matches(VALID_EXPRESSION_CHARACTER_PATTERN);
    }

    /**
     * Checks if the parentheses in the expression are balanced and properly ordered.
     *
     * @param expression the expression to check
     * @return true if balanced, otherwise false
     */
    private boolean isBalancedParentheses(String expression) {
        int balance = 0;

        expression = expression.replace("(", " ( ").replace(")", " ) ");
        Scanner scanner = new Scanner(expression);

        while (scanner.hasNext()) {
            String token = scanner.next();
            if (ExpressionUtils.isOpeningParenthesis(token)) {
                balance++;
            } else if (ExpressionUtils.isClosingParenthesis(token)) {
                balance--;
            }

            if (balance < 0) {
                return false;
            }
        }

        return balance == 0;
    }

    /**
     * Verifies that the expression has a syntactically correct structure,
     * ensuring proper placement of operands, operators, and parentheses.
     *
     * @param expression the expression to validate
     * @return true if structure is valid, otherwise false
     */
    private boolean isValidStructure(String expression) {
        if (expression == null || expression.isBlank()) {
            log.error("Empty expression is invalid");
            return false;
        }

        String previous = null;
        boolean expectingOperand = true;
        boolean isValidStructure = true;

        expression = expression.replace("(", " ( ").replace(")", " ) ");
        Scanner scanner = new Scanner(expression);

        while (scanner.hasNext()) {
            String token = scanner.next();

            if (ExpressionUtils.isOperator(token)) {
                if (expectingOperand) {
                    log.error("Invalid expression structure: expected operand after '{}' but found '{}'.", previous, token);
                    isValidStructure = false;
                    break;
                }
                expectingOperand = true;
            } else if (ExpressionUtils.isClosingParenthesis(token)) {
                if (expectingOperand || previous == null || ExpressionUtils.isOpeningParenthesis(previous)) {
                    log.error("Invalid expression structure: unexpected closing parenthesis after '{}'.", previous);
                    isValidStructure = false;
                    break;
                }
            } else if (ExpressionUtils.isOperand(token) || ExpressionUtils.isOpeningParenthesis(token)) {
                boolean isInvalidPrevious = previous != null &&
                        (ExpressionUtils.isOperand(previous) || ExpressionUtils.isClosingParenthesis(previous));
                if (!expectingOperand || isInvalidPrevious) {
                    log.error("Invalid expression structure: unexpected operand or opening parenthesis after '{}'.", previous);
                    isValidStructure = false;
                    break;
                }

                if (ExpressionUtils.isOperand(token)) {
                    expectingOperand = false;
                }
            } else {
                log.error("Invalid token encountered: '{}'", token);
                isValidStructure = false;
                break;
            }

            previous = token;
        }

        return isValidStructure && !expectingOperand;
    }

    /**
     * Checks whether a given expression is an assignment expression.
     *
     * An expression is considered an assignment expression if it satisfies one of the following:
     * It is a unary increment operator, or it starts with a variable followed by an assignment operator .
     *
     * @param expression The input expression to validate.
     * @return true if the expression is an assignment expression; false otherwise.
     */
    private boolean isAssignmentExpression(String expression) {
        if (ExpressionUtils.isUnaryOperator(expression)) {
            return true;
        }

        expression = expression.replace("(", " ( ").replace(")", " ) ");
        Scanner scanner = new Scanner(expression);

        if (!scanner.hasNext()) {
            return false;
        }

        String firstToken = scanner.next();

        if (ExpressionUtils.isVariable(firstToken)) {
            if (!scanner.hasNext()) {
                return false;
            }

            String secondToken = scanner.next();
            Operator operator = Operator.fromValue(secondToken);

            boolean is =  Operator.ASSIGN.equals(operator) || Operator.ADD_ASSIGN.equals(operator);

            return is;
        }

        return false;
    }
    //endregion
}
