package residentevil.common.validators;

import residentevil.common.annotations.DateBeforeToday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateBeforeTodayValidator implements ConstraintValidator<DateBeforeToday, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.isBefore(LocalDate.now());
    }
}