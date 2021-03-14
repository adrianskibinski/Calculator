package pl.unity;

import pl.unity.calculator.CalculationArguments;
import pl.unity.calculator.Calculator;
import pl.unity.calculator.DiscountCalculator;
import pl.unity.calculator.DiscountValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ApplicationMain {
    public static void main(String[] args) {
        Calculator calculator = new DiscountCalculator(new DiscountValidator());
        CalculationArguments arguments = new CalculationArguments(
                new ArrayList<>(Arrays.asList(500_00,1500_00)),
                100_00);

        List<Integer> pricesDiscounts = calculator.calculate(arguments);
        IntStream.range(0, arguments.getPrices().size())
            .mapToObj(i -> arguments.getPrices().get(i) + " -> " +  pricesDiscounts.get(i))
            .forEach(System.out::println);
    }
}
