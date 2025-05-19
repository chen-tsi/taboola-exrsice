package org.taboola.evaluator;

/**
 * Represents an evaluable expression or entity that can produce an integer result.
 */
public interface Evaluable {

    /**
     * Evaluates the expression or entity and returns its integer result.
     *
     * @return the evaluated integer result
     */
    int eval();
}
