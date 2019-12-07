package ar.edu.itba.paw.webapp.dto.form.validator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureDateValidator implements ConstraintValidator<FutureDate, Instant> {
	
	private boolean startOfDay;
	private String timezone;

	@Override
	public void initialize(FutureDate constraintAnnotation) {
		startOfDay = constraintAnnotation.startOfDay();
		timezone = constraintAnnotation.timezone();
	}

	@Override
	public boolean isValid(Instant date, ConstraintValidatorContext context) {
		if(startOfDay) {
			return date != null && date.isAfter(LocalDate.now()
					.atStartOfDay(ZoneId.of(timezone)).toInstant());
		}
		return date != null && date.isAfter(Instant.now().plus(1, ChronoUnit.MINUTES));
	}
}
