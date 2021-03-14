package pl.unity.validation;

import java.util.List;

import static java.util.Collections.singletonList;

public class ValidationFailure implements ValidationResult {

    List<String> errors;

    public ValidationFailure(String errors) {
        this.errors = singletonList(errors);
    }

    public ValidationFailure(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public boolean isFailure() {
        return true;
    }
}
