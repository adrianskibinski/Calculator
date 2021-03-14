package pl.unity.calculator;


import pl.unity.validation.ValidationFailure;
import pl.unity.validation.ValidationResult;
import pl.unity.validation.ValidationResultCollector;
import pl.unity.validation.ValidationSuccess;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class DiscountValidator implements Validator {
    public ValidationResult validate(CalculationArguments arguments) {
        return Stream.<Function<CalculationArguments, ValidationResult>>of(
                this::validateDiscountGreaterThanZero,
                this::validateArgumentsNotNull,
                this::validatePricesNotNull,
                this::validatePrices
        ).map(validation -> validation.apply(arguments))
                .collect(new ValidationResultCollector());
    }

    private ValidationResult validatePricesNotNull(CalculationArguments arguments) {
        return (!isNull(arguments) && isNull(arguments.getPrices()))
                ? new ValidationFailure("arguments.prices.shouldNotBeNull")
                : new ValidationSuccess();
    }

    private ValidationResult validateArgumentsNotNull(CalculationArguments arguments) {
        return (isNull(arguments))
                ? new ValidationFailure("arguments.shouldNotBeNull")
                : new ValidationSuccess();
    }

    private ValidationResult validateDiscountGreaterThanZero(CalculationArguments arguments) {
        return (!isNull(arguments) && arguments.getDiscount() < 0)
                ? new ValidationFailure("arguments.discount.shouldBeGreaterThanZero")
                : new ValidationSuccess();
    }

    private ValidationResult validatePrices(CalculationArguments arguments) {
        if (isNull(arguments) || isNull(arguments.getPrices())) {
            return new ValidationSuccess();
        }

        ValidationResult result = validatePricesSize(arguments);
        if (result.isFailure()) {
            return result;
        }

        return IntStream.range(0, arguments.getPrices().size())
                .mapToObj(i -> validatePrice(arguments.getPrices().get(i), i))
                .collect(new ValidationResultCollector());
    }

    private ValidationResult validatePricesSize(CalculationArguments arguments) {
        if (arguments.getPrices().size() > 5) {
            return new ValidationFailure("arguments.prices.shouldBeNoMoreThanFive");
        }

        if (arguments.getPrices().size() == 0) {
            return new ValidationFailure("arguments.prices.shouldNotBeEmpty");
        }
        return new ValidationSuccess();
    }

    private ValidationResult validatePrice(Integer price, int i) {
        return Stream.<Function<Integer, ValidationResult>>of(
                this::validatePriceNotNull,
                this::validatePriceGreaterThanZero
        ).map(validation -> validation.apply(price))
                .map(result -> formatErrorToSpecificPrice(i, result))
                .collect(new ValidationResultCollector());
    }

    private ValidationResult formatErrorToSpecificPrice(int i, ValidationResult result) {
        return result.getErrors().stream()
                .map(error -> ValidationResult.failure(String.format("arguments.prices.price[%d]." + error, i)))
                .collect(new ValidationResultCollector());
    }

    private ValidationResult validatePriceGreaterThanZero(Integer price) {
        return (!isNull(price) && price <= 0)
                ? new ValidationFailure("shouldBeGreaterThanZero")
                : new ValidationSuccess();
    }

    private ValidationResult validatePriceNotNull(Integer price) {
        return (isNull(price))
                ? new ValidationFailure("shouldNotBeNull")
                : new ValidationSuccess();
    }
}
