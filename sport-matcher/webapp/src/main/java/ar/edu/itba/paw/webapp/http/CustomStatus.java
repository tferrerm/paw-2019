package ar.edu.itba.paw.webapp.http;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public enum CustomStatus implements StatusType {
	
	UNPROCESSABLE_ENTITY(422, Family.CLIENT_ERROR, "Unprocessable Entity");
	
	private int statusCode;
	private Family family;
	private String reasonPhrase;
	
	CustomStatus(int statusCode, Family family, String reasonPhrase) {
		this.statusCode = statusCode;
		this.family = family;
		this.reasonPhrase = reasonPhrase;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public Family getFamily() {
		return family;
	}

	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

}
