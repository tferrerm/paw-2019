package ar.edu.itba.paw.webapp.dto.form.validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureDateValidator implements ConstraintValidator<FutureDate, Instant> {

	@Override
	public void initialize(FutureDate constraintAnnotation) {
	}

	@Override
	public boolean isValid(Instant date, ConstraintValidatorContext context) {
		return date != null && date.isAfter(Instant.now().plus(1, ChronoUnit.MINUTES));
	}
}
