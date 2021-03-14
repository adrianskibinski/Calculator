package pl.unity.validation;

import java.util.List;

public interface ValidationResult {
    static ValidationResult failure(String errors) {
        return new ValidationFailure(errors);
    }

    static ValidationResult failure(List<String> errors) {
        return new ValidationFailure(errors);
    }

    List<String> getErrors();

    boolean isFailure();
}
