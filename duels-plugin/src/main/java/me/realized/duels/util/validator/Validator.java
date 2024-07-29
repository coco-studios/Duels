package me.realized.duels.util.validator;

public interface Validator<T> {
    
    boolean shouldValidate();

    boolean validate(final T validated);
}
