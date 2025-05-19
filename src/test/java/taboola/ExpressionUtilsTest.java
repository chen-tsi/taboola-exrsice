package taboola;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.taboola.ExpressionUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "123, true",
            "0, true",
            "12a, false",
            "a, false",
            "'', false",
            "a34, false",
            "3.2, false",
            "i++, false"
    })
    void testIsNumber(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isNumber(input));
    }

    @ParameterizedTest
    @CsvSource({
            "x, true",
            "a, true",
            "ab, false",
            "A, false",
            "1, false",
            "++i, false"
    })
    void testIsVariable(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isVariable(input));
    }

    @ParameterizedTest
    @CsvSource({
            "++x, true",
            "x++, false",
            "x, false",
            "+x, false",
            "++xx, false",
            "12, false"
    })
    void testIsPreIncrement(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isPreIncrement(input));
    }

    @ParameterizedTest
    @CsvSource({
            "x++, true",
            "++x, false",
            "x, false",
            "x+++, false",
            "xy++, false",
            "34, false"
    })
    void testIsPostIncrement(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isPostIncrement(input));
    }

    @ParameterizedTest
    @CsvSource({
            "'(', true",
            "')', false",
            "'[', false",
            "'()', false",
            "3, false",
            "x, false"
    })
    void testIsOpeningParenthesis(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isOpeningParenthesis(input));
    }

    @ParameterizedTest
    @CsvSource({
            "')', true",
            "'(', false",
            "']', false",
            "')(', false",
            "x, false",
            "x++, false",
            "3, false"
    })
    void testIsClosingParenthesis(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isClosingParenthesis(input));
    }

    @ParameterizedTest
    @CsvSource({
            "+, true",
            "-, true",
            "*, true",
            "=, true",
            "+=, true",
            "++, false",
            "x, false",
            "==, false",
            "/, false",
            "3, false"
    })
    void testIsOperator(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isOperator(input));
    }

    @ParameterizedTest
    @CsvSource({
            "++x, true",
            "x++, true",
            "x, false",
            "+x, false",
            "45, false"
    })
    void testIsUnaryOperator(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isUnaryOperator(input));
    }

    @ParameterizedTest
    @CsvSource({
            "5, true",
            "x, true",
            "x++, true",
            "++x, true",
            "+, false",
            "xy, false",
            "'', false",
            "), false",
            "(, false",
            "++, false"
    })
    void testIsOperand(String input, boolean expected) {
        assertEquals(expected, ExpressionUtils.isOperand(input));
    }
}
