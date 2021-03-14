package pl.unity.calculator;

import java.util.List;

public class CalculationArguments {
    private final List<Integer> prices;
    private final int discount;

    public CalculationArguments(List<Integer> prices, int discount) {
        this.prices = prices;
        this.discount = discount;
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public int getDiscount() {
        return discount;
    }
}
