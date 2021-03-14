package pl.unity.validation;

import java.util.Collections;
import java.util.List;

public class ValidationSuccess implements ValidationResult {

    @Override
    public List<String> getErrors() {
        return Collections.emptyList();
    }

    @Override
    public boolean isFailure() {
        return false;
    }
}
