package taboola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.taboola.ExpressionCalculator;
import org.taboola.ExpressionValidator;
import org.taboola.exceptions.InvalidExpression;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpressionCalculatorTest {

    private ExpressionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ExpressionCalculator(new ExpressionValidator());
    }

    @ParameterizedTest
    @MethodSource("expressionProvider")
    void shouldCalculateExpression(List<String> expressions, List<Integer> expectedResults, Map<Character, Integer> expectedVariables) {
        for (int i = 0; i < expressions.size(); i++) {
            String expression = expressions.get(i);
            int expectedResult = expectedResults.get(i);
            int result = calculator.calculate(expression);
            assertEquals(expectedResult, result, "Unexpected result for expression: " + expression);

        }

        assertEquals(expectedVariables, calculator.getVariables(), "Unexpected variables for expressions: " + expressions);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "x = + 1",
            "x = 5 + $",
            ")",
            "(",
    })
    void shouldRaiseInvalidExpression(String expression) {
        assertThrows(InvalidExpression.class, () -> {
            calculator.calculate(expression);
        }, "Expected an exception for expression: " + expression);
    }

    private Stream<Arguments> expressionProvider() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(List.of("x = 1"), List.of(1), Map.of('x', 1)),
                org.junit.jupiter.params.provider.Arguments.of(List.of("y = 2 + 3"), List.of(5), Map.of('y', 5)),
                org.junit.jupiter.params.provider.Arguments.of(List.of("z = (2 + 3) * 2"), List.of(10), Map.of('z', 10)),
                org.junit.jupiter.params.provider.Arguments.of(List.of("a = 5"), List.of(5), Map.of('a', 5)),
                org.junit.jupiter.params.provider.Arguments.of(List.of("x = 1", "w = x++ + 1"), List.of(1, 2), Map.of('x', 2, 'w', 2)),
                org.junit.jupiter.params.provider.Arguments.of(List.of("x = 1", "w = 1" , "w += ++x + 1"), List.of(1, 1, 4), Map.of('x', 2, 'w', 4))
        );
    }
}
