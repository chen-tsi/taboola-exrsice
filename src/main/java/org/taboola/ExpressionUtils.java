package org.taboola;

import java.util.regex.Pattern;

/**
 * Utility class providing methods to identify and classify
 * different types of numeric expressions.
 */
public class ExpressionUtils {

    /**
     * Checks if the input string represents a number (integer only).
     *
     * @param s the input string
     * @return true if the string is a number, false otherwise
     */
    public static boolean isNumber(String s) {
        return Pattern.matches("[0-9]+", s);
    }

    /**
     * Checks if the input string is a single lowercase variable (a-z).
     *
     * @param s the input string
     * @return true if the string is a variable, false otherwise
     */
    public static boolean isVariable(String s) {
        return Pattern.matches("[a-z]", s);
    }

    /**
     * Checks if the input string is a pre increment expression (e.g., ++x).
     *
     * @param s the input string
     * @return true if the string is a pre increment expression, false otherwise
     */
    public static boolean isPreIncrement(String s) {
        return Pattern.matches("\\+\\+[a-z]", s);
    }

    /**
     * Checks if the input string is a post increment expression (e.g., x++).
     *
     * @param s the input string
     * @return true if the string is a post increment expression, false otherwise
     */
    public static boolean isPostIncrement(String s) {
        return Pattern.matches("[a-z]\\+\\+", s);
    }

    /**
     * Checks if the input string is an opening parenthesis "(".
     *
     * @param s the input string
     * @return true if the string is an opening parenthesis, false otherwise
     */
    public static boolean isOpeningParenthesis(String s) {
        return "(".equals(s);
    }

    /**
     * Checks if the input string is a closing parenthesis ")".
     *
     * @param s the input string
     * @return true if the string is a closing parenthesis, false otherwise
     */
    public static boolean isClosingParenthesis(String s) {
        return ")".equals(s);
    }

    /**
     * Checks if the input string is a supported operator.
     * Supported operators include: +, -, *, =, and +=
     *
     * @param s the input string
     * @return true if the string is an operator, false otherwise
     */
    public static boolean isOperator(String s) {
        return Pattern.matches("[+\\-*=]|\\+=", s);
    }

    /**
     * Checks if the input string is a unary operator expression
     * (either pre increment or post increment).
     *
     * @param s the input string
     * @return true if the string is a unary operator expression, false otherwise
     */
    public static boolean isUnaryOperator(String s) {
        return isPostIncrement(s) || isPreIncrement(s);
    }

    /**
     * Checks if the input string is a valid operand.
     * Valid operands include numbers, variables, and unary operator expressions.
     *
     * @param s the input string
     * @return true if the string is an operand, false otherwise
     */
    public static boolean isOperand(String s) {
        return isNumber(s) || isVariable(s) || isUnaryOperator(s);
    }
}
