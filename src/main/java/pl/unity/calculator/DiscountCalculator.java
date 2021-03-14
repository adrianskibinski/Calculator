package pl.unity.calculator;


import pl.unity.validation.ValidationException;
import pl.unity.validation.ValidationResult;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountCalculator implements Calculator {

    private final Validator validator;

    public DiscountCalculator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public List<Integer> calculate(CalculationArguments arguments) {
        ValidationResult validationResult = validator.validate(arguments);
        if (validationResult.isFailure()) {
            throw new ValidationException(String.join(", ", validationResult.getErrors()));
        }

        float discountMultiplier = calculateDiscountMultiplier(arguments);
        List<Integer> pricesDiscounts = arguments.getPrices().stream()
                .map(price -> price * discountMultiplier)
                .map(Float::intValue)
                .collect(Collectors.toList());

        if (discountMultiplier < 1) {
            applyRemainingDiscount(pricesDiscounts, arguments.getDiscount());
        }

        return pricesDiscounts;
    }

    private void applyRemainingDiscount(List<Integer> pricesDiscounts, int discount) {
        int lastElemId = pricesDiscounts.size() - 1;
        int appliedDiscount = pricesDiscounts.stream().reduce(0, Integer::sum);

        pricesDiscounts.set(lastElemId, pricesDiscounts.get(lastElemId) + (discount - appliedDiscount));
    }

    private float calculateDiscountMultiplier(CalculationArguments arguments) {
        float pricesSum = arguments.getPrices().stream().reduce(0, Integer::sum);
        float calculatedDiscount = (pricesSum > 0) ? arguments.getDiscount() / pricesSum : 0;

        return (calculatedDiscount <= 1) ? calculatedDiscount : 1;
    }
}
