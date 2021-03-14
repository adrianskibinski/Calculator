package pl.unity.calculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.unity.validation.ValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountCalculatorTest {

    private final Calculator calculator = new DiscountCalculator(new DiscountValidator());

    @ParameterizedTest
    @MethodSource("generateSuccessfulArguments")
    void testIfReturnsCorrectDiscounts(CalculationArguments arguments, List<Integer> expectedValues) {
        assertEquals(expectedValues, calculator.calculate(arguments));
    }

    @ParameterizedTest
    @MethodSource("generateUnsuccessfulArguments")
    void testIfReturnsCorrectValidationMessages(CalculationArguments arguments, String errorMessage) {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> calculator.calculate(arguments));
        assertEquals(errorMessage, exception.getMessage());
    }

    private static Stream<Arguments> generateSuccessfulArguments() {
        return Stream.of(
                Arguments.of(
                        new CalculationArguments(Arrays.asList(500_00, 1500_00), 100_00),
                        Arrays.asList(25_00, 75_00)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(530_99, 1530_99), 123_99),
                        Arrays.asList(31_92, 92_07)),
                Arguments.of(
                        new CalculationArguments(Collections.singletonList(3_33), 1_11),
                        Collections.singletonList(1_11)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 3_33), 15),
                        Arrays.asList(0, 15)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 3_33), 3_34),
                        Arrays.asList(1, 3_33)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 3_33), 3_33),
                        Arrays.asList(0, 3_33)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(11, 22, 33, 44, 55), 1_11),
                        Arrays.asList(7, 14, 22, 29, 39)),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 22_99, 3, 444_44, 5555_55), 1111_11),
                        Arrays.asList(0, 4_24, 0, 81_98, 1024_89))
        );
    }

    private static Stream<Arguments> generateUnsuccessfulArguments() {
        return Stream.of(
                Arguments.of(null, "arguments.shouldNotBeNull"),
                Arguments.of(
                        new CalculationArguments(null, -5),
                        "arguments.discount.shouldBeGreaterThanZero, arguments.prices.shouldNotBeNull"),
                Arguments.of(
                        new CalculationArguments(null, 100_00),
                        "arguments.prices.shouldNotBeNull"),
                Arguments.of(
                        new CalculationArguments(Collections.emptyList(), 100_00),
                        "arguments.prices.shouldNotBeEmpty"),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 2, 3, 4, 5), -5),
                        "arguments.discount.shouldBeGreaterThanZero"),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 2, -3, -4, 5), 1_00),
                        "arguments.prices.price[2].shouldBeGreaterThanZero, arguments.prices.price[3].shouldBeGreaterThanZero"),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, 2, 3, 4, 5, 6), 1_00),
                        "arguments.prices.shouldBeNoMoreThanFive"),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, null, 3, 4, 5, null), 1_00),
                        "arguments.prices.shouldBeNoMoreThanFive"),
                Arguments.of(
                        new CalculationArguments(Arrays.asList(1, null, 3, null, 5), 1_00),
                        "arguments.prices.price[1].shouldNotBeNull, arguments.prices.price[3].shouldNotBeNull")
        );
    }
}