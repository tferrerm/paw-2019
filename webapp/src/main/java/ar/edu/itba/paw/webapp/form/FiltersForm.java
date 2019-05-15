package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class FiltersForm {

	@Size(max=64)
	private String name;
	
	@Size(max=64)
	private String establishment;

	@Size(max=64)
	private String sport;

	@Size(max=64)
	private String vacancies;

	@Size(max=64)
	private String date;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEstablishment() {
		return establishment;
	}

	public void setEstablishment(String establishment) {
		this.establishment = establishment;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getVacancies() {
		return vacancies;
	}

	public void setVacancies(String vacancies) {
		this.vacancies = vacancies;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
