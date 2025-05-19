package taboola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.taboola.ExpressionValidator;

import static org.junit.jupiter.api.Assertions.*;


public class ExpressionValidatorTest {

    private ExpressionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ExpressionValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "x = 5 + 3",
            "x++",
            "++x",
            "y += 2 * 3",
            "z = (4 + 2) * 3",
            "x = ++y + z++",
            "z = ((1 + 2) * 3)",
            "x += (1 + 2) * ++y + z++",
            "x = ((1 + 2) * (3 + 4))",
            "z += (1 + x) * (3 - a)"
    })
    void shouldAcceptValidExpression(String expression) {
        assertTrue(validator.isValidExpression(expression), "Expected valid: " + expression);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "x = 5 + $",
            "x = 7 @ 2",
            "x = ++ y",
            "x = y ++",
            "x = 5 5",
            "x = y z",
            "x = (5 + 5) 4",
            "x = (5 + z) y",
            "x = 3 (4 + 5)",
            "x = (5 + 3",
            "x = 5 + 3)",
            "x = ((5 + 2)",
            "x = ()",
            "x = ( )",
            "()",
            "--x",
            "x--",
            "x -= 1",
            "x +=",
            "y =",
            "= y",
            "(x + 1) + (1 + 2)",
            "y",
            "33",
            "x = 6 - + 6"
    })
    void shouldRejectInvalidExpression(String expression) {
        assertFalse(validator.isValidExpression(expression), "Expected invalid: " + expression);
    }
}