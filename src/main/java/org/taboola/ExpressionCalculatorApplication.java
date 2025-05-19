package org.taboola;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.taboola.exceptions.InvalidExpression;
import org.taboola.exceptions.UndefinedVariableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for the Expression Calculator application.
 * <p>
 * This is a Spring Boot application that accepts numeric expressions from standard input,
 * evaluates them, and displays results or errors.
 * <p>
 * Supports variable assignments, arithmetic operations, and both pre/post increments.
 */
@Slf4j
@SpringBootApplication
public class ExpressionCalculatorApplication implements CommandLineRunner {

    //region Members

    /**
     * The core calculator component responsible for parsing and evaluating expressions.
     */
    private final ExpressionCalculator calculator;

    //endregion

    //region Constructors

    /**
     * Constructs the application with the required calculator dependency.
     *
     * @param calculator the expression calculator to be used for evaluation
     */
    @Autowired
    public ExpressionCalculatorApplication(ExpressionCalculator calculator) {
        this.calculator = calculator;
    }
    //endregion

    //region Public Methods

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(ExpressionCalculatorApplication.class, args);
    }

    /**
     * Command-line runner implementation that starts an interactive loop,
     * allowing the user to input expressions, evaluate them, and see results or error messages.
     *
     * @param args arguments passed from the command line (ignored)
     */
    @Override
    public void run(String... args) {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        log.info("Welcome to the Numeric Expression Calculator!\n" +
                "--------------------------------------------------\n" +
                "[✓] Supported operations: +, -, *, =, +=, ++ (prefix and postfix)\n" +
                "[✓] Enter numeric expressions using valid syntax and supported operations.\n" +
                "[✓] Separate all operands and operators with a **single whitespace**.\n" +
                "[✓] To finish entering expressions and view the final result, press **Enter** on an empty line.\n" +
                "--------------------------------------------------");

        while (true) {
            try {
                String expression = buffer.readLine();

                // User pressed enter without input: display result and reset state
                if (expression == null || expression.trim().isEmpty()) {
                    log.info(calculator.getVariablesAsString());
                    calculator.reset();
                    continue;
                }

                // Try to evaluate expression and handle expected errors
                try {
                    calculator.calculate(expression);
                } catch (InvalidExpression e) {
                    log.error("The expression '{}' is invalid.", expression);
                } catch (UndefinedVariableException e) {
                    log.error("There is an undefined variable in the expression '{}'.", expression);
                }
            } catch (IOException e) {
                log.error("There was an error while reading or evaluating the expression.", e);
            }
        }
    }
    //endregion
}
