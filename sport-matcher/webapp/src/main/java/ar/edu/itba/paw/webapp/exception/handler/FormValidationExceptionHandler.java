package ar.edu.itba.paw.webapp.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.paw.webapp.dto.exception.FormValidationExceptionDto;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.http.CustomStatus;

@Provider
public class FormValidationExceptionHandler implements ExceptionMapper<FormValidationException> {

	@Override
	public Response toResponse(FormValidationException exception) {
		return Response
				.status(CustomStatus.UNPROCESSABLE_ENTITY)
				.entity(FormValidationExceptionDto.ofException(exception))
				.build();
	}

}
