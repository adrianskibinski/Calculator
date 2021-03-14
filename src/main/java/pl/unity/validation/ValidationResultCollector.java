package pl.unity.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ValidationResultCollector implements Collector<ValidationResult, List<String>, ValidationResult> {
    @Override
    public Supplier<List<String>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<String>, ValidationResult> accumulator() {
        return (errors, validationResult) -> errors.addAll(validationResult.getErrors());
    }

    @Override
    public BinaryOperator<List<String>> combiner() {
        return (errorList1, errorList2) -> {
            List<String> errors = new ArrayList<>();
            errors.addAll(errorList1);
            errors.addAll(errorList2);
            return errors;
        };
    }

    @Override
    public Function<List<String>, ValidationResult> finisher() {
        return errors -> {
            if (errors.isEmpty()) {
                return new ValidationSuccess();
            } else {
                return ValidationResult.failure(errors);
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
}
