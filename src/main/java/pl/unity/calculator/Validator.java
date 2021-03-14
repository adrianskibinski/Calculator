package pl.unity.calculator;

import pl.unity.validation.ValidationResult;

public interface Validator {
    ValidationResult validate(CalculationArguments arguments);
}
